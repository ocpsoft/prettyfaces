package org.ocpsoft.prettyfaces.annotation.basic;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.prettyfaces.annotation.RequestAction;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/basic/{value}")
@ForwardTo("/faces/basic.xhtml")
public class BasicMappingBean
{

   @ParameterBinding
   private String value;

   private boolean actionInvoked = false;

   @RequestAction
   public void action()
   {
      actionInvoked = true;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

   public boolean isActionInvoked()
   {
      return actionInvoked;
   }

}
