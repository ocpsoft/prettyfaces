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

         URLPatternParser parser = mapping.getPatternParser();
         List<PathParameter> parameters = parser.getPathParameters();
         List<String> parameterValues = new ArrayList<String>();
         for (PathParameter injection : parameters)
         {
            // read value of the path parameter
            expression = injection.getExpression().getELExpression();
            value = elUtils.getValue(context, expression);
            if (value == null)
            {
               throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                        + mapping.getId() + " >, Required value " + " < " + expression + " > was null");
            }

            // convert the value to the corresponding string
            ParameterConverter converter = new ParameterConverter(context, mapping, injection);
            String valueAsString = converter.getAsString(value);
            if (valueAsString == null)
            {
               throw new PrettyException("PrettyFaces: The converter returned null while converting the object <"
                        + value.toString() + ">!");
            }
            
            parameterValues.add(valueAsString);
            
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
            ParameterConverter converter = new ParameterConverter(context, injection);

            expression = injection.getExpression().getELExpression();
            value = elUtils.getValue(context, expression);

            if ((name != null) && (value != null))
            {
               if (value.getClass().isArray())
               {
                  Object[] values = (Object[]) value;
                  for (Object temp : values)
                  {
                     queryParameterValues.add(new QueryParameter(name, converter.getAsString(temp)));
                  }
               }
               else
               {
                  queryParameterValues.add(new QueryParameter(name, converter.getAsString(value)));
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
