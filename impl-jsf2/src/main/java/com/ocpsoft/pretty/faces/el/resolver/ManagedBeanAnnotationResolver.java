package com.ocpsoft.pretty.faces.el.resolver;

import javax.faces.bean.ManagedBean;
import javax.servlet.ServletContext;

import com.ocpsoft.pretty.faces.spi.ELBeanNameResolver;

/**
 * <p>
 * Implementation of {@link ELBeanNameResolver} handling beans configured via the
 * JSF 2.0 ManagedBean annotation.
 * </p>
 * 
 * @author Christian Kaltepoth
 *
 */
public class ManagedBeanAnnotationResolver implements ELBeanNameResolver
{

   /*
    * @see com.ocpsoft.pretty.faces.el.BeanNameResolver#init(javax.servlet.ServletContext, java.lang.ClassLoader)
    */
   public boolean init(ServletContext servletContext, ClassLoader classLoader)
   {
      // nothing to do here
      return true;
   }

   /*
    * @see com.ocpsoft.pretty.faces.el.BeanNameResolver#getBeanName(java.lang.Class)
    */
   public String getBeanName(Class<?> clazz)
   {

      // Try to get ManagedBean annotation
      ManagedBean annotation = clazz.getAnnotation(ManagedBean.class);

      // No annotation? Abort!
      if (annotation == null)
      {
         return null;
      }

      // read name property
      String beanName = annotation.name();

      // we have found a valid value in the name attribute
      if (beanName != null && beanName.trim().length() > 0)
      {
         return beanName.trim();
      }
      // return default name as specified
      else
      {
         return deriveBeanName(clazz);
      }

   }

   /**
    * <p>
    * Creates a bean name from a class as described in the specification:
    * </p>
    * <p>
    * If the value of the name attribute is unspecified or is the empty String,
    * the managed-bean-name is derived from taking the unqualified class name
    * portion of the fully qualified class name and converting the first
    * character to lower case.
    * </p>
    * 
    * @param beanClass
    *           class to generate name for
    * @return the bean name
    */
   private String deriveBeanName(Class<?> beanClass)
   {
      String className = beanClass.getSimpleName();
      return Character.toLowerCase(className.charAt(0)) + className.substring(1);
   }

}
