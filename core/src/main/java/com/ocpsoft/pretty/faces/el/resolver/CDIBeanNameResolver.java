/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.faces.el.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.faces.spi.ELBeanNameResolver;

/**
 * <p>
 * A {@link ELBeanNameResolver} for CDI (JSR299) environments.
 * </p>
 * 
 * <p>
 * This resolver will try to get the BeanManager from the {@link ServletContext}
 * or directly from JNDI. If the BeanManager can be found, it will resolve names
 * by calling BeanManager.getBeans().
 * </p>
 * 
 * @author Christian Kaltepoth
 * 
 */
public class CDIBeanNameResolver implements ELBeanNameResolver
{

   private final static Log log = LogFactory.getLog(CDIBeanNameResolver.class);

   /**
    * Default JNDI name of the BeanManager
    */
   public final static String BEAN_MANAGER_JNDI = "java:comp/BeanManager";

   /**
    * Special JNDI name of the BeanManager for Apache Tomcat
    */
   public final static String BEAN_MANAGER_JNDI_TOMCAT = "java:comp/env/BeanManager";

   /**
    * FQCN of the BeanManager
    */
   public final static String BEAN_MANAGER_CLASS = "javax.enterprise.inject.spi.BeanManager";

   /**
    * BeanManager method to get beans by their type
    */
   public final static String GET_BEANS_METHOD = "getBeans";

   /**
    * FQCN of the Bean class
    */
   private final static String BEAN_CLASS = "javax.enterprise.inject.spi.Bean";

   /**
    * Name of the getName() on the Bean class
    */
   public final static String GET_NAME_METHOD = "getName";

   /**
    * Reference to get BeanManager.getBeans() method
    */
   private Method getBeansMethod = null;

   /**
    * Reference to the Bean.getName() method
    */
   private Method getNameMethod = null;

   /**
    * Reference to the BeanManager
    */
   private Object beanManager;

   /*
    * Implementation of method defined in the interface
    */
   public boolean init(ServletContext servletContext, ClassLoader classLoader)
   {

      // catch reflection exceptions
      try
      {

         // get BeanManager class and getBeans() method
         Class<?> beanManagerClass = classLoader.loadClass(BEAN_MANAGER_CLASS);
         getBeansMethod = beanManagerClass.getMethod(GET_BEANS_METHOD, Type.class, Annotation[].class);

         // get Bean class and getName() method
         Class<?> beanClass = classLoader.loadClass(BEAN_CLASS);
         getNameMethod = beanClass.getMethod(GET_NAME_METHOD);

      }
      catch (ClassNotFoundException e)
      {
         // happens in environments without CDI
         if (log.isDebugEnabled())
         {
            log.debug("BeanManager or Bean class not found. CDI resolver has been disabled.");
         }
         return false;
      }
      catch (NoSuchMethodException e)
      {
         // should never happen, because methods are defined in the CDI spec
         log.warn("Cannot find BeanManager.getBeans() or Bean.getName() method", e);
         return false;
      }
      catch (SecurityException e)
      {
         log.warn("Unable to init resolver due to security restrictions", e);
         return false;
      }

      // try to find in ServletContext first
      beanManager = getBeanManagerFromServletContext(servletContext);

      // try standard JNDI name
      if (beanManager == null)
      {
         beanManager = getBeanManagerFromJNDI(BEAN_MANAGER_JNDI);
      }

      // try special Tomcat JNDI name
      if (beanManager == null)
      {
         beanManager = getBeanManagerFromJNDI(BEAN_MANAGER_JNDI_TOMCAT);
      }
      
      // No BeanManager? Abort here
      if (beanManager == null)
      {
         // log this on debug level because the user may not use CDI at all
         if (log.isDebugEnabled())
         {
            log.debug("BeanManager cannot be found! CDI resolver gets disabled!");
         }
         return false;
      }

      if (log.isDebugEnabled())
      {
         log.debug("CDI environment detected. Enabling bean name resolving via BeanManager.");
      }
      return true;
   }

   /**
    * Tries to get the BeanManager from the servlet context attribute
    * <code>javax.enterprise.inject.spi.BeanManager</code>. This works with
    * Weld and the current OpenWebBeans snapshots.
    * 
    * @param servletContext The servlet context 
    * @return The BeanManager instance or <code>null</code>
    */
   private Object getBeanManagerFromServletContext(ServletContext servletContext)
   {

      // get BeanManager from servlet context
      Object obj = servletContext.getAttribute(BEAN_MANAGER_CLASS);

      // debug result
      if (log.isTraceEnabled())
      {
         if (obj == null)
         {
            log.trace("BeanManager not found in servlet context.");
         }
         else
         {
            log.trace("Found BeanManager in the servlet context.");
         }
      }

      return obj;

   }

   /**
    * Tries to get the BeanManager from JNDI
    * 
    * @param jndiName
    *           The JNDI name used for lookup
    * @return BeanManager instance or <code>null</code>
    */
   private Object getBeanManagerFromJNDI(String jndiName)
   {

      try
      {
         // perform lookup
         InitialContext initialContext = new InitialContext();
         Object obj = initialContext.lookup(jndiName);

         if (log.isTraceEnabled())
         {
            log.trace("Found BeanManager in: "+jndiName);
         }

         return obj;

      }
      catch (NamingException e)
      {
         if (log.isDebugEnabled())
         {
            log.debug("Unable to get BeanManager from '"+jndiName+"': " + e.getMessage());
         }
      }

      return null;
   }

   /*
    * Implementation of interface
    */
   public String getBeanName(Class<?> clazz)
   {
      // catch reflection exceptions
      try
      {

         // call BeanManager.getBeans(clazz) without suppling qualifiers
         Set<?> beansSet = (Set<?>) getBeansMethod.invoke(
               beanManager, clazz, Array.newInstance(Annotation.class, 0));

         // BeanManager returns no results
         if (beansSet == null || beansSet.size() == 0)
         {

            if (log.isTraceEnabled())
            {
               log.trace("BeanManager doesn't know  class: " + clazz.getName());
            }

            return null;
         }

         // more than one name? Warn the user..
         if (beansSet.size() > 1)
         {
            log.warn("The BeanManager returns more than one name for " + clazz.getName() + 
            ". You should place a @URLBeanName annotation on the class.");
            return null;
         }

         // get the bean
         Object bean = beansSet.iterator().next();

         // get name from bean instance
         String name = (String) getNameMethod.invoke(bean);

         // log the resolved name
         if (log.isTraceEnabled())
         {
            log.trace("BeanManager returned name " + name + " for class: " + clazz.getName());
         }

         return name;

      }
      catch (IllegalAccessException e)
      {
         // security issues
         log.warn("Unable to access BeanManager due to security restrictions", e);
      }
      catch (InvocationTargetException e)
      {
         // One of the methods we called has thrown an exception
         log.error("Failed to query BeanManager for the bean name...", e);
      }

      return null;
   }

}
