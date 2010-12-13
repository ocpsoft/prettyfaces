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
 * Public License along with this program. If not, see the file COPYING.LESSER3
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.dynaview.DynaviewEngine;
import com.ocpsoft.pretty.faces.config.servlet.WebXmlParser;
import com.ocpsoft.pretty.faces.config.spi.ParentingPostProcessor;
import com.ocpsoft.pretty.faces.config.spi.ValidatingPostProcessor;
import com.ocpsoft.pretty.faces.spi.ConfigurationPostProcessor;
import com.ocpsoft.pretty.faces.spi.ConfigurationProvider;
import com.ocpsoft.pretty.faces.util.ServiceLoader;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com>Lincoln Baxter, III</a>
 * @author Aleksei Valikov
 */
public class PrettyConfigurator
{

   private static final Log log = LogFactory.getLog(PrettyConfigurator.class);

   private final ServletContext servletContext;

   private final WebXmlParser webXmlParser = new WebXmlParser();
   private final DynaviewEngine dynaview = new DynaviewEngine();

   private PrettyConfig config;

   public PrettyConfigurator(final ServletContext servletContext)
   {
      this.servletContext = servletContext;
   }

   public void configure()
   {
      try
      {
         final PrettyConfigBuilder builder = new PrettyConfigBuilder();

         ServiceLoader<ConfigurationProvider> configLoader = ServiceLoader.load(ConfigurationProvider.class);
         for (ConfigurationProvider p : configLoader)
         {
            builder.addFromConfig(p.loadConfiguration(servletContext));
         }

         config = builder.build();
         config.setDynaviewId(getFacesDynaViewId());

         /*
          * Do the built-in post-processing manually to ensure ordering
          */
         ConfigurationPostProcessor parenting = new ParentingPostProcessor();

         config = parenting.processConfiguration(servletContext, config);

         ServiceLoader<ConfigurationPostProcessor> postProcessors = ServiceLoader.load(ConfigurationPostProcessor.class);
         for (ConfigurationPostProcessor p : postProcessors)
         {
            config = p.processConfiguration(servletContext, config);
         }

         ConfigurationPostProcessor validating = new ValidatingPostProcessor();
         config = validating.processConfiguration(servletContext, config);

         log.trace("Setting config into ServletContext");
         servletContext.setAttribute(PrettyContext.CONFIG_KEY, config);
      }
      catch (Exception e)
      {
         throw new PrettyException("Failed to load configuration.", e);
      }
   }

   private String getFacesDynaViewId()
   {
      try
      {
         webXmlParser.parse(servletContext);
         return dynaview.buildDynaViewId(webXmlParser.getFacesMapping());
      }
      catch (Exception e)
      {
         throw new PrettyException("Could not retrieve DynaViewId.", e);
      }
   }

   public PrettyConfig getConfig()
   {
      return config;
   }

}
