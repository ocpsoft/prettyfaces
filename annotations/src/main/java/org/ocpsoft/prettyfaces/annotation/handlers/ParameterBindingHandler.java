package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.faces.event.PhaseId;

import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.Binding;
import org.ocpsoft.rewrite.bind.El;
import org.ocpsoft.rewrite.bind.Submission;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.Visitor;
import org.ocpsoft.rewrite.faces.config.PhaseBinding;
import org.ocpsoft.rewrite.param.Parameterized;

public class ParameterBindingHandler implements AnnotationHandler<ParameterBinding>
{

   @Override
   public Class<ParameterBinding> handles()
   {
      return ParameterBinding.class;
   }

   @Override
   public void process(ClassContext context, AnnotatedElement element, ParameterBinding annotation)
   {

      if (element instanceof Field) {
         Field field = (Field) element;

         // default name is the name of the field
         String param = field.getName();

         // but the name specified in the annotation is preferred
         if (!annotation.value().isEmpty()) {
            param = annotation.value();
         }

         // FIXME: dirty way to build the EL expression
         String simpleClassName = field.getDeclaringClass().getSimpleName();
         String beanName = String.valueOf(simpleClassName.charAt(0)).toLowerCase()
                  + simpleClassName.substring(1);
         String expression = "#{" + beanName + "." + field.getName() + "}";

         // add bindings to conditions by walking over the condition tree
         context.getRuleBuilder().accept(new AddBindingVisitor(param, expression));

      }

   }

   private final class AddBindingVisitor implements Visitor<Condition>
   {
      private final String param;
      private final String expression;

      public AddBindingVisitor(String paramName, String expression)
      {
         this.param = paramName;
         this.expression = expression;
      }

      @Override
      @SuppressWarnings("rawtypes")
      public void visit(Condition condition)
      {
         if (condition instanceof Parameterized) {
            Parameterized parameterized = (Parameterized) condition;
            El binding = El.property(expression);
            Binding deferredBinding = PhaseBinding.to(binding).after(PhaseId.RESTORE_VIEW);
            parameterized.where(param, deferredBinding);
         }
      }
   }

}
