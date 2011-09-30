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
package com.ocpsoft.pretty.faces.config.mapping;

import java.util.ArrayList;
import java.util.List;

import com.ocpsoft.pretty.faces.config.types.ActionElement;
import com.ocpsoft.pretty.faces.config.types.ConvertElement;
import com.ocpsoft.pretty.faces.config.types.QueryParamElement;
import com.ocpsoft.pretty.faces.config.types.UrlMappingElement;
import com.ocpsoft.pretty.faces.config.types.ValidateElement;
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
   private List<PathConverter> pathConverters = new ArrayList<PathConverter>();
   private boolean onPostback = true;
   private URLPatternParser parser = null;

   /**
    * Creates an empty {@link UrlMapping}
    */
   public UrlMapping()
   {
      // nothing
   }

   /**
    * Creates an URL mapping from the supplied JAXB object
    * 
    * @param mappingElement
    *           The JAXB object
    */
   public UrlMapping(UrlMappingElement mappingElement)
   {
      if (mappingElement.getId() != null)
      {
         id = mappingElement.getId().trim();
      }
      if (mappingElement.getParentId() != null)
      {
         parentId = mappingElement.getParentId().trim();
      }
      if (mappingElement.isOutbound() != null)
      {
         outbound = mappingElement.isOutbound();
      }
      if (mappingElement.getViewId() != null && mappingElement.getViewId().getValue() != null)
      {
         viewId = mappingElement.getViewId().getValue().trim();
      }
      if (mappingElement.isOnPostback() != null)
      {
         onPostback = mappingElement.isOnPostback();
      }
      if (mappingElement.getPattern() != null)
      {
         if (mappingElement.getPattern().getValue() != null)
         {
            pattern = mappingElement.getPattern().getValue().trim();
         }
         if (mappingElement.getPattern().getValidate() != null)
         {
            for (ValidateElement validateElement : mappingElement.getPattern().getValidate())
            {
               pathValidators.add(new PathValidator(validateElement));
            }
         }
         if (mappingElement.getPattern().getConvert() != null)
         {
            for (ConvertElement converterElement : mappingElement.getPattern().getConvert())
            {
               pathConverters.add(new PathConverter(converterElement));
            }
         }
      }
      for (ActionElement actionElement : mappingElement.getAction())
      {
         actions.add(new UrlAction(actionElement));
      }
      for (QueryParamElement queryParamElement : mappingElement.getQueryParam())
      {
         queryParams.add(new QueryParameter(queryParamElement));
      }
   }

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
      if ((parser == null) && (pattern != null))
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
    * Adds a new converter to the list of path parameter converters
    */
   public void addPathConverter(PathConverter converter)
   {
      this.pathConverters.add(converter);
   }

   /**
    * Returns the {@link PathConverter} for the supplied {@link PathParameter}. If no converter has been explicitly
    * configured for the parameter the method will return <code>null</code>.
    */
   public PathConverter getPathConverterForPathParam(final PathParameter param)
   {
      for (PathConverter converter : pathConverters)
      {
         if (converter.getIndex() == param.getPosition())
         {
            return converter;
         }
      }
      return null;
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
      // reset the parser so it gets recreated
      this.parser = null;
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
      result = prime * result + (id == null ? 0 : id.hashCode());
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
      if (id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!id.equals(other.id))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "UrlMapping [ " + "id=" + id + ", pattern=" + pattern + ", parentId=" + parentId + ", viewId=" + viewId
               + ", actions=" + actions + ", outbound=" + outbound + ", parser=" + parser + ", pathValidators="
               + pathValidators + ", queryParams=" + queryParams + "]";
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

   public void setOnPostback(final boolean onPostback)
   {
      this.onPostback = onPostback;
   }

   public String getParentId()
   {
      return parentId;
   }

   public void setParentId(final String parentId)
   {
      this.parentId = parentId;
   }

   public boolean hasParent()
   {
      return (parentId != null) && !"".equals(parentId.trim());
   }
}
