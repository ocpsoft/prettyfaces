/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER3
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.spi.ClassLoaderConfigurationProvider;
import com.ocpsoft.pretty.faces.config.spi.DefaultXMLConfigurationProvider;

/**
 * @author Aleksei Valikov
 */
public class PrettyConfiguratorTest
{
   private final static Map servlets = new HashMap<String, MockFacesServletRegistration>();

   final ClassLoader mockResourceLoader = new MockClassLoader(mockPrettyConfigURL());
   final ClassLoader mockEmptyResourceLoader = new MockClassLoader();

   @BeforeClass
   @SuppressWarnings("unchecked")
   public static void beforeClass()
   {
      servlets.put("Faces Servlet", new MockFacesServletRegistration());
   }

   @Test
   public void testMETAINFConfiguration() throws SAXException, IOException
   {
      final ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      EasyMock.expect(servletContext.getMajorVersion()).andReturn(3).anyTimes();
      EasyMock.expect(servletContext.getServletRegistrations()).andReturn(servlets).anyTimes();
      EasyMock.expect(servletContext.getInitParameter(PrettyContext.CONFIG_KEY)).andReturn(null).anyTimes();
      EasyMock.expect(servletContext.getClassLoader()).andReturn(mockResourceLoader).anyTimes();
      EasyMock.expect(servletContext.getResourceAsStream(DefaultXMLConfigurationProvider.DEFAULT_PRETTY_FACES_CONFIG)).andReturn(null).anyTimes();

      final PrettyConfigurator configurator = new PrettyConfigurator(servletContext);

      EasyMock.replay(servletContext);
      configurator.configure();
      final PrettyConfig config = configurator.getConfig();
      Assert.assertEquals(10, config.getMappings().size());
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testInitParameterLocationConfiguration() throws SAXException, IOException
   {
      final ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      EasyMock.expect(servletContext.getMajorVersion()).andReturn(3).anyTimes();
      EasyMock.expect(servletContext.getServletRegistrations()).andReturn(servlets).anyTimes();
      EasyMock.expect(servletContext.getClassLoader()).andReturn(mockEmptyResourceLoader).anyTimes();
      EasyMock.expect(servletContext.getInitParameter(PrettyContext.CONFIG_KEY)).andReturn("car.xml, cdr.xml").anyTimes();
      EasyMock.expect(servletContext.getInitParameter(ClassLoaderConfigurationProvider.CLASSPATH_CONFIG_ENABLED)).andReturn("false").anyTimes();
      EasyMock.expect(servletContext.getResourceAsStream("car.xml")).andReturn(mockPrettyConfigInputStream()).anyTimes();
      EasyMock.expect(servletContext.getResourceAsStream("cdr.xml")).andReturn(mockPrettyConfigInputStream()).anyTimes();
      EasyMock.expect(servletContext.getResourceAsStream(DefaultXMLConfigurationProvider.DEFAULT_PRETTY_FACES_CONFIG)).andReturn(mockPrettyConfigInputStream()).anyTimes();

      final PrettyConfigurator configurator = new PrettyConfigurator(servletContext);
      EasyMock.replay(servletContext);
      configurator.configure();
      final PrettyConfig config = configurator.getConfig();
      Assert.assertEquals(30, config.getMappings().size());

   }

   private InputStream mockPrettyConfigInputStream()
   {
      return getClass().getClassLoader().getResourceAsStream("mock-pretty-config.xml");
   }

   private URL mockPrettyConfigURL()
   {
      return getClass().getClassLoader().getResource("mock-pretty-config.xml");
   }

}
