package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.ocpsoft.prettyfaces.annotation.AfterPhase;
import org.ocpsoft.prettyfaces.annotation.BeforePhase;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.api.MethodContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.faces.config.PhaseAction;
import org.ocpsoft.rewrite.faces.config.PhaseOperation;

public abstract class PhaseHandlerBase<A extends Annotation> implements AnnotationHandler<A>
{

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   public void process(ClassContext context, A annotation, HandlerChain chain)
   {

      if (context instanceof MethodContext) {
         handleMethodAnnotation((MethodContext) context, annotation);
      }

      chain.proceed();
   }

   protected void handleMethodAnnotation(MethodContext context, A annotation)
   {

      Method method = ((MethodContext) context).getJavaMethod();

      // check if the forbidden annotation is present
      if (method.getAnnotation(getForbiddenAnnotation()) != null) {
         throw new IllegalStateException("Error processing method "
                  + method.getDeclaringClass().getName() + "#" + method.getName()
                  + ": You cannot use @" + BeforePhase.class.getSimpleName() + " and @"
                  + AfterPhase.class.getSimpleName() + " at the same time.");
      }

      Operation operation = (Operation) context.get(Operation.class);
      if (operation == null) {
         throw new IllegalStateException("Cannot find operation to wrap");
      }

      PhaseOperation<?> deferredOperation = PhaseAction.enqueue(operation);

      applyPhaseRestriction(deferredOperation, annotation);

      context.put(Operation.class, deferredOperation);

   }

   protected abstract void applyPhaseRestriction(PhaseOperation<?> operation, A annotation);

   protected abstract Class<? extends Annotation> getForbiddenAnnotation();

}
