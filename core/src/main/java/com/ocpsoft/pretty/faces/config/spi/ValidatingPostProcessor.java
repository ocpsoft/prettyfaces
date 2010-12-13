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

import javax.servlet.ServletContext;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.spi.ConfigurationPostProcessor;

/**
 * Ensure that the configuration is properly constructed and compiles.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ValidatingPostProcessor implements ConfigurationPostProcessor
{
   public static final String VALIDATION_ENABLED_PARAM = "com.ocpsoft.pretty.VALIDATE_CONFIG";

   public PrettyConfig processConfiguration(ServletContext context, PrettyConfig config)
   {
      String enabled = context.getInitParameter(VALIDATION_ENABLED_PARAM);
      if ((enabled != null) && "false".equalsIgnoreCase(enabled.trim()))
      {
         return config;
      }

      for (UrlMapping m : config.getMappings())
      {
         m.getPatternParser();
      }

      return config;
   }
}
