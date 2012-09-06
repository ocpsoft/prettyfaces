package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.Method;

import javax.faces.context.FacesContext;

import org.ocpsoft.common.util.Assert;
import org.ocpsoft.prettyfaces.annotation.RequestAction;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.api.MethodContext;
import org.ocpsoft.rewrite.annotation.spi.MethodAnnotationHandler;
import org.ocpsoft.rewrite.config.Invoke;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.el.El;
import org.ocpsoft.rewrite.event.Rewrite;

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
   public void process(MethodContext context, RequestAction annotation, HandlerChain chain)
   {

      // create Operation for executing this method
      Method method = context.getJavaMethod();
      Operation rawOperation = Invoke.binding(El.retrievalMethod(method));

      // let subsequent handlers enrich the operation
      context.put(Operation.class, rawOperation);
      chain.proceed();
      Operation enrichedOperation = (Operation) context.get(Operation.class);
      Assert.notNull(enrichedOperation, "Operation was removed from context");

      // append this operation to the rule
      context.getRuleBuilder().perform(enrichedOperation);

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
