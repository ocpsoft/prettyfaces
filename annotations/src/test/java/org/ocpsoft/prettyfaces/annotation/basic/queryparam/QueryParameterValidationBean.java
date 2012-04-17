package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.JSFValidator;
import org.ocpsoft.prettyfaces.annotation.QueryParameterBinding;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/page")
@ForwardTo("/faces/query-param-validation.xhtml")
public class QueryParameterValidationBean
{

   @QueryParameterBinding("q")
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
