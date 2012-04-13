package org.ocpsoft.prettyfaces.annotation.basic.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("UppercaseConverter")
public class UppercaseConverter implements Converter
{

   @Override
   public Object getAsObject(FacesContext context, UIComponent component, String value)
   {
      if (value != null) {
         return value.toUpperCase();
      }
      return null;
   }

   @Override
   public String getAsString(FacesContext context, UIComponent component, Object value)
   {
      return value.toString();
   }

}
