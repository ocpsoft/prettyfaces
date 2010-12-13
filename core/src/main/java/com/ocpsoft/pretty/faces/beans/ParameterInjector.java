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
         injectQueryParams(context, mapping);
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

   private void injectQueryParams(final FacesContext context, final UrlMapping mapping)
   {
      boolean isPostback = FacesStateUtils.isPostback(context);
      List<QueryParameter> params = mapping.getQueryParams();
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
            if (context.getExternalContext().getRequestParameterMap().containsKey(name))
            {
               try
               {
                  if (elUtils.getExpectedType(context, el).isArray())
                  {
                     String[] values = context.getExternalContext().getRequestParameterValuesMap().get(name);
                     elUtils.setValue(context, el, values);
                  }
                  else
                  {
                     String value = context.getExternalContext().getRequestParameterMap().get(name);
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
