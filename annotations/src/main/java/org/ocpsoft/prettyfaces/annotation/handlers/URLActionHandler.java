package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.ocpsoft.prettyfaces.annotation.URLAction;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.El;
import org.ocpsoft.rewrite.config.Invoke;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.faces.config.PhaseAction;

public class URLActionHandler implements AnnotationHandler<URLAction>
{

   @Override
   public Class<URLAction> handles()
   {
      return URLAction.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, URLAction annotation)
   {

      if (element instanceof Method) {
         Method method = (Method) element;

         // FIXME: dirty way to build the EL expression
         String simpleClassName = method.getDeclaringClass().getSimpleName();
         String beanName = String.valueOf(simpleClassName.charAt(0)).toLowerCase()
                  + simpleClassName.substring(1);
         String expression = "#{" + beanName + "." + method.getName() + "}";

         // create Operation for executing this method
         Operation invocation = Invoke.binding(El.retrievalMethod(expression));
         Operation deferredInvocation = PhaseAction.enqueue(invocation).after(annotation.phaseId().getPhaseId());

         // append this operation to the rule
         Operation composite = context.getRuleBuilder().getOperationBuilder().and(deferredInvocation);
         context.getRuleBuilder().perform(composite);

      }

   }

}
