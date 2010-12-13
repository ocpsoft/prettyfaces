/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2010 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */

package com.ocpsoft.pretty.faces.config.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.faces.config.PrettyConfigParser;
import com.ocpsoft.pretty.faces.util.EmptyEntityResolver;

/**
 * Digester-based implementation of {@link PrettyConfigParser}.
 * 
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class WebXmlParser
{
   private static final Log log = LogFactory.getLog(WebXmlParser.class);

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

         WebXml webXml = new WebXml();
         if (in != null)
         {
            Digester digester = getConfiguredDigester();
            digester.push(webXml);
            digester.parse(in);
            processConfig(webXml);
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

   private Digester getConfiguredDigester()
   {
      final Digester digester = new Digester();

      /*
       * We use the context class loader to resolve classes. This fixes
       * ClassNotFoundExceptions on Geronimo.
       */
      digester.setUseContextClassLoader(true);

      // prevent downloading of DTDs
      digester.setEntityResolver(new EmptyEntityResolver());

      digester.addObjectCreate("web-app/servlet", ServletDefinition.class);
      digester.addCallMethod("web-app/servlet/servlet-name", "setServletName", 0);
      digester.addCallMethod("web-app/servlet/servlet-class", "setServletClass", 0);
      digester.addSetNext("web-app/servlet", "addServlet");

      digester.addObjectCreate("web-app/servlet-mapping", ServletMapping.class);
      digester.addCallMethod("web-app/servlet-mapping/servlet-name", "setServletName", 0);
      digester.addCallMethod("web-app/servlet-mapping/url-pattern", "setUrlPattern", 0);
      digester.addSetNext("web-app/servlet-mapping", "addServletMapping");

      return digester;
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
}
