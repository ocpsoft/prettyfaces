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

package com.ocpsoft.pretty.faces.config.spi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.PathValidator;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.spi.ConfigurationPostProcessor;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ParentingPostProcessor implements ConfigurationPostProcessor
{
   public static final String HIERARCHY_ENABLED_PARAM = "com.ocpsoft.pretty.INHERITABLE_CONFIG";
   private final List<UrlMapping> seen = new ArrayList<UrlMapping>();

   public PrettyConfig processConfiguration(ServletContext context, PrettyConfig config)
   {
      String enabled = context.getInitParameter(HIERARCHY_ENABLED_PARAM);
      if ((enabled != null) && "false".equalsIgnoreCase(enabled.trim()))
      {
         return config;
      }

      List<UrlMapping> mappings = config.getMappings();
      for (UrlMapping m : mappings)
      {
         createAncestry(config, m);
      }
      return config;
   }

   private void createAncestry(PrettyConfig config, UrlMapping m)
   {
      if (m.hasParent() && !seen.contains(m))
      {
         UrlMapping parent = config.getMappingById(m.getParentId());
         if (parent == null)
         {
            throw new PrettyException("Error when building configuration for URL-mapping [" + m.getId() + ":"
                  + m.getPattern() + "] - the requested parentId [" + m.getParentId() + "] does not exist in the configuration.");
         }
         if (parent.hasParent())
         {
            createAncestry(config, parent);
         }
         m.setPattern(parent.getPattern() + m.getPattern());
         mergeValidators(parent, m);
         seen.add(m);
      }
   }

   private void mergeValidators(UrlMapping parent, UrlMapping child)
   {
      List<PathValidator> result = new ArrayList<PathValidator>();
      List<PathValidator> validators = new ArrayList<PathValidator>();

      validators.addAll(parent.getPathValidators());
      validators.addAll(child.getPathValidators());

      int i = 0;
      for (PathValidator pv : validators)
      {
         PathValidator temp = copy(pv);
         temp.setIndex(i++);
         result.add(temp);
      }

      child.setPathValidators(result);
   }

   private PathValidator copy(PathValidator pathValidator)
   {
      PathValidator result = new PathValidator();
      result.setIndex(pathValidator.getIndex());
      result.setOnError(pathValidator.getOnError());
      result.setValidatorIds(pathValidator.getValidatorIds());
      result.setValidatorExpression(pathValidator.getValidatorExpression());
      return result;
   }
}
