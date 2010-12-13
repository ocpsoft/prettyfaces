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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.ServletContext;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class SeamBeanNameResolverTest
{

   @Test
   public void testNoSeamEnvironment() throws Exception
   {

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      // ClassLoader that always throws ClassNotFoundExceptions
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass((String) EasyMock.anyObject())).andThrow(new ClassNotFoundException())
            .anyTimes();
      EasyMock.replay(classLoader);

      // initialize resolver and verify that initialization failed
      SeamBeanNameResolver resolver = new SeamBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertFalse(initCompleted);

   }

   @Test
   public void testSeamUnresolveableClass() throws Exception
   {

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      // ClassLoader that knows the Seam class
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass("org.jboss.seam.Seam")).andReturn((Class) SeamMock.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      SeamBeanNameResolver resolver = new SeamBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: Bean not resolvable
      assertNull(resolver.getBeanName(SeamManagedBean.class));

   }

   @Test
   public void testSeamSuccessfulResolving() throws Exception
   {

      // setup SeamMock to answer with correct name
      SeamMock.setName("seamManagedBean");

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      // ClassLoader that knows the Seam class
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass("org.jboss.seam.Seam")).andReturn((Class) SeamMock.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      SeamBeanNameResolver resolver = new SeamBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: Successful resolving
      assertEquals("seamManagedBean", resolver.getBeanName(SeamManagedBean.class));

   }

   /**
    * Mock of the org.jboss.seam.Seam class
    */
   public static class SeamMock
   {

      /**
       * The name to answer with on calls to {@link #getComponentName(Class)}
       */
      private static String name = null;

      /**
       * Set the name to answer with on the next call to
       * {@link #getComponentName(Class)}
       * 
       * @param name
       *           name to answer with
       */
      public static void setName(String name)
      {
         SeamMock.name = name;
      }

      /**
       * The method called by the {@link SeamBeanNameResolver} class
       */
      public static String getComponentName(Class clazz)
      {
         return name;
      }
   }

   /**
    * Test bean
    */
   public static class SeamManagedBean
   {
      // nothing
   }

}
