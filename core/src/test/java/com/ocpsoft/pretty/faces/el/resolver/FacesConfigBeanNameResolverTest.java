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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

import org.easymock.classextension.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

public class FacesConfigBeanNameResolverTest
{
   
   // faces-config.xml for testing
   private static URL testXmlFile;

   @BeforeClass
   public static void initTest()
   {

      // Load test file
      testXmlFile = Thread.currentThread().getContextClassLoader().getResource("faces-config-resolver-test.xml");
      assertNotNull("Unable to find faces-config.xml for unit test", testXmlFile);

   }

   @Test
   public void testFacesConfigResolverInitialization() throws Exception
   {

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.replay(servletContext);

      // ClassLoader that always returns an empty enumeration on calls to getResources()
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.getResources((String) EasyMock.anyObject())).andReturn(new Vector<URL>().elements()).anyTimes();
      EasyMock.replay(classLoader);

      // initialize resolver and verify successful initialization
      FacesConfigBeanNameResolver resolver = new FacesConfigBeanNameResolver();
      boolean completed = resolver.init(servletContext, classLoader);
      assertTrue(completed);

      // the resolver will fail for an unknown bean
      assertNull(resolver.getBeanName(FacesConfigResolverManagedBean.class));

   }

   @Test
   public void testFacesConfigResolverWithWebInfConfig() throws Exception
   {

      // Simple mock of ServletContext returning our test file
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getResource("/WEB-INF/faces-config.xml")).andReturn(testXmlFile).once();
      EasyMock.replay(servletContext);

      // ClassLoader that always returns an empty enumeration on calls to getResources()
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.getResources((String) EasyMock.anyObject())).andReturn(new Vector<URL>().elements()).anyTimes();
      EasyMock.replay(classLoader);

      // initialize resolver and verify successful initialization
      FacesConfigBeanNameResolver resolver = new FacesConfigBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // validate resolving of the managed bean specified in the XML file
      assertEquals("myManagedBean", resolver.getBeanName(FacesConfigResolverManagedBean.class));

   }

   @Test
   public void testFacesConfigResolverWithCustomConfig() throws Exception
   {

      // Simple mock of ServletContext returning our test file
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getInitParameter(FacesServlet.CONFIG_FILES_ATTR)).andReturn("/WEB-INF/my-custom-config.xml").once();
      EasyMock.expect(servletContext.getResource("/WEB-INF/my-custom-config.xml")).andReturn(testXmlFile).once();
      EasyMock.replay(servletContext);

      // ClassLoader that always returns an empty enumeration on calls to getResources()
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.getResources((String) EasyMock.anyObject())).andReturn(new Vector<URL>().elements()).anyTimes();
      EasyMock.replay(classLoader);

      // initialize resolver and verify successful initialization
      FacesConfigBeanNameResolver resolver = new FacesConfigBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // validate resolving of the managed bean specified in the XML file
      assertEquals("myManagedBean", resolver.getBeanName(FacesConfigResolverManagedBean.class));

   }
   
   @Test
   public void testFacesConfigResolverWithMetaInfConfig() throws Exception
   {

      // create enumeration of our test configuration file
      Enumeration<URL> configFileEnumeration = new Vector<URL>(Arrays.asList(testXmlFile)).elements();

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.replay(servletContext);

      // ClassLoader that returns our test file
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.getResources("META-INF/faces-config.xml")).andReturn(configFileEnumeration).once();
      EasyMock.replay(classLoader);

      // initialize resolver and verify successful initialization
      FacesConfigBeanNameResolver resolver = new FacesConfigBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // validate resolving of the managed bean specified in the XML file
      assertEquals("myManagedBean", resolver.getBeanName(FacesConfigResolverManagedBean.class));

   }

   /**
    * Class of a managed bean
    */
   public class FacesConfigResolverManagedBean
   {
      // nothing
   }

}
