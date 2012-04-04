package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.MappingId;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;

public class MappingIdHandler implements AnnotationHandler<MappingId>
{

   @Override
   public Class<MappingId> handles()
   {
      return MappingId.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, MappingId annotation)
   {
      context.getRuleBuilder().withId(annotation.value());
   }

}
