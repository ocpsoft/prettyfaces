package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.Field;

import javax.faces.event.PhaseId;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.FieldAnnotationHandler;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.Visitor;
import org.ocpsoft.rewrite.el.El;
import org.ocpsoft.rewrite.faces.config.PhaseBinding;
import org.ocpsoft.rewrite.param.Parameterized;

public class ParameterBindingHandler extends FieldAnnotationHandler<ParameterBinding>
{

   private final Logger log = Logger.getLogger(ParameterBindingHandler.class);

   @Override
   public Class<ParameterBinding> handles()
   {
      return ParameterBinding.class;
   }

   @Override
   public void process(FieldContext context, Field field, ParameterBinding annotation)
   {

      // default name is the name of the field
      String param = field.getName();

      // but the name specified in the annotation is preferred
      if (!annotation.value().isEmpty()) {
         param = annotation.value();
      }

      // add bindings to conditions by walking over the condition tree
      context.getRuleBuilder().accept(new AddBindingVisitor(context, param, field));

      if (log.isTraceEnabled()) {
         log.trace("Binding parameter [{}] to field [{}]", param, field);
      }

   }

   private final class AddBindingVisitor implements Visitor<Condition>
   {
      private final String param;
      private final FieldContext context;
      private final Field field;

      public AddBindingVisitor(FieldContext context, String paramName, Field field)
      {
         this.context = context;
         this.param = paramName;
         this.field = field;
      }

      @Override
      @SuppressWarnings("rawtypes")
      public void visit(Condition condition)
      {
         if (condition instanceof Parameterized) {
            Parameterized parameterized = (Parameterized) condition;

            // build an deferred EL binding
            El elBinding = El.property(field);
            PhaseBinding deferredBinding = PhaseBinding.to(elBinding).after(PhaseId.RESTORE_VIEW);

            // add the parameter and the binding
            parameterized.where(param).bindsTo(deferredBinding);

            // register the binding builder in the field context
            context.setBindingBuilder(elBinding);

         }
      }
   }

}
