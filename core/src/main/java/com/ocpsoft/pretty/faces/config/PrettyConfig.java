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

package com.ocpsoft.pretty.faces.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyFilter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.url.URL;

public class PrettyConfig
{
   public static final String CONFIG_REQUEST_KEY = "pretty_CONFIG_REQUEST_KEY";
   private List<UrlMapping> mappings = new ArrayList<UrlMapping>();
   private List<RewriteRule> globalRewriteRules = new ArrayList<RewriteRule>();
   private String dynaviewId = "";

   /**
    * Set the current DynaView ID. This is used when calculating dynamic viewIds
    * specified in pretty-config.xml (Do not change unless you know what you are
    * doing - this maps directly to your Faces Servlet mapping and is discovered
    * automatically when {@link PrettyFilter} starts up.
    */
   public void setDynaviewId(String facesDynaViewId)
   {
      this.dynaviewId = facesDynaViewId;
   }

   /**
    * Get the current DynaView ID. This is the viewId to which
    * {@link PrettyFilter} will issue a servlet forward when the developer has
    * requested a dynamic view-id in a url-mapping.
    */
   public String getDynaviewId()
   {
      return dynaviewId;
   }

   /**
    * Return the currently configured List of {@link RewriteRule} as an
    * unmodifiable collection.
    */
   public List<RewriteRule> getGlobalRewriteRules()
   {
      return globalRewriteRules;
   }

   /**
    * Set the current list of {@link RewriteRule} objects.
    */
   public void setGlobalRewriteRules(final List<RewriteRule> rules)
   {
      globalRewriteRules = Collections.unmodifiableList(rules);
   }

   /**
    * Get the currently configured list of {@link UrlMapping} as an unmodifiable
    * List
    */
   public List<UrlMapping> getMappings()
   {
      return Collections.unmodifiableList(mappings);
   }

   /**
    * Set the currently configured list of {@link UrlMapping}
    */
   public void setMappings(final List<UrlMapping> mappings)
   {
      Collections.sort(mappings, UrlMapping.ORDINAL_COMPARATOR);
      this.mappings = Collections.unmodifiableList(mappings);
   }

   /**
    * Search through all currently configured {@link UrlMapping} objects for the
    * first one that matches the given URL.
    * 
    * @return the first appropriate {@link UrlMapping} for a given URL.
    */
   public UrlMapping getMappingForUrl(final URL url)
   {
      for (UrlMapping mapping : getMappings())
      {
         if (mapping.matches(url))
         {
            return mapping;
         }
      }
      return null;
   }

   /**
    * Discover if the given id is a {@link UrlMapping} id specified in the
    * current configuration.
    * 
    * @return True if the id is found, false if not.
    */
   public boolean isMappingId(final String id)
   {
      UrlMapping mapping = getMappingById(id);
      return mapping instanceof UrlMapping;
   }

   /**
    * Discover if the given URL is mapped by any {@link UrlMapping} specified in
    * the current configuration.
    * 
    * @return True if the URL is mapped, false if not.
    */
   public boolean isURLMapped(final URL url)
   {
      UrlMapping mapping = getMappingForUrl(url);
      return mapping != null;
   }

   /**
    * Discover if the given ViewId is mapped by any {@link UrlMapping} specified
    * in the current configuration.
    * <p>
    * <b>Note:</b>This will not match if a #{dynamicView.id} method is
    * configured.
    * 
    * @return True if the ViewId is mapped, false if not.
    */
   public boolean isViewMapped(String viewId)
   {
      if (viewId != null)
      {
         viewId = viewId.trim();
         UrlMapping needle = new UrlMapping();
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
    * Get the {@link UrlMapping} corresponding with the given mapping id.
    * 
    * @param id Mapping id
    * @return PrettyUrlMapping Corresponding mapping
    */
   public UrlMapping getMappingById(String id)
   {
      if (id != null)
      {
         if (id.startsWith(PrettyContext.PRETTY_PREFIX))
         {
            id = id.substring(PrettyContext.PRETTY_PREFIX.length());
         }
         for (UrlMapping mapping : getMappings())
         {
            if (mapping.getId().equals(id))
            {
               return mapping;
            }
         }
      }
      return null;
   }

   @Override
   public String toString()
   {
      return "PrettyConfig [mappings=" + mappings + ", globalRewriteRules=" + globalRewriteRules + "]";
   }
}
