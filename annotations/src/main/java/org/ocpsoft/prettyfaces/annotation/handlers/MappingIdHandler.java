package org.ocpsoft.prettyfaces.annotation.handlers;

import org.ocpsoft.prettyfaces.annotation.MappingId;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;

public class MappingIdHandler implements AnnotationHandler<MappingId>
{

   @Override
   public Class<MappingId> handles()
   {
      return MappingId.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, MappingId annotation, HandlerChain chain)
   {
      context.getRuleBuilder().withId(annotation.value());
      chain.proceed();
   }

}
