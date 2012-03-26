package org.ocpsoft.prettyfaces.annotation.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ocpsoft.prettyfaces.annotation.spi.AnnotationHandler;

import com.ocpsoft.logging.Logger;
import com.ocpsoft.rewrite.config.Rule;

public class ClassVisitor
{

   private final Logger log = Logger.getLogger(ClassVisitor.class);

   /**
    * Maps annotation types to a list of handlers supporting the corresponding type
    */
   private final Map<Class<Annotation>, List<AnnotationHandler<Annotation>>> handlerMap = new HashMap<Class<Annotation>, List<AnnotationHandler<Annotation>>>();

   /**
    * The rules created by the visitor
    */
   private final List<Rule> rules = new ArrayList<Rule>();

   /**
    * The visitor must be initialized with the handlers to call for specific annotations
    */
   public ClassVisitor(List<AnnotationHandler<Annotation>> handlers)
   {
      for (AnnotationHandler<Annotation> handler : handlers) {

         // determine the annotation the handler can process
         Class<Annotation> annotationType = handler.handles();

         // register the handler in the handlers map
         List<AnnotationHandler<Annotation>> list = handlerMap.get(annotationType);
         if (list == null) {
            list = new ArrayList<AnnotationHandler<Annotation>>();
            handlerMap.put(annotationType, list);
         }
         list.add(handler);
      }

      if (log.isDebugEnabled()) {
         log.debug("Initialized to use {} AnnotationHandlers..", handlers.size());
      }

   }

   /**
    * Processes the annotation on the supplied class.
    */
   public void visit(Class<?> clazz)
   {

      if (log.isTraceEnabled()) {
         log.trace("Scanning class: {}", clazz.getName());
      }

      // create one builder for the class
      MappingBuilder builder = new MappingBuilderImpl();

      // first process fields
      for (Field field : clazz.getDeclaredFields()) {
         visit(field, builder);
      }

      // the methods
      for (Method method : clazz.getDeclaredMethods()) {
         visit(method, builder);
      }

      // the class itself is last
      visit(clazz, builder);

      // try to build a rule
      Rule rule = builder.toRule();

      // the rule may be null if there where no PrettyFaces annotations on the class
      if (rule != null) {

         if (log.isTraceEnabled()) {
            log.trace("One rule created from class: {}", clazz.getName());
         }

         rules.add(rule);

      }

   }

   /**
    * Process one {@link AnnotatedElement} of the class.
    */
   private void visit(AnnotatedElement element, MappingBuilder builder)
   {

      // each annotation on the element may be interesting for us
      for (Annotation annotation : element.getAnnotations()) {

         // type of this annotation
         Class<? extends Annotation> annotationType = annotation.annotationType();

         // determine the handlers to call for this type
         List<AnnotationHandler<Annotation>> handlers = handlerMap.get(annotationType);

         // process handlers if any
         if (handlers != null) {
            for (AnnotationHandler<Annotation> handler : handlers) {
               handler.process(annotation, element, builder);
            }
         }

      }
   }

   public List<Rule> getRules()
   {
      return rules;
   }

}
