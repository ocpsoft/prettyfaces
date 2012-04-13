package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.convert.ConverterException;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.JSFConverter;
import org.ocpsoft.prettyfaces.core.util.NullComponent;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.BindingBuilder;
import org.ocpsoft.rewrite.bind.Converter;
import org.ocpsoft.rewrite.bind.Validator;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;

public class JSFConverterHandler implements AnnotationHandler<JSFConverter>
{

   private final Logger log = Logger.getLogger(JSFConverterHandler.class);

   @Override
   public Class<JSFConverter> handles()
   {
      return JSFConverter.class;
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void process(ClassContext classContext, AnnotatedElement element, JSFConverter annotation)
   {

      // works only for fields and not for methods
      if (element instanceof Field && classContext instanceof FieldContext) {

         Field field = (Field) element;
         FieldContext context = (FieldContext) classContext;

         // locate the binding previously created by @ParameterBinding
         BindingBuilder bindingBuilder = context.getBindingBuilder();
         if (bindingBuilder == null) {
            throw new IllegalStateException("No binding found for field: " + field.getName());
         }

         // create the converter and attach it to the binding
         String converterId = annotation.converterId();
         JSFRewriteValidator converter = new JSFRewriteValidator(converterId);
         bindingBuilder.convertedBy(converter);

         if (log.isTraceEnabled()) {
            log.trace("Attaching JSF converter [{}] to field [{}] of class [{}]", new Object[] {
                     converterId, field.getName(), field.getDeclaringClass().getName()
            });
         }

      }

   }

   /**
    * Implementation of {@link Validator} that uses the supplied JSF validator for validation
    * 
    * @author Christian Kaltepoth
    */
   private static class JSFRewriteValidator<T> implements Converter<T>
   {

      private final String converterId;

      public JSFRewriteValidator(String validatorId)
      {
         this.converterId = validatorId;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T convert(Rewrite event, EvaluationContext context, Object value)
      {

         // we need to create the Application ourself using the ApplicationFactory
         ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
         if (factory == null) {
            throw new IllegalArgumentException("Could not find ApplicationFactory");
         }

         // try to obtain the Application instance from the factory
         Application application = factory.getApplication();
         if (application == null) {
            throw new IllegalArgumentException("Unable to create Application");
         }

         // obtain the JSF validator
         javax.faces.convert.Converter converter = application.createConverter(converterId);

         // run conversion
         String valueAsString = value != null ? value.toString() : null;
         try {
            return (T) converter.getAsObject(null, new NullComponent(), valueAsString);
         }
         catch (ConverterException e) {
            return null;
         }

      }

   }

}
