package com.ocpsoft.pretty.faces.config.annotation;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

public class WebClassesFinderTest
{

   @Test
   @SuppressWarnings("unchecked")
   public void testWebClassesFinder() throws Exception
   {

      // URLs of simulated directories and files
      URL classesUrl = new URL("file:/somewhere/WEB-INF/classes/");
      URL packageUrl = new URL("file:/somewhere/WEB-INF/classes/package/");
      URL classUrl = new URL("file:/somewhere/WEB-INF/classes/package/TestClass.class");

      // Contents of directories
      Set<String> classesDirectory = new HashSet<String>(Arrays.asList("/WEB-INF/classes/package/"));
      Set<String> packageDirectory = new HashSet<String>(Arrays.asList("/WEB-INF/classes/package/TestClass.class"));

      // Create filter accepting all packages
      PackageFilter filter = new PackageFilter(null);

      // Mock of ServletContext that knows the directories and files
      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getResource("/WEB-INF/classes/")).andReturn(classesUrl).anyTimes();
      EasyMock.expect(servletContext.getResourcePaths("/WEB-INF/classes/")).andReturn(classesDirectory).anyTimes();
      EasyMock.expect(servletContext.getResource("/WEB-INF/classes/package/")).andReturn(packageUrl).anyTimes();
      EasyMock.expect(servletContext.getResourcePaths("/WEB-INF/classes/package/")).andReturn(packageDirectory).anyTimes();
      EasyMock.expect(servletContext.getResource("/WEB-INF/classes/package/TestClass.class")).andReturn(classUrl).anyTimes();
      EasyMock.replay(servletContext);

      // ClassLoader that knows the test class
      ClassLoader classLoader = EasyMock.createNiceMock(ClassLoader.class);
      EasyMock.expect(classLoader.loadClass("package.TestClass")).andReturn((Class) ClassFinderTestBean.class).anyTimes();
      EasyMock.replay(classLoader);

      // Prepare mock of PrettyAnnotationHandler
      PrettyAnnotationHandler handler = EasyMock.createMock(PrettyAnnotationHandler.class);
      handler.processClass(ClassFinderTestBean.class);
      EasyMock.expectLastCall().once();
      EasyMock.replay(handler);

      // Run finder
      WebClassesFinder finder = new WebClassesFinder(servletContext, classLoader, filter);
      finder.findClasses(handler);

      // Verify that the PrettyAnnotationHandler has been notified
      EasyMock.verify(handler);

   }

}
