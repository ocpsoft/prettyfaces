package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.ocpsoft.prettyfaces.annotation.NamedParameter;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;

public class NamedParameterHandler implements AnnotationHandler<NamedParameter>
{

   @Override
   public Class<NamedParameter> handles()
   {
      return NamedParameter.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, NamedParameter annotation)
   {

      if (element instanceof Field) {
         Field field = (Field) element;

         // default name is the name of the field
         String paramName = field.getName();

         // but the name specified in the annotation is preferred
         if (!annotation.value().isEmpty()) {
            paramName = annotation.value();
         }

         // TODO walk the rule and add El binding to matching parameters
         // builder.addParameterInjection(field, paramName);

      }

   }
}
