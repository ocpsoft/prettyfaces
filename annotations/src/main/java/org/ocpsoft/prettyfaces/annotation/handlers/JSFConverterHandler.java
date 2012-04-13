package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ocpsoft.logging.Logger;
import org.ocpsoft.prettyfaces.annotation.JSFConverter;
import org.ocpsoft.prettyfaces.core.util.NullComponent;
import org.ocpsoft.prettyfaces.core.util.NullLifecycle;
import org.ocpsoft.rewrite.annotation.api.ClassContext;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.bind.BindingBuilder;
import org.ocpsoft.rewrite.bind.Converter;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

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
         CustomFacesContextJSFRewriteConverter converter = new CustomFacesContextJSFRewriteConverter(converterId);
         bindingBuilder.convertedBy(converter);

         if (log.isTraceEnabled()) {
            log.trace("Attaching JSF converter [{}] to field [{}] of class [{}]", new Object[] {
                     converterId, field.getName(), field.getDeclaringClass().getName()
            });
         }

      }

   }

   /**
    * Implementation of {@link Converter} that uses the supplied JSF converter for conversion
    * 
    * @author Christian Kaltepoth
    */
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

         // obtain the JSF converter
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

   private static class CustomFacesContextJSFRewriteConverter<T> implements Converter<T>
   {

      private final String converterId;

      public CustomFacesContextJSFRewriteConverter(String converterId)
      {
         this.converterId = converterId;
      }

      @Override
      @SuppressWarnings("unchecked")
      public T convert(Rewrite event, EvaluationContext context, Object value)
      {

         // we will build the FacesContext ourself
         FacesContextFactory factory =
                  (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
         if (factory == null) {
            throw new IllegalArgumentException("Could not find FacesContextFactory");
         }

         // we need HttpServletRewrite to obtain the HttpServletRequest, HttpServletResponse, etc
         if (event instanceof HttpServletRewrite) {
            HttpServletRewrite httpRewrite = (HttpServletRewrite) event;

            // TODO: getting the ServletContext from the request works in Servlet 3.0 only!
            ServletContext servletContext = httpRewrite.getRequest().getServletContext();
            HttpServletRequest request = httpRewrite.getRequest();
            HttpServletResponse response = httpRewrite.getResponse();

            // we have to properly release the FacesContext
            FacesContext facesContext = null;
            try {

               // build the FacesContext using a fake Lifecycle instance
               facesContext = factory.getFacesContext(servletContext, request, response, new NullLifecycle());
               if (facesContext == null) {
                  throw new IllegalStateException("Could not create FacesContext");
               }

               // get the converter from the new FacesContext
               javax.faces.convert.Converter converter = facesContext.getApplication().createConverter(converterId);

               // run conversion
               String valueAsString = value != null ? value.toString() : null;
               try {
                  return (T) converter.getAsObject(facesContext, new NullComponent(), valueAsString);
               }
               catch (ConverterException e) {
                  return null;
               }

            }
            finally {
               if (facesContext != null) {
                  facesContext.release();
               }
            }

         }

         throw new IllegalStateException("Unsupported Rewrite event type: " + event.getClass().getName());

      }
   }

}
