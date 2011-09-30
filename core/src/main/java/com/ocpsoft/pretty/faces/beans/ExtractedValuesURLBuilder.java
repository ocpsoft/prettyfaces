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
import javax.faces.convert.Converter;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.mapping.PathConverter;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.url.URLPatternParser;
import com.ocpsoft.pretty.faces.util.FacesElUtils;
import com.ocpsoft.pretty.faces.util.NullComponent;
import com.ocpsoft.pretty.faces.util.StringUtils;

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
            // read value of the path parameter
            expression = injection.getExpression().getELExpression();
            value = elUtils.getValue(context, expression);
            if (value == null)
            {
               throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                        + mapping.getId() + " >, Required value " + " < " + expression + " > was null");
            }

            // search for a custom converter
            String customConverterId = null;
            PathConverter pathConverter = mapping.getPathConverterForPathParam(injection);
            if(pathConverter != null) {
               customConverterId = StringUtils.trimToNull(pathConverter.getConverterId());
            }

            // convert the value to the corresponding string
            String valueAsString = convertObjectToString(context, value, customConverterId);
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
            String customConverterId = StringUtils.trimToNull(injection.getConverterId());

            expression = injection.getExpression().getELExpression();
            value = elUtils.getValue(context, expression);

            if ((name != null) && (value != null))
            {
               if (value.getClass().isArray())
               {
                  Object[] values = (Object[]) value;
                  for (Object temp : values)
                  {
                     // convert the object to a string and add the query parameter
                     String valueAsString = convertObjectToString(context, temp, customConverterId);
                     queryParameterValues.add(new QueryParameter(name, valueAsString));
                  }
               }
               else
               {
                  // convert the object to a string and add the query parameter
                  String valueAsString = convertObjectToString(context, value, customConverterId);
                  queryParameterValues.add(new QueryParameter(name, valueAsString));
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
   
   /**
    * This method will convert the supplied object to its string representation. The method will either use the JSF
    * default converter for the type or use the custom converter (if supplied).
    * 
    * @param context the current {@link FacesContext}
    * @param value The object to convert
    * @param customConverterId An optional custom converter
    * @return the string representation or <code>null</code> if value is <code>null</code>
    */
   private String convertObjectToString(FacesContext context, Object value, String customConverterId)
   {

      // return null for null values
      if (value == null) {
         return null;
      }

      // use this converter for creating the string representation of the object
      Converter converter = null;

      // check for a custom converter
      if (StringUtils.isNotBlank(customConverterId)) {

         converter = context.getApplication().createConverter(customConverterId);

         // fail if the converter does not exist
         if (converter == null) {
            throw new IllegalStateException("Cannot find JSF converter: " + customConverterId);
         }

      }

      // use the default converter
      else {
         converter = context.getApplication().createConverter(value.getClass());
      }

      // return the converted value
      if (converter != null) {
         return converter.getAsString(context, new NullComponent(), value);
      }
      else {
         return value.toString();
      }

   }

}
