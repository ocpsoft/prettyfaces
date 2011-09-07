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

package com.ocpsoft.pretty.faces.config.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.xml.sax.SAXException;

import com.ocpsoft.logging.Logger;
import com.ocpsoft.pretty.faces.config.PrettyConfigParser;
import com.ocpsoft.pretty.faces.util.SimpleXMLParserBase;

/**
 * Digester-based implementation of {@link PrettyConfigParser}.
 * 
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class WebXmlParser
{
   private static final Logger log = Logger.getLogger(WebXmlParser.class);

   private static final String FACES_SERVLET = "javax.faces.webapp.FacesServlet";
   private static final String WEB_XML_PATH = "/WEB-INF/web.xml";

   String facesMapping = null;

   public void parse(final ServletContext context) throws IOException, SAXException
   {
      if (context.getMajorVersion() >= 3)
      {
         Map<String, ? extends ServletRegistration> servlets = context.getServletRegistrations();
         if (servlets != null)
         {
            for (ServletRegistration s : servlets.values())
            {
               if (s.getClassName().equalsIgnoreCase(FACES_SERVLET))
               {
                  Collection<String> mappings = s.getMappings();
                  if (!mappings.isEmpty())
                  {
                     facesMapping = mappings.iterator().next();
                     break;
                  }
               }
            }
         }

         if (facesMapping == null)
         {
            log.warn("Faces Servlet (javax.faces.webapp.FacesServlet) not found in web context - cannot configure PrettyFaces DynaView");
         }
      }
      else
      {
         InputStream in = context.getResourceAsStream(WEB_XML_PATH);
         if (in == null)
         {
            log.warn("No " + WEB_XML_PATH + " found - cannot configure PrettyFaces DynaView");
         }

         if (in != null)
         {
            WebXmlSaxParser parser = new WebXmlSaxParser();
            parser.parse(in);
            processConfig(parser.getWebXml());
         }
      }

      log.trace("Completed parsing web.xml");
   }

   private void processConfig(final WebXml webXml)
   {
      ServletDefinition facesServlet = null;
      if (webXml != null)
      {
         Iterator<ServletDefinition> si = webXml.getServlets().iterator();
         while ((facesServlet == null) && si.hasNext())
         {
            ServletDefinition servlet = si.next();
            if (FACES_SERVLET.equals(servlet.getServletClass()))
            {
               facesServlet = servlet;
               Iterator<ServletMapping> mi = webXml.getServletMappings().iterator();
               while ((facesMapping == null) && mi.hasNext())
               {
                  ServletMapping mapping = mi.next();
                  if (facesServlet.getServletName().equals(mapping.getServletName()))
                  {
                     facesMapping = mapping.getUrlPattern().trim();
                  }
               }
            }
         }
      }
   }

   public boolean isFacesPresent()
   {
      return facesMapping != null;
   }

   public String getFacesMapping()
   {
      if (isFacesPresent())
      {
         return facesMapping;
      }
      return "";
   }

   /**
    * Implementation of {@link SimpleXMLParserBase} that reads all servlet-mapping declarations of a web.xml
    */
   private static class WebXmlSaxParser extends SimpleXMLParserBase
   {

      private final WebXml webXml = new WebXml();

      private ServletDefinition currentServletDefinition;

      private ServletMapping currentServletMapping;

      @Override
      public void processStartElement(final String name)
      {

         // <servlet>
         if (elements("web-app", "servlet"))
         {
            currentServletDefinition = new ServletDefinition();
         }

         // <servlet-mapping>
         if (elements("web-app", "servlet-mapping"))
         {
            currentServletMapping = new ServletMapping();
         }

      }

      @Override
      public void processEndElement(final String name)
      {

         // </servlet>
         if (elements("web-app", "servlet"))
         {
            webXml.addServlet(currentServletDefinition);
            currentServletDefinition = null;
         }

         // </servlet-mapping>
         if (elements("web-app", "servlet-mapping"))
         {
            webXml.addServletMapping(currentServletMapping);
            currentServletMapping = null;
         }

      }

      @Override
      public void processCharacters(final String text)
      {
         if (elements("web-app", "servlet", "servlet-name"))
         {
            currentServletDefinition.setServletName(text.trim());
         }

         if (elements("web-app", "servlet", "servlet-class"))
         {
            currentServletDefinition.setServletClass(text.trim());
         }

         if (elements("web-app", "servlet-mapping", "servlet-name"))
         {
            currentServletMapping.setServletName(text.trim());
         }

         if (elements("web-app", "servlet-mapping", "url-pattern"))
         {
            currentServletMapping.setUrlPattern(text.trim());
         }

      }

      public WebXml getWebXml()
      {
         return webXml;
      }

   }
}
