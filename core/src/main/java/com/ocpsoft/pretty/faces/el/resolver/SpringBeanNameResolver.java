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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.faces.spi.ELBeanNameResolver;

/**
 * <p>
 * Implementation of {@link ELBeanNameResolver} that will resolve names of beans
 * managed by Spring.
 * </p>
 * <p>
 * This resolver will get the WebApplicationContext from the
 * {@link ServletContext} and then use the getBeanNamesForType(beanClass) method
 * to resolve bean names.
 * </p>
 * 
 * @author Christian Kaltepoth
 */
public class SpringBeanNameResolver implements ELBeanNameResolver
{

   private final static Log log = LogFactory.getLog(SpringBeanNameResolver.class);

   /**
    * FQCN of the WebApplicationContext class
    */
   private final static String WEB_APP_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";

   /**
    * Name of the getBeanNamesForType() class
    */
   private final static String GET_BEAN_NAMES_METHOD = "getBeanNamesForType";

   /**
    * FQCN of the ScopedProxyUtils class
    */
   private final static String SCOPED_PROXY_UTILS_CLASS = "org.springframework.aop.scope.ScopedProxyUtils";

   /**
    * Name of the static getTargetBeanName() method
    */
   private final static String GET_TARGET_BEAN_NAME_METHOD = "getTargetBeanName";

   /**
    * The getBeanNamesForType() method
    */
   private Method getBeanNamesMethod;

   /**
    * The static getTargetBeanName() method
    */
   private Method getTargetBeanNameMethod;

   /**
    * The Spring WebApplicationContext
    */
   private Object webAppContext;

   /*
    * Interface implementation
    */
   public boolean init(ServletContext servletContext, ClassLoader classLoader)
   {

      // try to get WebApplicationContext from ServletContext
      webAppContext = servletContext.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");

      // Not found? Disable resolver!
      if (webAppContext == null)
      {
         if (log.isDebugEnabled())
         {
            log.debug("WebApplicationContext not found in ServletContext. Resolver has been disabled.");
         }
         return false;
      }

      // catch reflection failures
      try
      {
         // get findBeanNamesByType method
         Class<?> webAppContextClass = classLoader.loadClass(WEB_APP_CONTEXT_CLASS);
         getBeanNamesMethod = webAppContextClass.getMethod(GET_BEAN_NAMES_METHOD, Class.class);

         // init successful
         if (log.isDebugEnabled())
         {
            log.debug("Spring detected. Enabling Spring bean name resolving.");
         }
         getTargetBeanNameMethod = getProxyTargetBeanNameMethod(classLoader);
         return true;

      }
      catch (ClassNotFoundException e)
      {
         // will happen when Spring is not on the classpath
         if (log.isDebugEnabled())
         {
            log.debug("WebApplicationContext class could not be found. Resolver has been disabled.");
         }
      }
      catch (NoSuchMethodException e)
      {
         // Spring is expected to offer this methods
         log.warn("Cannot find getBeanNamesByType() method.", e);
      }
      catch (SecurityException e)
      {
         // security issues
         log.warn("Unable to init resolver due to security restrictions", e);
      }

      // resolver will be disabled
      return false;

   }

   /*
    * Interface implementation
    */
   public String getBeanName(Class<?> clazz)
   {

      // catch any reflection exceptions
      try
      {

         // ask for bean names for the supplied type
         String[] names = (String[]) getBeanNamesMethod.invoke(webAppContext, clazz);

         // no beans names returned?
         if (names == null || names.length == 0)
         {

            if (log.isTraceEnabled())
            {
               log.trace("Spring doesn't know a name for class: " + clazz.getName());
            }

            return null;
         }

         // filter out scoped proxies, which are really Spring-internal names for beans we want:
         names = filterProxyNames(names);

         // more than one name? Warn the user..
         if (names.length > 1)
         {

            log.warn("Spring returns more than one name for " + clazz.getName()
                  + ". You should place a @URLBeanName annotation on the class.");
            return null;

         }

         // we found the name!!!
         if (log.isTraceEnabled())
         {
            log.trace("Spring returned the name " + names[0] + " for class: " + clazz.getName());
         }

         // return the resolved name
         return names[0];

      }
      catch (IllegalAccessException e)
      {
         // security issues
         log.warn("Unable to call Spring due to security restrictions", e);
      }
      catch (InvocationTargetException e)
      {
         // One of the methods we called has thrown an exception
         log.error("Failed to query Spring for the bean name...", e);
      }

      // we don't know
      return null;
   }

   private String[] filterProxyNames(String[] names)
   {
      if (getTargetBeanNameMethod == null)
      {
         return names;
      }
      ArrayList<String> result = new ArrayList<String>(names.length);
      for (int i = names.length - 1; i >= 0; i--)
      {
         String name = names[i];
         if (name == null)
         {
            continue;
         }
         boolean isProxy = false;
         for (int j = 0; !isProxy && j < names.length; j++)
         {
            if (j == i)
            {
               continue;
            }
            try
            {
               isProxy = name.equals(getTargetBeanNameMethod.invoke(null, names[j]));
            }
            catch (Exception e)
            {
               if (log.isDebugEnabled())
               {
                  log.debug(String.format("Error invoking %s(%s)", getTargetBeanNameMethod, name));
               }
            }
         }
         if (!isProxy)
         {
            result.add(name);
         }
      }
      
      return result.toArray(new String[result.size()]);
   }

   private static Method getProxyTargetBeanNameMethod(ClassLoader classLoader)
   {
      try {
         Class<?> scopedProxyUtilsClass = Class.forName(SCOPED_PROXY_UTILS_CLASS, true, classLoader);
         return scopedProxyUtilsClass.getMethod(GET_TARGET_BEAN_NAME_METHOD, String.class);
      }
      catch (Exception e)
      {
         // will happen when Spring AOP is not on the classpath, or similar
         if (log.isDebugEnabled())
         {
            log.debug(String.format("Could not find %s#%s method; filtering of proxy bean names has been disabled.",
                  SCOPED_PROXY_UTILS_CLASS, GET_TARGET_BEAN_NAME_METHOD));
         }
         return null;
      }
   }
}
