package com.ocpsoft.pretty.faces.config.mapping;

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
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.ocpsoft.pretty.faces.el.Expressions;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.url.URLPatternParser;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class UrlMapping
{
   private String id = "";
   private String parentId = "";
   private boolean outbound = true;
   private String viewId = "";
   private List<UrlAction> actions = new ArrayList<UrlAction>();
   private String pattern = "";
   private List<QueryParameter> queryParams = new ArrayList<QueryParameter>();
   private List<PathValidator> pathValidators = new ArrayList<PathValidator>();
   private boolean onPostback = true;
   private URLPatternParser parser;
   private boolean dirty = true;

   public static final Comparator<UrlMapping> ORDINAL_COMPARATOR = new Comparator<UrlMapping>()
   {
      public int compare(final UrlMapping l, final UrlMapping r)
      {
         if (l.getPatternParser().getParameterCount() < r.getPatternParser().getParameterCount())
         {
            return 1;
         }
         else if (l.getPatternParser().getParameterCount() > r.getPatternParser().getParameterCount())
         {
            return -1;
         }
         return 0;
      }
   };

   /**
    * Return whether or not this Mapping requires DynaView capabilities
    * 
    * @return True if using DynaView, false if not
    */
   public boolean isDynaView()
   {
      return Expressions.isEL(viewId);
   }

   /**
    * Get this patterns pattern parser instance.
    */
   public URLPatternParser getPatternParser()
   {
      if (((parser == null) || dirty) && (pattern != null))
      {
         this.parser = new URLPatternParser(pattern);
      }
      return parser;
   }

   /**
    * Return a list of validators that belong to the given parameter.
    */
   public List<PathValidator> getValidatorsForPathParam(final PathParameter param)
   {
      List<PathValidator> result = new ArrayList<PathValidator>();
      for (PathValidator pv : pathValidators)
      {
         if (pv.getIndex() == param.getPosition())
         {
            result.add(pv);
         }
      }
      return result;
   }

   /**
    * Test this mapping against a given URL.
    * 
    * @return True if this mapping matches the URL, false if not.
    */
   public boolean matches(final URL url)
   {
      return getPatternParser().matches(url);
   }

   /**
    * Get this mapping's ID (not prefixed with "pretty:")
    */
   public String getId()
   {
      return id;
   }

   /**
    * Set this mapping's ID (not prefixed with "pretty:")
    */
   public void setId(final String id)
   {
      this.id = id;
   }

   public String getViewId()
   {
      return viewId;
   }

   public void setViewId(final String viewId)
   {
      this.viewId = viewId;
   }

   public List<UrlAction> getActions()
   {
      return actions;
   }

   public void setActions(final List<UrlAction> actions)
   {
      this.actions = actions;
   }

   public void addAction(final UrlAction action)
   {
      actions.add(action);
   }

   public void addPathValidator(final PathValidator v)
   {
      pathValidators.add(v);
   }

   public List<PathValidator> getPathValidators()
   {
      return pathValidators;
   }

   public void setPathValidators(final List<PathValidator> pathValidators)
   {
      this.pathValidators = pathValidators;
   }

   public String getPattern()
   {
      return pattern;
   }

   public void setPattern(final String pattern)
   {
      this.pattern = pattern;
      this.dirty = true;
   }

   public boolean addQueryParam(final QueryParameter param)
   {
      return queryParams.add(param);
   }

   public List<QueryParameter> getQueryParams()
   {
      return queryParams;
   }

   public void setQueryParams(final List<QueryParameter> queryParams)
   {
      this.queryParams = queryParams;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (viewId == null ? 0 : viewId.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof UrlMapping))
      {
         return false;
      }
      UrlMapping other = (UrlMapping) obj;
      if (viewId == null)
      {
         if (other.viewId != null)
         {
            return false;
         }
      }
      else if (!viewId.equals(other.viewId))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "UrlMapping [ " + "id=" + id + ", pattern=" + pattern + ", parentId=" + parentId + ", viewId=" + viewId + ", actions=" + actions + ", outbound=" + outbound + ", parser=" + parser + ", pathValidators=" + pathValidators + ", queryParams=" + queryParams + "]";
   }

   public boolean isOutbound()
   {
      return outbound;
   }

   public void setOutbound(final boolean outbound)
   {
      this.outbound = outbound;
   }

   public boolean isOnPostback()
   {
      return onPostback;
   }

   public void setOnPostback(boolean onPostback)
   {
      this.onPostback = onPostback;
   }

   public String getParentId()
   {
      return parentId;
   }

   public void setParentId(String parentId)
   {
      this.parentId = parentId;
   }

   public boolean hasParent()
   {
      return (parentId != null) && !"".equals(parentId.trim());
   }
}
