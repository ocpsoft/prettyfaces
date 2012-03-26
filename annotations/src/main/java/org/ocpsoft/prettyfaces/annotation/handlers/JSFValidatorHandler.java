package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.ocpsoft.prettyfaces.annotation.api.JSFValidator;
import org.ocpsoft.prettyfaces.annotation.scan.MappingBuilder;
import org.ocpsoft.prettyfaces.annotation.spi.AnnotationHandler;

import com.ocpsoft.rewrite.context.EvaluationContext;
import com.ocpsoft.rewrite.event.Rewrite;
import com.ocpsoft.rewrite.param.Constraint;

public class JSFValidatorHandler implements AnnotationHandler<JSFValidator>
{

   @Override
   public Class<JSFValidator> handles()
   {
      return JSFValidator.class;
   }

   @Override
   public void process(JSFValidator annotation, AnnotatedElement element, MappingBuilder builder)
   {

      // works only for fields and not for methods
      if (element instanceof Field) {

         Field field = (Field) element;

         // create the custom constraint
         String validatorId = annotation.validatorId();
         JSFValidatorConstraint constraint = new JSFValidatorConstraint(validatorId);

         // register the constraint
         builder.addParameterConstraint(field.getName(), constraint);

      }

   }

   /**
    * 
    * Implementation of {@link Constraint} which validates using a JSF validator
    * 
    * @author Christian Kaltepoth
    *
    */
   public static class JSFValidatorConstraint implements Constraint<String>
   {

      private final String validatorId;

      public JSFValidatorConstraint(String validatorId)
      {
         this.validatorId = validatorId;
      }

      @Override
      public boolean isSatisfiedBy(Rewrite event, EvaluationContext context, String value)
      {

         // obtain the JSF validator
         FacesContext facesContext = FacesContext.getCurrentInstance();
         Validator validator = facesContext.getApplication().createValidator(validatorId);

         // perform validation
         try {

            validator.validate(facesContext, null, value);
            return true;
         }
         catch (ValidatorException e) {
            return false;
         }
      }

   }
}
