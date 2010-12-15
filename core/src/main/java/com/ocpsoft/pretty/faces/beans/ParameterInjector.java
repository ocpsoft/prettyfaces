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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
   private static final Log log = LogFactory.getLog(ParameterInjector.class);
   private static final FacesElUtils elUtils = new FacesElUtils();

   public void injectParameters(final FacesContext context)
   {
      log.trace("Injecting parameters");
      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
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
            String value = param.getValue();
            try
            {
               elUtils.setValue(context, el, value);
            }
            catch (Exception e)
            {
               throw new PrettyException("PrettyFaces: Exception occurred while processing <" + mapping.getId() + ":"
                        + el + "> for URL <" + url + ">", e);
            }
         }
      }
   }

   private void injectQueryParams(final FacesContext context, final UrlMapping mapping, final PrettyContext prettyContext)
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
                  if (elUtils.getExpectedType(context, el).isArray())
                  {
                     String[] values = queryString.getParameterValues(name);
                     elUtils.setValue(context, el, values);
                  }
                  else
                  {
                     String value = queryString.getParameter(name);
                     elUtils.setValue(context, el, value);
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
