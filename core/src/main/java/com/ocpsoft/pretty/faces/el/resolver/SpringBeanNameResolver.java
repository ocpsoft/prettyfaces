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

import com.ocpsoft.logging.Logger;
import com.ocpsoft.pretty.faces.spi.ELBeanNameResolver;

/**
 * <p>
 * Implementation of {@link ELBeanNameResolver} that will resolve names of beans managed by Spring.
 * </p>
 * <p>
 * This resolver will get the WebApplicationContext from the {@link ServletContext} and then use the
 * getBeanNamesForType(beanClass) method to resolve bean names.
 * </p>
 * 
 * @author Christian Kaltepoth
 */
public class SpringBeanNameResolver implements ELBeanNameResolver
{

   private final static Logger log = Logger.getLogger(SpringBeanNameResolver.class);

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
    * Name of the static ScopedProxyUtils.getTargetBeanName() method
    */
   private final static String GET_TARGET_BEAN_NAME_METHOD = "getTargetBeanName";

   /**
    * The getBeanNamesForType() method
    */
   private Method getBeanNamesMethod;

   /**
    * The static ScopedProxyUtils.getTargetBeanName() method
    */
   private Method getTargetBeanNameMethod;

   /**
    * The Spring WebApplicationContext
    */
   private Object webAppContext;

   /*
    * Interface implementation
    */
   public boolean init(final ServletContext servletContext, final ClassLoader classLoader)
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

         // this is optional as the method is part of the Spring AOP module
         getTargetBeanNameMethod = getProxyTargetBeanNameMethod(classLoader);

         // success
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
   public String getBeanName(final Class<?> clazz)
   {

      // catch any reflection exceptions
      try
      {

         // ask for bean names for the supplied type
         String[] names = (String[]) getBeanNamesMethod.invoke(webAppContext, clazz);

         // no beans names returned?
         if ((names == null) || (names.length == 0))
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

   /**
    * Removes Spring internal bean names from the supplied list of names.
    * 
    * @param names The original list of names
    * @return the filtered list of names
    */
   private String[] filterProxyNames(final String[] names)
   {

      // Spring AOP hasn't been found!
      if (getTargetBeanNameMethod == null)
      {
         return names;
      }

      ArrayList<String> result = new ArrayList<String>();
      for (int i = names.length - 1; i >= 0; i--)
      {
         String name = names[i];
         if (name == null)
         {
            continue;
         }
         boolean isTargetBeanName = false;
         for (int j = 0; !isTargetBeanName && (j < names.length); j++)
         {

            // don't compare with itself
            if (j == i)
            {
               continue;
            }

            // we will catch all reflection exceptions
            try
            {

               // get the name Spring would use for the proxied bean
               String targetBeanName = (String) getTargetBeanNameMethod.invoke(null, names[j]);

               // the name is a Spring internal name for the proxied bean
               isTargetBeanName = name.equals(targetBeanName);

            }
            catch (IllegalArgumentException e)
            {
               log.warn(String.format("Internal error invoking %s", getTargetBeanNameMethod.getName()), e);
            }
            catch (IllegalAccessException e)
            {
               log.warn(String.format("Error invoking %s due to security restrictions",
                        getTargetBeanNameMethod.getName()));
            }
            catch (InvocationTargetException e)
            {
               log.warn(String.format("Method %s has thrown an exception:", getTargetBeanNameMethod.getName()),
                        e.getTargetException());
            }
         }

         // no internal name -> add to result
         if (!isTargetBeanName)
         {
            result.add(name);
         }

      }

      return result.toArray(new String[result.size()]);
   }

   /**
    * Tries to obtain the method ScopedProxyUtils.getTargetBeanName(). This may fail because the class is part of
    * Spring's AOP module.
    * 
    * @param classLoader The classloader to use for lookups
    * @return the target method or <code>null</code> if Spring AOP could not be found
    */
   private static Method getProxyTargetBeanNameMethod(final ClassLoader classLoader)
   {
      try
      {
         Class<?> scopedProxyUtilsClass = Class.forName(SCOPED_PROXY_UTILS_CLASS, true, classLoader);
         return scopedProxyUtilsClass.getMethod(GET_TARGET_BEAN_NAME_METHOD, String.class);
      }
      catch (ClassNotFoundException e)
      {
         // will happen when Spring AOP is not on the classpath
         if (log.isDebugEnabled())
         {
            log.debug(String.format("Could not find %s#%s method; filtering of proxy bean names has been disabled.",
                     SCOPED_PROXY_UTILS_CLASS, GET_TARGET_BEAN_NAME_METHOD));
         }
      }
      catch (SecurityException e)
      {
         // security issue
         log.warn(String.format("Unable to find method %s on class %s due to security restrictions.",
                  GET_TARGET_BEAN_NAME_METHOD, SCOPED_PROXY_UTILS_CLASS));
      }
      catch (NoSuchMethodException e)
      {
         // Spring is expected to offer this method
         log.warn(String.format("Cannot find method %s on class %s.", GET_TARGET_BEAN_NAME_METHOD,
                  SCOPED_PROXY_UTILS_CLASS));
      }
      return null;
   }
}
