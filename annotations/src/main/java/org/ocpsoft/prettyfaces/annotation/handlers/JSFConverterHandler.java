package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.Field;

import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.JSFConverter;
import org.ocpsoft.prettyfaces.core.util.NullComponent;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.FieldAnnotationHandler;
import org.ocpsoft.rewrite.bind.BindingBuilder;
import org.ocpsoft.rewrite.bind.Converter;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;

/**
 * @author Christian Kaltepoth
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class JSFConverterHandler extends FieldAnnotationHandler<JSFConverter>
{
   private final Logger log = Logger.getLogger(JSFConverterHandler.class);

   @Override
   public Class<JSFConverter> handles()
   {
      return JSFConverter.class;
   }

   @Override
   public int priority()
   {
      return HandlerConstants.WEIGHT_TYPE_ENRICHING;
   }

   @Override
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public void process(FieldContext context, JSFConverter annotation, HandlerChain chain)
   {
      Field field = context.getJavaField();

      // locate the binding previously created by @ParameterBinding
      BindingBuilder bindingBuilder = (BindingBuilder) context.get(BindingBuilder.class);
      if (bindingBuilder == null) {
         throw new IllegalStateException("No binding found for field: " + field.getName());
      }

      String converterId = annotation.converterId();
      JSFRewriteConverter converter = new JSFRewriteConverter(converterId);
      bindingBuilder.convertedBy(converter);

      if (log.isTraceEnabled()) {
         log.trace("Attaching JSF converter [{}] to field [{}] of class [{}]", new Object[] {
                  converterId, field.getName(), field.getDeclaringClass().getName()
         });
      }
      
      chain.proceed(context);
      
   }

   private static class JSFRewriteConverter<T> implements Converter<T>
   {
      private final String converterId;

      public JSFRewriteConverter(String converterId)
      {
         this.converterId = converterId;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T convert(Rewrite event, EvaluationContext context, Object value)
      {
         FacesContext facesContext = FacesContext.getCurrentInstance();
         if (facesContext == null) {
            throw new IllegalStateException("Could not get FacesContext");
         }

         javax.faces.convert.Converter converter = facesContext.getApplication().createConverter(converterId);

         String valueAsString = value != null ? value.toString() : null;
         try {
            return (T) converter.getAsObject(facesContext, new NullComponent(), valueAsString);
         }
         catch (ConverterException e) {
            return null;
         }
      }
   }
}
