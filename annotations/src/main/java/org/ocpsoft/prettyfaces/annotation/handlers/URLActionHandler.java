package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.faces.context.FacesContext;

import org.ocpsoft.prettyfaces.annotation.URLAction;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.El;
import org.ocpsoft.rewrite.config.Invoke;
import org.ocpsoft.rewrite.config.Operation;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
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

         // wrap the operation if it shouldn't be executed on postbacks
         if (!annotation.onPostback()) {
            invocation = new IgnorePostbackOperation(invocation);
         }

         // the action invocation must be deferred to get executed inside the JSF lifecycle
         Operation deferredOperation = PhaseAction.enqueue(invocation).after(annotation.phaseId().getPhaseId());

         // append this operation to the rule
         Operation composite = context.getRuleBuilder().getOperationBuilder().and(deferredOperation);
         context.getRuleBuilder().perform(composite);

      }

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
