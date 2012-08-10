package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.URLPattern;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.Path;

public class URLPatternHandler implements AnnotationHandler<URLPattern>
{

   @Override
   public Class<URLPattern> handles()
   {
      return URLPattern.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, URLPattern annotation)
   {
      context.getRuleBuilder().when(Path.matches(annotation.value()));
   }

}
