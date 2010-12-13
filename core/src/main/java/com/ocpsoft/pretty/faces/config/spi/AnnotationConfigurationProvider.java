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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.annotation.ClassFinder;
import com.ocpsoft.pretty.faces.config.annotation.PackageFilter;
import com.ocpsoft.pretty.faces.config.annotation.PrettyAnnotationHandler;
import com.ocpsoft.pretty.faces.config.annotation.WebClassesFinder;
import com.ocpsoft.pretty.faces.config.annotation.WebLibFinder;
import com.ocpsoft.pretty.faces.el.LazyBeanNameFinder;
import com.ocpsoft.pretty.faces.spi.ConfigurationProvider;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class AnnotationConfigurationProvider implements ConfigurationProvider
{
   private static final Log log = LogFactory.getLog(AnnotationConfigurationProvider.class);

   public static final String CONFIG_SCAN_LIB_DIR = "com.ocpsoft.pretty.SCAN_LIB_DIRECTORY";
   public static final String CONFIG_BASE_PACKAGES = "com.ocpsoft.pretty.BASE_PACKAGES";

   public PrettyConfig loadConfiguration(ServletContext servletContext)
   {
      String packageFilters = servletContext.getInitParameter(CONFIG_BASE_PACKAGES);

      if ((packageFilters != null) && packageFilters.trim().equalsIgnoreCase("none"))
      {
         log.debug("Annotation scanning has is disabled!");
         return null;
      }

      PackageFilter packageFilter = new PackageFilter(packageFilters);
      LazyBeanNameFinder beanNameFinder = new LazyBeanNameFinder(servletContext);
      PrettyAnnotationHandler annotationHandler = new PrettyAnnotationHandler(beanNameFinder);

      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      if (classloader == null)
      {
         classloader = this.getClass().getClassLoader();
      }

      List<ClassFinder> classFinders = new ArrayList<ClassFinder>();

      // we will always scan /WEB-INF/classes
      classFinders.add(new WebClassesFinder(servletContext, classloader, packageFilter));

      // does the user want to scan /WEB-INF/lib ?
      String jarConfig = servletContext.getInitParameter(CONFIG_SCAN_LIB_DIR);
      if ((jarConfig != null) && jarConfig.trim().equalsIgnoreCase("true"))
      {
         classFinders.add(new WebLibFinder(servletContext, classloader, packageFilter));
      }

      for (ClassFinder finder : classFinders)
      {
         finder.findClasses(annotationHandler);
      }

      PrettyConfigBuilder builder = new PrettyConfigBuilder();
      annotationHandler.build(builder);
      return builder.build();
   }
}
