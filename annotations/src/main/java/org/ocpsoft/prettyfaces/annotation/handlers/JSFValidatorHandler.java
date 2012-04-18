package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.Field;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.prettyfaces.core.util.NullComponent;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.FieldAnnotationHandler;
import org.ocpsoft.rewrite.bind.BindingBuilder;
import org.ocpsoft.rewrite.bind.Validator;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;

/**
 * @author Christian Kaltepoth
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JSFValidatorHandler extends FieldAnnotationHandler<JSFValidator>
{

   private final Logger log = Logger.getLogger(JSFValidatorHandler.class);

   @Override
   public Class<JSFValidator> handles()
   {
      return JSFValidator.class;
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void process(FieldContext context, Field field, JSFValidator annotation)
   {

      // locate the binding previously created by @ParameterBinding
      BindingBuilder bindingBuilder = context.getBindingBuilder();
      if (bindingBuilder == null) {
         throw new IllegalStateException("No binding found for field: " + field.getName());
      }

      // validation using a JSF validatorId
      if (annotation.validatorId().trim().length() > 0) {
         JSFRewriteValidator validator = new JSFRewriteValidator(annotation.validatorId());
         bindingBuilder.validatedBy(validator);

         if (log.isTraceEnabled()) {
            log.trace("Attaching JSF validator [{}] to field [{}] of class [{}]", new Object[] {
                     annotation.validatorId(), field.getName(), field.getDeclaringClass().getName()
            });
         }
      }

      // validation using a JSF validator method
      else if (annotation.validateWith().trim().length() > 0) {

         JSFMethodRewriteValidator validator = new JSFMethodRewriteValidator(annotation.validateWith());
         bindingBuilder.validatedBy(validator);

         if (log.isTraceEnabled()) {
            log.trace("Attaching JSF method validator expression [{}] to field [{}] of class [{}]", new Object[] {
                     annotation.validateWith(), field.getName(), field.getDeclaringClass().getName()
            });
         }
      }
   }

   /**
    * Implementation of {@link Validator} that uses the supplied JSF validator for validation
    */
   private static class JSFRewriteValidator<T> implements Validator<T>
   {
      private final String validatorId;

      public JSFRewriteValidator(String validatorId)
      {
         this.validatorId = validatorId;
      }

      @Override
      public boolean validate(Rewrite event, EvaluationContext context, T value)
      {
         FacesContext facesContext = FacesContext.getCurrentInstance();
         if (facesContext == null) {
            throw new IllegalStateException("Could not get FacesContext");
         }

         javax.faces.validator.Validator validator = facesContext.getApplication().createValidator(validatorId);

         try {
            validator.validate(null, new NullComponent(), value);
            return true;
         }
         catch (ValidatorException e) {
            return false;
         }
      }
   }

   /**
    * Implementation of {@link Validator} that executes a validator method using the supplied EL expression
    */
   private static class JSFMethodRewriteValidator<T> implements Validator<T>
   {
      private final Logger log = Logger.getLogger(JSFMethodRewriteValidator.class);

      private final String expression;

      public JSFMethodRewriteValidator(String expression)
      {
         this.expression = expression;
      }

      @Override
      public boolean validate(Rewrite event, EvaluationContext context, T value)
      {
         FacesContext facesContext = FacesContext.getCurrentInstance();
         if (facesContext == null) {
            throw new IllegalStateException("Could not get FacesContext");
         }

         ELContext elContext = facesContext.getELContext();
         ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
         MethodExpression methodExpression = expressionFactory.createMethodExpression(elContext, expression,
                  null, new Class[] { FacesContext.class, UIComponent.class, Object.class });

         try {
            methodExpression.invoke(elContext, new Object[] { facesContext, new NullComponent(), value });
            return true;
         }
         catch (ELException e) {

            // the method has thrown a ValidatorException
            if (e.getCause() instanceof ValidatorException) {
               return false;
            }

            // all other causes are unexpected
            // TODO should this re-throw instead of returning false? this is an error scenario after-all
            else {
               log.error("Failed to invoke validator method: " + expression, e);
               return false;
            }
         }
      }
   }
}
