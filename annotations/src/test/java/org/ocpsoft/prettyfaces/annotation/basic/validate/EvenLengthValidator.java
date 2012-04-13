package org.ocpsoft.prettyfaces.annotation.basic.validate;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("EvenLengthValidator")
public class EvenLengthValidator implements Validator
{

   @Override
   public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
   {
      if (value == null || value.toString().length() % 2 != 0) {
         throw new ValidatorException(new ArrayList<FacesMessage>());
      }
   }

}
