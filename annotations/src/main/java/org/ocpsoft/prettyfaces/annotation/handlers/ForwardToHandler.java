package org.ocpsoft.prettyfaces.annotation.handlers;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.Forward;

public class ForwardToHandler implements AnnotationHandler<ForwardTo>
{

   @Override
   public Class<ForwardTo> handles()
   {
      return ForwardTo.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_STRUCTURAL;
   }

   @Override
   public void process(ClassContext context, ForwardTo annotation, HandlerChain chain)
   {
      context.getRuleBuilder().perform(Forward.to(annotation.value()));
      chain.proceed(context);
   }
}
