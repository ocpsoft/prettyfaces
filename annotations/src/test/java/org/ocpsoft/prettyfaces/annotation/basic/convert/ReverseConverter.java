package org.ocpsoft.prettyfaces.annotation.basic.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("ReverseConverter")
public class ReverseConverter implements Converter
{

   @Override
   public Object getAsObject(FacesContext context, UIComponent component, String value)
   {
      if (value != null) {
         return reverse(value);
      }
      return null;
   }

   @Override
   public String getAsString(FacesContext context, UIComponent component, Object value)
   {
      if (value != null) {
         return reverse(value.toString());
      }
      return null;
   }

   private static String reverse(String s)
   {
      return new StringBuilder(s).reverse().toString();
   }

}
