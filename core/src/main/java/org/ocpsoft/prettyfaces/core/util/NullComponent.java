package org.ocpsoft.prettyfaces.core.util;

import javax.faces.component.UIComponentBase;

public class NullComponent extends UIComponentBase
{
   @Override
   public String getFamily()
   {
      return "org.ocpsoft.prettyfaces.NullComponent";
   }
}