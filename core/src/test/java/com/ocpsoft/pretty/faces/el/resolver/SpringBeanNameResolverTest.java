package com.ocpsoft.pretty.faces.el.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.ServletContext;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
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

   /**
    * Test bean
    */
   public static class SpringManagedBean
   {
      // nothing
   }

}
