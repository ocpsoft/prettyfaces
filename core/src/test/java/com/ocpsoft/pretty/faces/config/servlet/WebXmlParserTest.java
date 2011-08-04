package com.ocpsoft.pretty.faces.config.servlet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.junit.Test;

public class WebXmlParserTest
{

   @Test
   public void testWebXmlParserServlet2x() throws Exception
   {

      InputStream webXmlStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("web-xml-parser-test.xml");
      assertNotNull("web.xml not found", webXmlStream);

      ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);
      EasyMock.expect(servletContext.getResourceAsStream("/WEB-INF/web.xml")).andReturn(webXmlStream).once();
      EasyMock.replay(servletContext);

      WebXmlParser parser = new WebXmlParser();
      parser.parse(servletContext);

      assertEquals("*.jsf", parser.getFacesMapping());

   }

}
