package com.ocpsoft.pretty.faces.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * An implementation of {@link PrettyExpression} used when the name of the bean
 * should be resolved lazily. This class is used by the annotation configuration
 * mechanism.
 * 
 * @author Christian Kaltepoth
 */
public class LazyExpression implements PrettyExpression
{

   private final static Log log = LogFactory.getLog(LazyExpression.class);

   /**
    * LazyBeanNameFinder used to resolve the bean names
    */
   private final LazyBeanNameFinder finder;

   /**
    * the bean class
    */
   private final Class<?> beanClass;

   /**
    * The component of the bean the expression refers to. Can be a single
    * property name, a property path or method name.
    */
   private final String component;

   /**
    * Holds the expression once it has been build lazily
    */
   private String expression;

   /**
    * Creates a new {@link LazyExpression}
    * 
    * @param finder
    *            The bean name finder to user for lazy resolving
    * @param beanClass
    *            the class of the bean
    * @param component
    *            the component of the bean referenced by the expression. Can be
    *            a single property name, a property path or a method name
    */
   public LazyExpression(LazyBeanNameFinder finder, Class<?> beanClass, String component)
   {
      this.finder = finder;
      this.beanClass = beanClass;
      this.component = component;
   }

   /*
    * @see com.ocpsoft.pretty.faces.expression.PrettyExpression#getELExpression()
    */
   public String getELExpression()
   {

      // build the expression if not already done
      if (expression == null)
      {

         /*
          * Build the expression. Note that findBeanName() will either
          * return the resolved bean name or throw a runtime exception
          */
         expression = "#{" + finder.findBeanName(beanClass) + "." + component + "}";

         // log the resolved expression on trace level
         if (log.isTraceEnabled())
         {
            log.trace("Lazy expression resolved to: " + expression);
         }

      }
      return expression;
   }

   @Override
   public String toString()
   {
      return "#{[" + beanClass.getName() + "]." + component + "}";
   }

   /**
    * The class this lazy expression refers to.
    * 
    * @return The class of the bean
    */
   public Class<?> getBeanClass()
   {
      return beanClass;
   }

   /**
    * The component of the bean the expression refers to.
    * 
    * @return the component of the bean referenced by the expression. Can be a
    *         single property name, a property path or a method name
    */
   public String getComponent()
   {
      return component;
   }
   
}
