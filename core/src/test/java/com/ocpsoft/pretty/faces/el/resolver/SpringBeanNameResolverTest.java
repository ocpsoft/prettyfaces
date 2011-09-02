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
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.web.context.WebApplicationContext;

@SuppressWarnings("unchecked")
public class SpringBeanNameResolverTest
{

   @Test
   public void testNoSpringEnvironment() throws Exception
   {

      // Simple mock of ServletContext doing nothing
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      // ClassLoader that always throws ClassNotFoundExceptions
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass((String) EasyMock.anyObject())).andThrow(new ClassNotFoundException())
            .anyTimes();
      EasyMock.replay(classLoader);

      // initialize resolver and verify that initialization failed
      SpringBeanNameResolver resolver = new SpringBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertFalse(initCompleted);

   }

   @Test
   public void testSpringUnresolveableClass() throws Exception
   {

      // WebApplicationContext that doesn't know our test class
      WebApplicationContext appContext = EasyMock.createMock(WebApplicationContext.class);
      EasyMock.expect(appContext.getBeanNamesForType(SpringManagedBean.class)).andReturn(new String[0]).once();
      EasyMock.replay(appContext);

      // Simple mock of ServletContext containing the WebApplicationContext
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
            .andReturn(appContext).once();
      EasyMock.replay(servletContext);

      // ClassLoader that knows WebApplicationContext
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(WebApplicationContext.class.getName())).andReturn(
            (Class) WebApplicationContext.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      SpringBeanNameResolver resolver = new SpringBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: Bean not resolvable
      assertNull(resolver.getBeanName(SpringManagedBean.class));

   }

   @Test
   public void testSpringSuccessfulResolving() throws Exception
   {

      // WebApplicationContext that knows our test class
      WebApplicationContext appContext = EasyMock.createMock(WebApplicationContext.class);
      EasyMock.expect(appContext.getBeanNamesForType(SpringManagedBean.class)).andReturn(
            new String[] { "springManagedBean" }).once();
      EasyMock.replay(appContext);

      // Simple mock of ServletContext containing the WebApplicationContext
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
            .andReturn(appContext).once();
      EasyMock.replay(servletContext);

      // ClassLoader that knows WebApplicationContext
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(WebApplicationContext.class.getName())).andReturn(
            (Class) WebApplicationContext.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      SpringBeanNameResolver resolver = new SpringBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: Successful resolving
      assertEquals("springManagedBean", resolver.getBeanName(SpringManagedBean.class));

   }

   @Test
   public void testSpringMultipleNamesForClass() throws Exception
   {

      // WebApplicationContext that knows multiple names for our test class
      WebApplicationContext appContext = EasyMock.createMock(WebApplicationContext.class);
      EasyMock.expect(appContext.getBeanNamesForType((Class) SpringManagedBean.class)).andReturn(
            new String[] { "springManagedBean", "otherBean" }).once();
      EasyMock.replay(appContext);

      // Simple mock of ServletContext containing the WebApplicationContext
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
            .andReturn(appContext).once();
      EasyMock.replay(servletContext);

      // ClassLoader that knows WebApplicationContext
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(WebApplicationContext.class.getName())).andReturn(
            (Class) WebApplicationContext.class).once();
      EasyMock.replay(classLoader);

      // initialize resolver
      SpringBeanNameResolver resolver = new SpringBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);

      // Verify: unknown because of multiple known names
      assertNull(resolver.getBeanName(SpringManagedBean.class));

   }

   @Test
   public void testSpringScopedProxyNames() throws Exception
   {
      
      // WebApplicationContext that knows multiple names for our test class
      WebApplicationContext appContext = EasyMock.createMock(WebApplicationContext.class);
      String scopedProxyName = ScopedProxyUtils.getTargetBeanName("springManagedBean");
      EasyMock.expect(appContext.getBeanNamesForType((Class) SpringManagedBean.class)).andReturn(
            new String[] { "springManagedBean", scopedProxyName }).once();
      EasyMock.replay(appContext);
      
      // Simple mock of ServletContext containing the WebApplicationContext
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
      .andReturn(appContext).once();
      EasyMock.replay(servletContext);
      
      // ClassLoader that knows WebApplicationContext
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(WebApplicationContext.class.getName())).andReturn(
            (Class) WebApplicationContext.class).once();
      EasyMock.expect(classLoader.loadClass(ScopedProxyUtils.class.getName())).andReturn(
            (Class) ScopedProxyUtils.class).once();
      EasyMock.replay(classLoader);
      
      // initialize resolver
      SpringBeanNameResolver resolver = new SpringBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);
      
      // Verify: Successful resolving
      assertEquals("springManagedBean", resolver.getBeanName(SpringManagedBean.class));
      
   }

   @Test
   public void testSpringScopedProxyNamesWithoutAOP() throws Exception
   {
      
      // WebApplicationContext that knows multiple names for our test class
      WebApplicationContext appContext = EasyMock.createMock(WebApplicationContext.class);
      String scopedProxyName = ScopedProxyUtils.getTargetBeanName("springManagedBean");
      EasyMock.expect(appContext.getBeanNamesForType((Class) SpringManagedBean.class)).andReturn(
            new String[] { "springManagedBean", scopedProxyName }).once();
      EasyMock.replay(appContext);
      
      // Simple mock of ServletContext containing the WebApplicationContext
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
      .andReturn(appContext).once();
      EasyMock.replay(servletContext);
      
      // ClassLoader that knows WebApplicationContext
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass(WebApplicationContext.class.getName())).andReturn(
            (Class) WebApplicationContext.class).once();
      EasyMock.replay(classLoader);
      
      // initialize resolver
      SpringBeanNameResolver resolver = new SpringBeanNameResolver();
      boolean initCompleted = resolver.init(servletContext, classLoader);
      assertTrue(initCompleted);
      
      // Verify: unknown because of multiple known names
      assertNull(resolver.getBeanName(SpringManagedBean.class));
      
   }

   /**
    * Test bean
    */
   public static class SpringManagedBean
   {
      // nothing
   }

}
