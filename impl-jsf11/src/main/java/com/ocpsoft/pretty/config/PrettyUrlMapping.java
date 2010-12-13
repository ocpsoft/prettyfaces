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

import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.config.mapping.UrlAction;

public class PrettyUrlMapping
{
   private String id;
   private String viewId;
   private List<UrlAction> actions = new ArrayList<UrlAction>();
   private String pattern;
   private List<QueryParameter> queryParams = new ArrayList<QueryParameter>();

   public String getId()
   {
      return id;
   }

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

   public String getPattern()
   {
      return pattern;
   }

   public void setPattern(final String pattern)
   {
      this.pattern = pattern;
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
      result = prime * result + ((viewId == null) ? 0 : viewId.hashCode());
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
      if (!(obj instanceof PrettyUrlMapping))
      {
         return false;
      }
      PrettyUrlMapping other = (PrettyUrlMapping) obj;
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
}
