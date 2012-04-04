package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.Priority;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;

public class PriorityHandler implements AnnotationHandler<Priority>
{

   @Override
   public Class<Priority> handles()
   {
      return Priority.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, Priority annotation)
   {
      context.getRuleBuilder().withPriority(annotation.value());
   }

}
