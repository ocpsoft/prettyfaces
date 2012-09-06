package org.ocpsoft.prettyfaces.annotation.handlers;

import org.ocpsoft.prettyfaces.annotation.Priority;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;

public class PriorityHandler implements AnnotationHandler<Priority>
{

   @Override
   public Class<Priority> handles()
   {
      return Priority.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, Priority annotation, HandlerChain chain)
   {
      context.getRuleBuilder().withPriority(annotation.value());
      chain.proceed();
   }

}
