package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.ocpsoft.prettyfaces.annotation.api.NamedParameter;
import org.ocpsoft.prettyfaces.annotation.scan.MappingBuilder;
import org.ocpsoft.prettyfaces.annotation.spi.AnnotationHandler;

public class NamedParameterHandler implements AnnotationHandler<NamedParameter>
{

   @Override
   public Class<NamedParameter> handles()
   {
      return NamedParameter.class;
   }

   @Override
    public void process(NamedParameter annotation, AnnotatedElement element, MappingBuilder builder) {

      if(element instanceof Field) {
         Field field = (Field) element;

         // default name is the name of the field
         String paramName = field.getName();
         
         // but the name specified in the annotation is preferred
         if(!annotation.value().isEmpty()) {
            paramName = annotation.value();
         }
         
         builder.addParameterInjection(field, paramName);
         
      }
    
    }
}
