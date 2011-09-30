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

import java.util.List;

import javax.faces.context.FacesContext;

import com.ocpsoft.logging.Logger;
import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.util.FacesElUtils;
import com.ocpsoft.pretty.faces.util.FacesStateUtils;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class ParameterInjector
{
   private static final Logger log = Logger.getLogger(ParameterInjector.class);
   private static final FacesElUtils elUtils = new FacesElUtils();

   public void injectParameters(final FacesContext context)
   {
      log.trace("Injecting parameters");
      PrettyContext prettyContext = PrettyContext.getCurrentInstance(context);
      URL url = prettyContext.getRequestURL();
      UrlMapping mapping = prettyContext.getConfig().getMappingForUrl(url);

      if (mapping != null)
      {
         injectPathParams(context, url, mapping);
         injectQueryParams(context, mapping, prettyContext);
      }
   }

   private void injectPathParams(final FacesContext context, final URL url, final UrlMapping mapping)
   {

      // skip path parameter injection due to onPostback attribute?
      if (!mapping.isOnPostback() && FacesStateUtils.isPostback(context))
      {
         return;
      }

      List<PathParameter> params = mapping.getPatternParser().parse(url);
      for (PathParameter param : params)
      {
         String el = param.getExpression().getELExpression();
         if ((el != null) && !"".equals(el.trim()))
         {
            try
            {

               // we may need the type of the referenced property
               Class<?> expectedType = elUtils.getExpectedType(context, el);
               
               // perform conversion
               ParameterConverter converter = new ParameterConverter(context, mapping, param);
               Object convertedValue = converter.getAsObject(expectedType, param.getValue());
               
               // write the property
               elUtils.setValue(context, el, convertedValue);
               
            }
            catch (Exception e)
            {
               throw new PrettyException("PrettyFaces: Exception occurred while processing <" + mapping.getId() + ":"
                        + el + "> for URL <" + url + ">", e);
            }
         }
      }
   }

   private void injectQueryParams(final FacesContext context, final UrlMapping mapping,
            final PrettyContext prettyContext)
   {
      boolean isPostback = FacesStateUtils.isPostback(context);
      List<QueryParameter> params = mapping.getQueryParams();
      QueryString queryString = prettyContext.getRequestQueryString();
      for (QueryParameter param : params)
      {
         // check if to skip this QueryParameter due to onPostback attribute
         if (!param.isOnPostback() && isPostback)
         {
            continue;
         }

         String el = param.getExpression().getELExpression();
         if ((el != null) && !"".equals(el.trim()))
         {
            String name = param.getName();
            if (queryString.getParameterMap().containsKey(name))
            {
               try
               {

                  // we may need the type of the referenced property
                  Class<?> expectedType = elUtils.getExpectedType(context, el);

                  // we support only String arrays at the moment
                  if (expectedType.isArray())
                  {
                     String[] values = queryString.getParameterValues(name);
                     elUtils.setValue(context, el, values);
                  }
                  else
                  {

                     // we process only one occurrence of the query parameter here
                     String valueAsString = queryString.getParameter(name);
                     
                     // perform conversion
                     ParameterConverter converter = new ParameterConverter(context, param);
                     Object convertedValue = converter.getAsObject(expectedType, valueAsString);

                     // write the property
                     elUtils.setValue(context, el, convertedValue);

                  }
               }
               catch (Exception e)
               {
                  throw new PrettyException(
                           "PrettyFaces: Exception occurred while processing mapping<" + mapping.getId() + ":" + el
                                    + "> for query parameter named<" + name + "> " + e.getMessage(), e);
               }
            }
         }
      }
   }

}
