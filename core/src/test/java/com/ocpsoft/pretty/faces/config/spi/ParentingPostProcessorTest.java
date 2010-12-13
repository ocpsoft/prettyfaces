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
package com.ocpsoft.pretty.faces.config.spi;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.DigesterPrettyConfigParser;
import com.ocpsoft.pretty.faces.config.MockClassLoader;
import com.ocpsoft.pretty.faces.config.MockFacesServletRegistration;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.PrettyConfigurator;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ParentingPostProcessorTest
{
   private final static Map servlets = new HashMap<String, MockFacesServletRegistration>();
   final ClassLoader mockResourceLoader = new MockClassLoader();

   @BeforeClass
   @SuppressWarnings("unchecked")
   public static void beforeClass()
   {
      servlets.put("Faces Servlet", new MockFacesServletRegistration());
   }

   private final Enumeration<String> initParameterNames = Collections.enumeration(new ArrayList<String>());

   @Test
   public void testParseParentIds() throws Exception
   {

      final PrettyConfigBuilder builder = new PrettyConfigBuilder();
      new DigesterPrettyConfigParser().parse(builder, getClass().getClassLoader().getResourceAsStream("parenting-pretty-config.xml"));
      PrettyConfig config = builder.build();

      assertEquals("", config.getMappingById("parent").getParentId());
      assertEquals("parent", config.getMappingById("child").getParentId());
      assertEquals("child", config.getMappingById("grandchild").getParentId());
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testParentIdInheritsPatterns() throws SAXException, IOException
   {
      final ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

      EasyMock.expect(servletContext.getMajorVersion()).andReturn(3).anyTimes();
      EasyMock.expect(servletContext.getServletRegistrations()).andReturn(servlets).anyTimes();
      EasyMock.expect(servletContext.getClassLoader()).andReturn(mockResourceLoader).anyTimes();
      EasyMock.expect(servletContext.getInitParameterNames()).andReturn(initParameterNames).anyTimes();
      EasyMock.expect(servletContext.getInitParameter(PrettyContext.CONFIG_KEY)).andReturn(null).anyTimes();
      EasyMock.expect(servletContext.getInitParameter(ClassLoaderConfigurationProvider.CLASSPATH_CONFIG_ENABLED)).andReturn("false").anyTimes();
      EasyMock.expect(servletContext.getResourceAsStream(DefaultXMLConfigurationProvider.DEFAULT_PRETTY_FACES_CONFIG)).andReturn(mockPrettyConfigInputStream()).anyTimes();

      final PrettyConfigurator configurator = new PrettyConfigurator(servletContext);
      EasyMock.replay(servletContext);
      configurator.configure();

      final PrettyConfig config = configurator.getConfig();

      assertEquals(3, config.getMappings().size());
      assertEquals("/parent", config.getMappingById("parent").getPattern());
      assertEquals("/parent/child/#{name}", config.getMappingById("child").getPattern());
      assertEquals("/parent/child/#{name}/grandchild/#{gname}", config.getMappingById("grandchild").getPattern());

      assertEquals(2, config.getMappingById("grandchild").getPathValidators().size());
      assertEquals("validator1", config.getMappingById("grandchild").getPathValidators().get(0).getValidatorIds());
      assertEquals("validator2", config.getMappingById("grandchild").getPathValidators().get(1).getValidatorIds());
      assertEquals(0, config.getMappingById("grandchild").getPathValidators().get(0).getIndex());
      assertEquals(1, config.getMappingById("grandchild").getPathValidators().get(1).getIndex());
   }

   private InputStream mockPrettyConfigInputStream()
   {
      return getClass().getClassLoader().getResourceAsStream("parenting-pretty-config.xml");
   }

}
