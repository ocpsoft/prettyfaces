package org.ocpsoft.prettyfaces.annotation.basic;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.prettyfaces.annotation.Join;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.prettyfaces.annotation.URLAction;

@ManagedBean
@RequestScoped
@Join(path = "/basic/{value}", to = "/faces/basic.xhtml")
public class BasicJoinBean
{

   @ParameterBinding
   private String value;

   private boolean actionInvoked = false;

   @URLAction
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
