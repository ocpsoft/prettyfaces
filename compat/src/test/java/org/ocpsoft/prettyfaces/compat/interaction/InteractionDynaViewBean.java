package org.ocpsoft.prettyfaces.compat.interaction;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/page")
@ForwardTo("/page.xhtml")
public class InteractionDynaViewBean
{
   private String value;

   public String viewId()
   {
      return "/" + value + ".jsf";
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
