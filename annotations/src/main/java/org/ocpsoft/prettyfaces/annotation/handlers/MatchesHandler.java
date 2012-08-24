package org.ocpsoft.prettyfaces.annotation.handlers;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.Matches;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.FieldAnnotationHandler;
import org.ocpsoft.rewrite.param.Parameter;
import org.ocpsoft.rewrite.param.RegexConstraint;

/**
 * @author Christian Kaltepoth
 */
public class MatchesHandler extends FieldAnnotationHandler<Matches>
{
   private final Logger log = Logger.getLogger(MatchesHandler.class);

   @Override
   public Class<Matches> handles()
   {
      return Matches.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void process(FieldContext context, Matches annotation, HandlerChain chain)
   {

      // obtain the parameter for the current field
      Parameter parameter = (Parameter) context.get(Parameter.class);
      if (parameter == null) {
         throw new IllegalStateException("Cound not find any binding for field: " +
                  context.getJavaField().getName());
      }

      // add a corresponding RegexConstraint
      String expr = annotation.value();
      parameter.constrainedBy(new RegexConstraint(expr));

      if (log.isTraceEnabled()) {
         log.trace("Parameter [{}] has been constrained by [{}]", parameter.getName(), expr);
      }

      chain.proceed(context);

   }

}
