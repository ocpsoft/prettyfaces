package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.config.CompositeCondition;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.DefaultConditionBuilder;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.param.Constraint;
import org.ocpsoft.rewrite.param.Parameterized;

public class JSFValidatorHandler implements AnnotationHandler<JSFValidator>
{

   @Override
   public Class<JSFValidator> handles()
   {
      return JSFValidator.class;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void process(ClassContext context, AnnotatedElement element, JSFValidator annotation)
   {

      // works only for fields and not for methods
      if (element instanceof Field) {

         Field field = (Field) element;

         // create the custom constraint
         String validatorId = annotation.validatorId();
         JSFValidatorConstraint constraint = new JSFValidatorConstraint(validatorId);

         // register the constraint
         DefaultConditionBuilder condition = context.getRuleBuilder().getConditionBuilder();

         visitCondition(condition, field, constraint);

      }

   }

   private void visitCondition(Condition condition, Field field, Constraint<?> constraint)
   {
      if (condition instanceof CompositeCondition)
      {
         for (Condition c : ((CompositeCondition) condition).getConditions()) {
            if (condition instanceof CompositeCondition)
               visitCondition(condition, field, constraint);
         }
      }
      else if (condition instanceof Parameterized)
      {
         ((Parameterized) condition).where(field.getName()).constrainedBy(constraint);
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
