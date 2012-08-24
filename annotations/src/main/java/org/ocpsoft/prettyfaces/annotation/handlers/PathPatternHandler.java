package org.ocpsoft.prettyfaces.annotation.handlers;

import org.ocpsoft.prettyfaces.annotation.PathPattern;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.Path;

public class PathPatternHandler implements AnnotationHandler<PathPattern>
{

   @Override
   public Class<PathPattern> handles()
   {
      return PathPattern.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, PathPattern annotation, HandlerChain chain)
   {
      context.getRuleBuilder().when(Path.matches(annotation.value()));
      chain.proceed(context);
   }

}
