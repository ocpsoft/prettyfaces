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
package com.ocpsoft.pretty.faces.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;

import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.RequestParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.url.URLPatternParser;

/**
 * A utility class for building Pretty URLs.
 * 
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyURLBuilder
{
   /**
    * Extract any {@link UIParameter} objects from a given component. These
    * parameters are what PrettyFaces uses to communicate with the JSF component
    * tree
    * 
    * @param component
    * @return A list of {@link UIParameter} objects, or an empty list if no
    *         {@link UIParameter}s were contained within the component children.
    */
   public List<UIParameter> extractParameters(final UIComponent component)
   {
      List<UIParameter> results = new ArrayList<UIParameter>();
      for (UIComponent child : component.getChildren())
      {
         if (child instanceof UIParameter)
         {
            results.add((UIParameter) child);
         }
      }
      return results;
   }

   /**
    * Build a Pretty URL for the given UrlMapping and parameters.
    */
   public String build(final UrlMapping mapping, final Map<String, String[]> parameters)
   {
      List<UIParameter> list = new ArrayList<UIParameter>();
      for (Entry<String, String[]> e : parameters.entrySet())
      {
         UIParameter p = new UIParameter();
         p.setName(e.getKey());
         p.setValue(e.getValue());
         list.add(p);
      }
      return build(mapping, list);
   }

   /**
    * Build a Pretty URL for the given UrlMapping and parameters.
    */
   public String build(final UrlMapping mapping, final Object... parameters)
   {
      List<UIParameter> list = new ArrayList<UIParameter>();
      for (Object e : parameters)
      {
         UIParameter p = new UIParameter();
         if (e != null)
         {
            p.setValue(e.toString());
         }
         list.add(p);
      }
      return build(mapping, list);
   }

   /**
    * Build a Pretty URL for the given UrlMapping and parameters.
    */
   public String build(final UrlMapping mapping, final RequestParameter... parameters)
   {
      List<UIParameter> list = new ArrayList<UIParameter>();
      for (RequestParameter param : parameters)
      {
         UIParameter p = new UIParameter();
         if (param != null)
         {
            p.setValue(param.getName());
            p.setValue(param.getValue());
         }
         list.add(p);
      }
      return build(mapping, list);
   }

   /**
    * Build a Pretty URL for the given Mapping ID and parameters.
    */
   public String build(final UrlMapping urlMapping, final List<UIParameter> parameters)
   {
      String result = "";
      if (urlMapping != null)
      {
         URLPatternParser parser = new URLPatternParser(urlMapping.getPattern());
         List<String> pathParams = new ArrayList<String>();

         // TODO this logic should be in the components, not in the builder
         if (parameters.size() == 1)
         {
            UIParameter firstParam = parameters.get(0);
            if (((firstParam.getValue() != null)) && (firstParam.getName() == null))
            {
               if (firstParam.getValue() instanceof List<?>)
               {
                  URL url = parser.getMappedURL(firstParam.getValue());
                  return url.toURL();
               }
               else if (firstParam.getValue().getClass().isArray())
               {
                  // The Object[] cast here is required, otherwise Java treats
                  // getValue() as a single Object.
                  List<Object> list = Arrays.asList((Object[]) firstParam.getValue());
                  URL url = parser.getMappedURL(list);
                  return url.toURL();
               }
            }
         }

         List<QueryParameter> queryParams = new ArrayList<QueryParameter>();
         for (UIParameter parameter : parameters)
         {
            String name = parameter.getName();
            Object value = parameter.getValue();

            if ((name == null) && (value != null))
            {
               pathParams.add(value.toString());
            }
            else
            {
               List<?> values = null;
               if ((value != null) && value.getClass().isArray())
               {
                  values = Arrays.asList((Object[]) value);
               }
               else if (value instanceof List<?>)
               {
                  values = (List<?>) value;
               }
               else if (value != null)
               {
                  values = Arrays.asList(value);
               }

               if (values != null)
               {
                  for (Object object : values)
                  {
                     String tempValue = null;
                     if (object != null)
                     {
                        tempValue = object.toString();
                     }
                     queryParams.add(new QueryParameter(name, tempValue));
                  }
               }
               else
               {
                  queryParams.add(new QueryParameter(name, null));
               }
            }
         }

         result = parser.getMappedURL(pathParams.toArray()).toURL() + QueryString.build(queryParams).toQueryString();
      }
      return result;
   }
}
