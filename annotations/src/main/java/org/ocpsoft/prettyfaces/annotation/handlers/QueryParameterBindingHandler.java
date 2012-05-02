package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.PhaseId;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.QueryParameterBinding;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.FieldAnnotationHandler;
import org.ocpsoft.rewrite.bind.Bindable;
import org.ocpsoft.rewrite.bind.Binding;
import org.ocpsoft.rewrite.bind.Bindings;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.el.El;
import org.ocpsoft.rewrite.faces.config.PhaseBinding;
import org.ocpsoft.rewrite.servlet.config.HttpCondition;
import org.ocpsoft.rewrite.servlet.http.event.HttpInboundServletRewrite;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

public class QueryParameterBindingHandler extends FieldAnnotationHandler<QueryParameterBinding>
{

   private final Logger log = Logger.getLogger(QueryParameterBindingHandler.class);

   @Override
   public Class<QueryParameterBinding> handles()
   {
      return QueryParameterBinding.class;
   }

   @Override
   public void process(FieldContext context, Field field, QueryParameterBinding annotation)
   {

      // default name is the name of the field
      String queryParam = field.getName();

      // but the name specified in the annotation is preferred
      if (!annotation.value().isEmpty()) {
         queryParam = annotation.value();
      }

      // add the binding condition
      QueryParameterBindingCondition bindingCondition = new QueryParameterBindingCondition(queryParam);
      Condition conjunction = context.getRuleBuilder().getConditionBuilder().and(bindingCondition);
      context.getRuleBuilder().when(conjunction);

      // build an deferred EL binding
      El elBinding = El.property(field);
      PhaseBinding deferredBinding = PhaseBinding.to(elBinding).after(PhaseId.RESTORE_VIEW);
      bindingCondition.bindsTo(deferredBinding);

      // register the binding builder in the field context
      context.setBindingBuilder(elBinding);

      if (log.isTraceEnabled()) {
         log.trace("Binding query parameter [{}] to to field [{}]", queryParam, field);
      }

   }

   private static class QueryParameterBindingCondition extends HttpCondition implements
            Bindable<QueryParameterBindingCondition>
   {

      private final String name;
      private final List<Binding> bindings = new ArrayList<Binding>();

      public QueryParameterBindingCondition(String name)
      {
         this.name = name;
      }

      @Override
      public boolean evaluateHttp(HttpServletRewrite event, EvaluationContext context)
      {
         if (event instanceof HttpInboundServletRewrite) {
            String[] values = ((HttpInboundServletRewrite) event).getRequest().getParameterValues(name);
            Bindings.enqueueSubmission(event, context, this, values);
         }
         return true;
      }

      @Override
      public QueryParameterBindingCondition bindsTo(Binding binding)
      {
         this.bindings.add(binding);
         return null;
      }

      @Override
      public List<Binding> getBindings()
      {
         return Collections.unmodifiableList(bindings);
      }

   }

}
