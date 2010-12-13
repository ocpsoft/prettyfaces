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

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContext;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class CDIBeanNameResolverTest
{

   @Test
   public void testNoCDIEnvironment() throws Exception
   {

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      // ClassLoader that always throws ClassNotFoundExceptions
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass((String) EasyMock.anyObject())).andThrow(new ClassNotFoundException()).once();
      EasyMock.replay(classLoader);

      // initialize resolver and verify that initialization failed
      CDIBeanNameResolver resolver = new CDIBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertFalse(initCompleted);

   }

   @Test
   public void testUnresolveableClass() throws Exception
   {

      // Create BeanManager that always returns empty sets
      BeanManager beanManager = EasyMock.createNiceMock(BeanManager.class);
      EasyMock.expect(beanManager.getBeans((String) EasyMock.anyObject())).andReturn(new HashSet<Bean<?>>()).anyTimes();
      EasyMock.replay(beanManager);

      // Simple mock of ServletContext containing the BeanManager
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(BeanManager.class.getName())).andReturn(beanManager).once();
      EasyMock.replay(servletContext);

      // ClassLoader that knows BeanManager and Bean classes
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(BeanManager.class.getName())).andReturn((Class) BeanManager.class).once();
      EasyMock.expect(classLoader.loadClass(Bean.class.getName())).andReturn((Class) Bean.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      CDIBeanNameResolver resolver = new CDIBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: Bean not resolvable
      assertNull(resolver.getBeanName(TestBean.class));

   }

   @Test
   public void testSuccessfulResolving() throws Exception
   {

      // create one Bean instance for our test class
      Bean<TestBean> testBean = EasyMock.createNiceMock(Bean.class);
      EasyMock.expect(testBean.getName()).andReturn("simpleBean").once();
      EasyMock.replay(testBean);
      Set<Bean<?>> beanSet = new HashSet<Bean<?>>();
      beanSet.add(testBean);

      // Create BeanManager that knows our test class
      BeanManager beanManager = EasyMock.createNiceMock(BeanManager.class);
      EasyMock.expect(beanManager.getBeans(TestBean.class)).andReturn(beanSet).once();
      EasyMock.replay(beanManager);

      // Simple mock of ServletContext containing the BeanManager
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(BeanManager.class.getName())).andReturn(beanManager).once();
      EasyMock.replay(servletContext);

      // ClassLoader that knows BeanManager and Bean classes
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(BeanManager.class.getName())).andReturn((Class) BeanManager.class).once();
      EasyMock.expect(classLoader.loadClass(Bean.class.getName())).andReturn((Class) Bean.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      CDIBeanNameResolver resolver = new CDIBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: Successful resolving
      assertEquals("simpleBean", resolver.getBeanName(TestBean.class));

   }

   @Test
   public void testMultipleNamesForClass() throws Exception
   {

      // add two beans to the result set
      Set<Bean<?>> beanSet = new HashSet<Bean<?>>();
      beanSet.add(EasyMock.createNiceMock(Bean.class));
      beanSet.add(EasyMock.createNiceMock(Bean.class));

      // Create BeanManager that knows our test class
      BeanManager beanManager = EasyMock.createNiceMock(BeanManager.class);
      EasyMock.expect(beanManager.getBeans(TestBean.class)).andReturn(beanSet).once();
      EasyMock.replay(beanManager);

      // Simple mock of ServletContext containing the BeanManager
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(BeanManager.class.getName())).andReturn(beanManager).once();
      EasyMock.replay(servletContext);

      // ClassLoader that knows BeanManager and Bean classes
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(BeanManager.class.getName())).andReturn((Class) BeanManager.class).once();
      EasyMock.expect(classLoader.loadClass(Bean.class.getName())).andReturn((Class) Bean.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      CDIBeanNameResolver resolver = new CDIBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: unknown because of multiple known names
      assertNull(resolver.getBeanName(TestBean.class));

   }

   /**
    * Test bean
    */
   public static class TestBean
   {
      // nothing
   }

}
