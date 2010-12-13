package com.ocpsoft.pretty.faces.el.resolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.faces.spi.ELBeanNameResolver;

/**
 * <p>
 * Implementation of {@link ELBeanNameResolver} that resolves Seam components.
 * </p>
 * 
 * <p>
 * This class uses reflection to call:
 * </p>
 * 
 * <pre>
 * Seam.getComponentName(beanClass)
 * </pre>
 * 
 * @author Christian Kaltepoth
 * 
 */
public class SeamBeanNameResolver implements ELBeanNameResolver
{
   private final static Log log = LogFactory.getLog(SeamBeanNameResolver.class);

   /**
    * FQCN of the Seam class
    */
   private final static String SEAM_CLASS = "org.jboss.seam.Seam";

   /**
    * Name of the getComponentName method
    */
   private final static String GET_COMPONENT_NAME_METHOD = "getComponentName";

   /**
    * Reference to the getComponentNameMethod
    */
   private Method getComponentNameMethod = null;

   /*
    * Interface implementation
    */
   public boolean init(ServletContext servletContext, ClassLoader classLoader)
   {

      try
      {
         // get Seam class
         Class<?> seamClass = classLoader.loadClass(SEAM_CLASS);

         // get getComponentName method
         getComponentNameMethod = seamClass.getMethod(GET_COMPONENT_NAME_METHOD, Class.class);

         // initialization completed
         if (log.isDebugEnabled())
         {
            log.debug("Seam environment detected. Enabling bean name resolving via Seam.");
         }
         return true;

      }
      catch (ClassNotFoundException e)
      {
         // Will happen in enviroments without Seam
         if (log.isDebugEnabled())
         {
            log.debug("Seam class has not been found. Seam resolver will be disabled.");
         }
      }
      catch (NoSuchMethodException e)
      {
         // This method is expected on the Seam class
         log.warn("Cannot find method getComponentName() on Seam class.", e);
      }
      catch (SecurityException e)
      {
         log.warn("Unable to init resolver due to security restrictions", e);
      }

      // disable resolver
      return false;

   }

   /*
    * Interface implementation
    */
   public String getBeanName(Class<?> clazz)
   {

      // catch reflection exceptions
      try
      {

         // invoke getComponentName method
         String result = (String) getComponentNameMethod.invoke(null, clazz);

         // log name if call was successful
         if (log.isTraceEnabled() && result != null)
         {
            log.trace("Seam returned name '" + result + "' for class: " + clazz.getName());
         }

         // return result
         return result;

      }
      catch (IllegalAccessException e)
      {
         // security issues
         log.warn("Unable to call Seam.getComponentName() due to security restrictions", e);
      }
      catch (InvocationTargetException e)
      {
         // Seam's getComponentName() has thrown an exception
         log.error("Failed to invoke Seam.getComponentName()", e);
      }

      // we don't know the name
      return null;

   }

}
