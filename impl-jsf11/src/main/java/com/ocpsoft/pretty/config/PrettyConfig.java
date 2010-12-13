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
package com.ocpsoft.pretty.config;

import java.util.ArrayList;
import java.util.List;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.util.UrlPatternParser;

public class PrettyConfig
{
   public static final String CONFIG_REQUEST_KEY = "pretty_CONFIG_REQUEST_KEY";
   private List<PrettyUrlMapping> mappings = new ArrayList<PrettyUrlMapping>();

   public List<PrettyUrlMapping> getMappings()
   {
      return mappings;
   }

   public void setMappings(final List<PrettyUrlMapping> mappings)
   {
      this.mappings = mappings;
   }

   public void addMapping(final PrettyUrlMapping mapping)
   {
      mappings.add(mapping);
   }

   public PrettyUrlMapping getMappingForUrl(final String url)
   {
      for (PrettyUrlMapping mapping : getMappings())
      {
         String urlPattern = mapping.getPattern();
         UrlPatternParser um = new UrlPatternParser(urlPattern);
         if (um.matches(url))
         {
            return mapping;
         }
      }
      return null;
   }

   public boolean isMappingId(final String action)
   {
      PrettyUrlMapping mapping = getMappingById(action);
      return mapping instanceof PrettyUrlMapping;
   }

   public boolean isURLMapped(final String url)
   {
      PrettyUrlMapping mapping = getMappingForUrl(url);
      return (mapping != null);
   }

   public boolean isViewMapped(String viewId)
   {
      if (viewId != null)
      {
         viewId = viewId.trim();
         PrettyUrlMapping needle = new PrettyUrlMapping();
         needle.setViewId(viewId);
         if (viewId.startsWith("/"))
         {
            if (getMappings().contains(needle))
            {
               return true;
            }
            needle.setViewId(viewId.substring(1));
         }
         return getMappings().contains(needle);
      }
      return false;
   }

   /**
    * This method accepts a mapping id (with or without prefix)
    * 
    * @param id Mapping id
    * @return PrettyUrlMapping Corresponding mapping
    */
   public PrettyUrlMapping getMappingById(String id)
   {
      if (id != null)
      {
         if (id.startsWith(PrettyContext.PRETTY_PREFIX))
         {
            id = id.substring(PrettyContext.PRETTY_PREFIX.length());
         }
         for (PrettyUrlMapping mapping : getMappings())
         {
            if (mapping.getId().equals(id))
            {
               return mapping;
            }
         }
      }
      return null;
   }
}
