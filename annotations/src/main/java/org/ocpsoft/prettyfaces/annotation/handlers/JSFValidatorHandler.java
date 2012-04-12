package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.validator.ValidatorException;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.BindingBuilder;
import org.ocpsoft.rewrite.bind.Validator;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;

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

         // create the validator and attach it to the binding
         String validatorId = annotation.validatorId();
         JSFRewriteValidator validator = new JSFRewriteValidator(validatorId);
         bindingBuilder.validatedBy(validator);

         if (log.isTraceEnabled()) {
            log.trace("Attaching JSF validator [{}] to field [{}] of class [{}]", new Object[] {
                     validatorId, field.getName(), field.getDeclaringClass().getName()
            });
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

         // we need to create the Application ourself using the ApplicationFactory
         ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
         if (factory == null) {
            throw new IllegalArgumentException("Could not find ApplicationFactory");
         }

         // try to obtain the Application instance from the factory
         Application application = factory.getApplication();
         if (application == null) {
            throw new IllegalArgumentException("Unable to create Application");
         }

         // obtain the JSF validator
         javax.faces.validator.Validator validator = application.createValidator(validatorId);

         // perform validation
         try {

            validator.validate(null, null, value);
            return true;
         }
         catch (ValidatorException e) {
            return false;
         }

      }

   }

}
