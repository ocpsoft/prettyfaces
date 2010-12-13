/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER or visit the GNU
 * website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.beans;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELException;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.url.URLPatternParser;
import com.ocpsoft.pretty.faces.util.FacesElUtils;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class ExtractedValuesURLBuilder
{
   private static final FacesElUtils elUtils = new FacesElUtils();

   /**
    * For all required values of the given PrettyUrlMapping, extract values from their mapped backing beans and create a
    * URL based on the url-pattern.
    * 
    * @param mapping Mapping for which to extract values and generate URL
    * @return The fully constructed URL
    */
   public URL buildURL(final UrlMapping mapping)
   {
      URL result = null;

      String expression = "";
      Object value = null;
      try
      {
         FacesContext context = FacesContext.getCurrentInstance();

         URLPatternParser parser = new URLPatternParser(mapping.getPattern());
         List<PathParameter> parameters = parser.getPathParameters();
         List<String> parameterValues = new ArrayList<String>();
         for (PathParameter injection : parameters)
         {
            expression = injection.getExpression().getELExpression();
            value = elUtils.getValue(context, expression);
            if (value == null)
            {
               throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                        + mapping.getId() + " >, Required value " + " < " + expression + " > was null");
            }
            parameterValues.add(value.toString());
         }

         result = parser.getMappedURL(parameterValues);
      }
      catch (ELException e)
      {
         throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                  + mapping.getId() + " >, Error occurred while extracting values from backing bean" + " < "
                  + expression + ":" + value + " >", e);
      }

      return result;
   }

   public QueryString buildQueryString(final UrlMapping mapping)
   {
      QueryString result = new QueryString();

      String expression = "";
      Object value = null;
      try
      {
         FacesContext context = FacesContext.getCurrentInstance();

         List<QueryParameter> queryParams = mapping.getQueryParams();
         List<QueryParameter> queryParameterValues = new ArrayList<QueryParameter>();

         for (QueryParameter injection : queryParams)
         {
            String name = injection.getName();

            expression = injection.getExpression().getELExpression();
            value = elUtils.getValue(context, expression);

            if ((name != null) && (value != null))
            {
               if (value.getClass().isArray())
               {
                  Object[] values = (Object[]) value;
                  for (Object temp : values)
                  {
                     queryParameterValues.add(new QueryParameter(name, temp.toString()));
                  }
               }
               else
               {
                  queryParameterValues.add(new QueryParameter(name, value.toString()));
               }
            }
         }

         result = QueryString.build(queryParameterValues);
      }
      catch (ELException e)
      {
         throw new PrettyException("PrettyFaces: Exception occurred while building QueryString for MappingId < "
                  + mapping.getId() + " >, Error occurred while extracting values from backing bean" + " < "
                  + expression + ":" + value + " >", e);
      }

      return result;
   }

}
