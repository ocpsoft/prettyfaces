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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.DigesterPrettyConfigParser;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.PrettyConfigParser;
import com.ocpsoft.pretty.faces.spi.ConfigurationProvider;

/**
 * Loads configuration files specified in web.xml init parameter
 * {@link PrettyContext#CONFIG_KEY}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ContextSpecifiedConfigurationProvider implements ConfigurationProvider
{
   private static final Log log = LogFactory.getLog(ContextSpecifiedConfigurationProvider.class);

   public PrettyConfig loadConfiguration(ServletContext servletContext)
   {
      final PrettyConfigBuilder builder = new PrettyConfigBuilder();
      PrettyConfigParser configParser = new DigesterPrettyConfigParser();
      final List<String> configFilesList = getConfigFilesList(servletContext);
      for (final String configFilePath : configFilesList)
      {
         final InputStream is = servletContext.getResourceAsStream(configFilePath);
         if (is == null)
         {
            log.error("Pretty Faces config resource [" + configFilePath + "] not found.");
            continue;
         }

         log.trace("Reading config [" + configFilePath + "].");

         try
         {
            configParser.parse(builder, is);
         }
         catch (Exception e)
         {
            throw new PrettyException("Failed to parse PrettyFaces configuration from " + configFilePath, e);
         }
         finally
         {
            try
            {
               is.close();
            }
            catch (IOException ignored)
            {

            }
         }
      }

      return builder.build();
   }

   private List<String> getConfigFilesList(ServletContext context)
   {
      final String configFiles = context.getInitParameter(PrettyContext.CONFIG_KEY);
      final List<String> configFilesList = new ArrayList<String>();
      if (configFiles != null)
      {
         final StringTokenizer st = new StringTokenizer(configFiles, ",", false);
         while (st.hasMoreTokens())
         {
            final String systemId = st.nextToken().trim();

            if (DefaultXMLConfigurationProvider.DEFAULT_PRETTY_FACES_CONFIG.equals(systemId))
            {
               log.warn("The file [" + DefaultXMLConfigurationProvider.DEFAULT_PRETTY_FACES_CONFIG + "] has been specified in the ["
                     + PrettyContext.CONFIG_KEY + "] context parameter of "
                     + "the deployment descriptor; this is unecessary and will be ignored.");
            }
            else
            {
               configFilesList.add(systemId);
            }
         }
      }
      return configFilesList;
   }
}
