package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.prettyfaces.core.util.NullComponent;
import org.ocpsoft.prettyfaces.core.util.NullLifecycle;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.BindingBuilder;
import org.ocpsoft.rewrite.bind.Validator;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

public class JSFValidatorHandler implements AnnotationHandler<JSFValidator>
{

   private final Logger log = Logger.getLogger(JSFValidatorHandler.class);

   @Override
   public Class<JSFValidator> handles()
   {
      return JSFValidator.class;
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void process(ClassContext classContext, AnnotatedElement element, JSFValidator annotation)
   {

      // works only for fields and not for methods
      if (element instanceof Field && classContext instanceof FieldContext) {

         Field field = (Field) element;
         FieldContext context = (FieldContext) classContext;

         // locate the binding previously created by @ParameterBinding
         BindingBuilder bindingBuilder = context.getBindingBuilder();
         if (bindingBuilder == null) {
            throw new IllegalStateException("No binding found for field: " + field.getName());
         }

         // validation using a JSF validatorId
         if (annotation.validatorId().trim().length() > 0) {

            // create the validator and attach it to the binding
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

            // create the validator and attach it to the binding
            JSFMethodRewriteValidator validator = new JSFMethodRewriteValidator(annotation.validateWith());
            bindingBuilder.validatedBy(validator);

            if (log.isTraceEnabled()) {
               log.trace("Attaching JSF method validator expression [{}] to field [{}] of class [{}]", new Object[] {
                        annotation.validateWith(), field.getName(), field.getDeclaringClass().getName()
               });
            }

         }

      }

   }

   /**
    * Implementation of {@link Validator} that uses the supplied JSF validator for validation
    * 
    * @author Christian Kaltepoth
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

         // we will build the FacesContext ourself
         FacesContextFactory factory =
                  (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
         if (factory == null) {
            throw new IllegalArgumentException("Could not find FacesContextFactory");
         }

         // we need HttpServletRewrite to obtain the HttpServletRequest, HttpServletResponse, etc
         if (event instanceof HttpServletRewrite) {
            HttpServletRewrite httpRewrite = (HttpServletRewrite) event;

            // TODO: getting the ServletContext from the request works in Servlet 3.0 only!
            ServletContext servletContext = httpRewrite.getRequest().getServletContext();
            HttpServletRequest request = httpRewrite.getRequest();
            HttpServletResponse response = httpRewrite.getResponse();

            // we have to properly release the FacesContext
            FacesContext facesContext = null;
            try {

               // build the FacesContext using a fake Lifecycle instance
               facesContext = factory.getFacesContext(servletContext, request, response, new NullLifecycle());
               if (facesContext == null) {
                  throw new IllegalStateException("Could not create FacesContext");
               }

               // obtain the JSF validator
               javax.faces.validator.Validator validator = facesContext.getApplication().createValidator(validatorId);

               // perform validation
               try {
                  validator.validate(null, new NullComponent(), value);
                  return true;
               }
               catch (ValidatorException e) {
                  return false;
               }

            }
            finally {
               if (facesContext != null) {
                  facesContext.release();
               }
            }

         }

         throw new IllegalStateException("Unsupported Rewrite event type: " + event.getClass().getName());

      }

   }

   /**
    * Implementation of {@link Validator} that executes a validator method using the supplied EL expression
    * 
    * @author Christian Kaltepoth
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

         // we will build the FacesContext ourself
         FacesContextFactory factory =
                  (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
         if (factory == null) {
            throw new IllegalArgumentException("Could not find FacesContextFactory");
         }

         // we need HttpServletRewrite to obtain the HttpServletRequest, HttpServletResponse, etc
         if (event instanceof HttpServletRewrite) {
            HttpServletRewrite httpRewrite = (HttpServletRewrite) event;

            // TODO: getting the ServletContext from the request works in Servlet 3.0 only!
            ServletContext servletContext = httpRewrite.getRequest().getServletContext();
            HttpServletRequest request = httpRewrite.getRequest();
            HttpServletResponse response = httpRewrite.getResponse();

            // we have to properly release the FacesContext
            FacesContext facesContext = null;
            try {

               // build the FacesContext using a fake Lifecycle instance
               facesContext = factory.getFacesContext(servletContext, request, response, new NullLifecycle());
               if (facesContext == null) {
                  throw new IllegalStateException("Could not create FacesContext");
               }

               // create a MethodExpression for the validation method
               ELContext elContext = facesContext.getELContext();
               ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
               MethodExpression methodExpression = expressionFactory.createMethodExpression(elContext, expression,
                        null, new Class[] { FacesContext.class, UIComponent.class, Object.class });

               // invoke the method
               try {

                  methodExpression.invoke(elContext, new Object[] { facesContext, new NullComponent(), value });
                  return true;

               }

               // exceptions may be unexpected or be caused by a validation failure
               catch (ELException e) {

                  // the method has thrown a ValidatorException
                  if (e.getCause() instanceof ValidatorException) {
                     return false;
                  }

                  // all other causes are unexpected
                  else {
                     log.error("Failed to invoke validator method: " + expression, e);
                     return false;
                  }
               }

            }
            finally {
               if (facesContext != null) {
                  facesContext.release();
               }
            }

         }

         throw new IllegalStateException("Unsupported Rewrite event type: " + event.getClass().getName());

      }

   }

}
