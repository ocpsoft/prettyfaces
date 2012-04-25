package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.RolesRequired;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.servlet.config.JAASRoles;

public class RolesRequiredHandler implements AnnotationHandler<RolesRequired>
{

   @Override
   public Class<RolesRequired> handles()
   {
      return RolesRequired.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, RolesRequired annotation)
   {
      Condition condition = JAASRoles.required(annotation.value());
      Condition conjunction = context.getRuleBuilder().getConditionBuilder().and(condition);
      context.getRuleBuilder().when(conjunction);
   }
}
