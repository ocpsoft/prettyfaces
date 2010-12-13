/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.ocpsoft.pretty.faces.config.spi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.xml.sax.SAXException;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.DigesterPrettyConfigParser;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.PrettyConfigParser;
import com.ocpsoft.pretty.faces.spi.ConfigurationProvider;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ClassLoaderConfigurationProvider implements ConfigurationProvider
{
   public static final String PRETTY_CONFIG_RESOURCE = "META-INF/pretty-config.xml";
   public static final String CLASSPATH_CONFIG_ENABLED = "com.ocpsoft.pretty.LOAD_CLASSPATH_CONFIG";

   public PrettyConfig loadConfiguration(ServletContext context)
   {
      String enabled = context.getInitParameter(CLASSPATH_CONFIG_ENABLED);
      if ((enabled != null) && "false".equalsIgnoreCase(enabled.trim()))
      {
         return null;
      }

      final PrettyConfigBuilder builder = new PrettyConfigBuilder();
      PrettyConfigParser configParser = new DigesterPrettyConfigParser();
      try
      {
         final Enumeration<URL> urls = getClass().getClassLoader().getResources(PRETTY_CONFIG_RESOURCE);
         if (urls != null)
         {
            while (urls.hasMoreElements())
            {
               final URL url = urls.nextElement();
               if (url != null)
               {
                  InputStream is = null;
                  try
                  {
                     is = openStream(url);
                     try
                     {
                        configParser.parse(builder, is);
                     }
                     catch (SAXException e)
                     {
                        throw new PrettyException("Failed to parse PrettyFaces configuration from URL:" + url, e);
                     }
                  }
                  finally
                  {
                     if (is != null)
                     {
                        is.close();
                     }
                  }
               }
            }
         }
      }
      catch (Exception e)
      {
         throw new PrettyException("Could not get references to PrettyFaces ClassLoader-configuration elements.", e);
      }
      return builder.build();
   }

   private InputStream openStream(final URL url) throws IOException
   {
      final URLConnection connection = url.openConnection();
      connection.setUseCaches(false);
      return connection.getInputStream();
   }

}
