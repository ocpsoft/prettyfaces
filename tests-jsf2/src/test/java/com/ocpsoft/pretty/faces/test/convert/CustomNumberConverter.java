package com.ocpsoft.pretty.faces.test.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("customNumberConverter")
public class CustomNumberConverter implements Converter
{

   @Override
   public Object getAsObject(FacesContext context, UIComponent component, String value)
   {
      if("one".equals(value)) {
         return 1;
      }
      if("two".equals(value)) {
         return 2;
      }
      if("three".equals(value)) {
         return 3;
      }
      return 0;
   }

   @Override
   public String getAsString(FacesContext context, UIComponent component, Object value)
   {
      if(Integer.valueOf(1).equals(value)) {
         return "one";
      }
      if(Integer.valueOf(2).equals(value)) {
         return "two";
      }
      if(Integer.valueOf(3).equals(value)) {
         return "three";
      }
      return "unknown";
   }

}
