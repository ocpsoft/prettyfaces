package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.ocpsoft.prettyfaces.annotation.RolesRequired;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

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
      Condition condition = new JAASRolesCondition(annotation.value());
      Condition conjunction = context.getRuleBuilder().getConditionBuilder().and(condition);
      context.getRuleBuilder().when(conjunction);
   }

   /**
    * Implementation of {@link Condition} that checks the subject's roles using
    * {@link HttpServletRequest#isUserInRole(String)}
    * 
    * @author Christian Kaltepoth
    */
   private static class JAASRolesCondition implements Condition
   {

      private final Collection<String> roles;

      public JAASRolesCondition(String[] roles)
      {
         this.roles = Arrays.asList(roles);
      }

      @Override
      public boolean evaluate(Rewrite event, EvaluationContext context)
      {
         if (event instanceof HttpServletRewrite) {
            HttpServletRewrite rewrite = (HttpServletRewrite) event;

            // check if user has all required roles
            for (String role : roles) {
               if (!rewrite.getRequest().isUserInRole(role)) {
                  return false;
               }
            }

            return true;

         }
         throw new IllegalStateException("Only HttpServletRewrite is supported!");
      }

   }

}
