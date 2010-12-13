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
package com.ocpsoft.pretty.faces.spi;

import javax.servlet.ServletContext;

import com.ocpsoft.pretty.faces.config.PrettyConfig;

/**
 * Defines the interface to be used in performing post-config-parse step
 * processing.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ConfigurationPostProcessor
{

   /**
    * Process the given PrettyConfig, returning the modified configuration.
    * 
    * @param servletContext The {@link ServletContext} for the application being
    *           configured.
    * @param config The {@link PrettyConfig} to process
    * @return The processed configuration
    */
   PrettyConfig processConfiguration(ServletContext context, PrettyConfig config);

}
