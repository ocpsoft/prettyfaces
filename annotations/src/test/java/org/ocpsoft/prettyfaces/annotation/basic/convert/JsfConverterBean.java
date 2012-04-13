package org.ocpsoft.prettyfaces.annotation.basic.convert;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.JSFConverter;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/convert/{value}")
@ForwardTo("/faces/convert.xhtml")
public class JsfConverterBean
{

   @ParameterBinding
   @JSFConverter(converterId = "UppercaseConverter")
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
