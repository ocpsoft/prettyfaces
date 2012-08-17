package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.Method;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.ocpsoft.prettyfaces.annotation.AfterPhase;
import org.ocpsoft.prettyfaces.annotation.BeforePhase;
import org.ocpsoft.prettyfaces.annotation.RequestAction;
import org.ocpsoft.rewrite.annotation.api.MethodContext;
import org.ocpsoft.rewrite.annotation.spi.MethodAnnotationHandler;
import org.ocpsoft.rewrite.config.Invoke;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.el.El;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.faces.config.PhaseAction;
import org.ocpsoft.rewrite.faces.config.PhaseOperation;

public class RequestActionHandler extends MethodAnnotationHandler<RequestAction>
{

   @Override
   public Class<RequestAction> handles()
   {
      return RequestAction.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_STRUCTURAL;
   }

   @Override
   public void process(MethodContext context, Method method, RequestAction annotation)
   {

      // create Operation for executing this method
      Operation invocation = Invoke.binding(El.retrievalMethod(method));

      // wrap the operation if it shouldn't be executed on postbacks
      if (!annotation.onPostback()) {
         invocation = new IgnorePostbackOperation(invocation);
      }

      // the action invocation must be deferred to get executed inside the JSF lifecycle
      PhaseOperation<?> deferredOperation = PhaseAction.enqueue(invocation);

      // queue the operation for a specific time in the JSF lifecycle
      BeforePhase beforePhase = method.getAnnotation(BeforePhase.class);
      AfterPhase afterPhase = method.getAnnotation(AfterPhase.class);
      if (beforePhase == null && afterPhase == null) {
         deferredOperation.after(PhaseId.RESTORE_VIEW);
      }
      else if (beforePhase == null && afterPhase != null) {
         deferredOperation.after(afterPhase.value().getPhaseId());
      }
      else if (beforePhase != null && afterPhase == null) {
         deferredOperation.before(beforePhase.value().getPhaseId());
      }
      else {
         throw new IllegalStateException("Error processing @" + handles().getSimpleName() + " annotation on method "
                  + method.getDeclaringClass().getName() + "#" + method.getName()
                  + ": You cannot use @" + BeforePhase.class.getSimpleName() + " and @"
                  + AfterPhase.class.getSimpleName() + " at the same time.");
      }

      // append this operation to the rule
      context.getRuleBuilder().perform(deferredOperation);

   }

   /**
    * This operation wraps another operation and delegates events only if the current request is NOT a JSF postback.
    * 
    * @author Christian Kaltepoth
    */
   private final class IgnorePostbackOperation implements Operation
   {

      private final Operation delegate;

      public IgnorePostbackOperation(Operation delegate)
      {
         this.delegate = delegate;
      }

      @Override
      public void perform(Rewrite event, EvaluationContext context)
      {

         // get the FacesContext which is required to determine whether this is a postback
         FacesContext facesContext = FacesContext.getCurrentInstance();
         if (facesContext == null) {
            throw new IllegalStateException("FacesContext.getCurrentInstance() returned null");
         }

         // use StateManager to obtain postback state
         boolean postback = facesContext.getRenderKit().getResponseStateManager().isPostback(facesContext);

         // skip delegation if this is a postback
         if (!postback) {
            delegate.perform(event, context);
         }

      }

   }

}
