package org.ocpsoft.prettyfaces.annotation.handlers;

import org.ocpsoft.prettyfaces.annotation.RolesRequired;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.servlet.config.JAASRoles;

public class RolesRequiredHandler implements AnnotationHandler<RolesRequired>
{

   @Override
   public Class<RolesRequired> handles()
   {
      return RolesRequired.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, RolesRequired annotation, HandlerChain chain)
   {
      context.getRuleBuilder().when(JAASRoles.required(annotation.value()));
      chain.proceed(context);
   }
}
