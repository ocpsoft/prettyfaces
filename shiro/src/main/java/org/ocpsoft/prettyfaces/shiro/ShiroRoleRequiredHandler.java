package org.ocpsoft.prettyfaces.shiro;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.ocpsoft.prettyfaces.annotation.handlers.HandlerConstants;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;

public class ShiroRoleRequiredHandler implements AnnotationHandler<ShiroRoleRequired>
{

   @Override
   public Class<ShiroRoleRequired> handles()
   {
      return ShiroRoleRequired.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, ShiroRoleRequired annotation)
   {

      Condition roleCondition = new ShiroRoleCondition(annotation.value());
      Condition conjunction = context.getRuleBuilder().getConditionBuilder().and(roleCondition);
      context.getRuleBuilder().when(conjunction);
   }

   /**
    * Implementation of {@link Condition} that checks the subject's roles
    * 
    * @author Christian Kaltepoth
    */
   private static class ShiroRoleCondition implements Condition
   {

      private final Collection<String> roles;

      public ShiroRoleCondition(String[] roles)
      {
         this.roles = Arrays.asList(roles);
      }

      @Override
      public boolean evaluate(Rewrite event, EvaluationContext context)
      {
         return SecurityUtils.getSubject().hasAllRoles(roles);
      }

   }

}
