package org.ocpsoft.prettyfaces.annotation.basic.validate;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/validate/{value}")
@ForwardTo("/faces/method-validation.xhtml")
public class MethodValidationBean
{

   @ParameterBinding
   @JSFValidator(validateWith = "#{methodValidationBean.validate}")
   private String value;

   public void validate(FacesContext context, UIComponent comp, Object value)
   {
      if (value.toString().contains("666")) {
         throw new ValidatorException(new FacesMessage());
      }
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

}
