package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.QueryParameterBinding;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/page")
@ForwardTo("/faces/query-param-basic.xhtml")
public class QueryParameterBean
{

   @QueryParameterBinding("q")
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
