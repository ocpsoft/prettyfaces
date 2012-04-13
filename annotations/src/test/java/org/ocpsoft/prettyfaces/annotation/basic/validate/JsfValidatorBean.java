package org.ocpsoft.prettyfaces.annotation.basic.validate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/validate/{value}")
@ForwardTo("/faces/validate.xhtml")
public class JsfValidatorBean
{

   @ParameterBinding
   @JSFValidator(validatorId = "EvenLengthValidator")
   private String value;

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

}
