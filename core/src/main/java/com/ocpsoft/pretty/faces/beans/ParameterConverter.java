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

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ocpsoft.pretty.faces.config.mapping.PathConverter;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.NullComponent;
import com.ocpsoft.pretty.faces.util.StringUtils;

/**
 * 
 * This class is used to perform the conversion for path and query parameters. The JSF converter used for the conversion
 * will be either the default converter for the type or the user-specified converter defined for the
 * {@link QueryParameter} or {@link PathParameter}.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 * 
 */
public class ParameterConverter
{

   private final FacesContext facesContext;

   private final String customConverterId;

   /**
    * Creates a {@link ParameterConverter} for the supplied query parameter.
    */
   public ParameterConverter(FacesContext facesContext, QueryParameter queryParameter)
   {
      this.facesContext = facesContext;
      this.customConverterId = StringUtils.trimToNull(queryParameter.getConverterId());
   }

   /**
    * Creates a {@link ParameterConverter} for the supplied path parameter.
    */
   public ParameterConverter(FacesContext facesContext, UrlMapping mapping, PathParameter pathParameter)
   {
      this.facesContext = facesContext;
      PathConverter pathConverter = mapping.getPathConverterForPathParam(pathParameter);
      if (pathConverter != null) {
         customConverterId = StringUtils.trimToNull(pathConverter.getConverterId());
      }
      else {
         customConverterId = null;
      }
   }

   /**
    * Converters the string value into the corresponding object. Returns <code>null</code> if the value is
    * <code>null</code>.
    */
   public Object getAsObject(Class<?> type, String value)
   {

      // return null for null values
      if (value == null) {
         return null;
      }

      // create the converter for the type
      Converter converter = createConverter(type);

      // return the converted value
      if (converter != null) {
         return converter.getAsObject(facesContext, new NullComponent(), value);
      }
      else {
         return value;
      }

   }

   /**
    * Converts the supplied value into the string representation. Returns <code>null</code> if the value is
    * <code>null</code>.
    */
   public String getAsString(Object value)
   {

      // return null for null values
      if (value == null) {
         return null;
      }

      // create the converter for the type
      Converter converter = createConverter(value.getClass());

      // return the converted value
      if (converter != null) {
         return converter.getAsString(facesContext, new NullComponent(), value);
      }
      else {
         return value.toString();
      }

   }

   private Converter createConverter(Class<?> type)
   {

      Converter converter = null;

      // check for a custom converter
      if (StringUtils.isNotBlank(customConverterId)) {

         converter = facesContext.getApplication().createConverter(customConverterId);

         // fail if the converter does not exist
         if (converter == null) {
            throw new IllegalStateException("Cannot find JSF converter: " + customConverterId);
         }

      }

      // use the default converter
      else {
         converter = facesContext.getApplication().createConverter(type);
      }

      return converter;
   }
}
