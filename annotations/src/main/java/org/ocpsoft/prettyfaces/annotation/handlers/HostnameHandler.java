package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.Hostname;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.Domain;

public class HostnameHandler implements AnnotationHandler<Hostname>
{

   @Override
   public Class<Hostname> handles()
   {
      return Hostname.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, Hostname annotation)
   {
      context.getRuleBuilder().when(Domain.matches(annotation.value()));
   }
}
