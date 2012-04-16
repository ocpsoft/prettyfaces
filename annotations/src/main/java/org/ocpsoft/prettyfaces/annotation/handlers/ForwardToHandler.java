package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;

import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.config.OperationBuilder;
import org.ocpsoft.rewrite.config.RuleBuilder;
import org.ocpsoft.rewrite.servlet.config.Forward;

public class ForwardToHandler implements AnnotationHandler<ForwardTo>
{

   @Override
   public Class<ForwardTo> handles()
   {
      return ForwardTo.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, ForwardTo annotation)
   {

      RuleBuilder ruleBuilder = context.getRuleBuilder();

      // add new operation to the rule
      OperationBuilder operation = ruleBuilder.getOperationBuilder().and(Forward.to(annotation.value()));
      ruleBuilder.perform(operation);
   }

}
