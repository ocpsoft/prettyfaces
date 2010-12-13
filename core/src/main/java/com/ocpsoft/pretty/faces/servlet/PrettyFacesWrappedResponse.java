/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2010 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
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

package com.ocpsoft.pretty.faces.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIParameter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.rewrite.RewriteEngine;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.util.PrettyURLBuilder;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyFacesWrappedResponse extends HttpServletResponseWrapper
{
   private final RewriteEngine rewriteEngine = new RewriteEngine();

   private final PrettyConfig prettyConfig;

   private final String contextPath;

   public PrettyFacesWrappedResponse(final String contextPath, final HttpServletResponse response, final PrettyConfig config)
   {
      super(response);
      this.contextPath = contextPath;
      this.prettyConfig = config;
   }

   @Override
   @SuppressWarnings("deprecation")
   public String encodeRedirectUrl(final String url)
   {
      return super.encodeRedirectUrl(url);
   }

   @Override
   public String encodeRedirectURL(final String url)
   {
      return super.encodeRedirectURL(url);
   }

   @Override
   @SuppressWarnings("deprecation")
   public String encodeUrl(final String url)
   {
      return super.encodeUrl(url);
   }

   @Override
   public String encodeURL(final String url)
   {
      String result = rewritePrettyMappings(url);

      result = rewrite(result);

      return super.encodeURL(result);
   }

   private String rewritePrettyMappings(final String url)
   {
      String result = url;

      if (url != null)
      {
         String strippedUrl = stripContextPath(url);

         List<UrlMapping> matches = new ArrayList<UrlMapping>();
         for (UrlMapping m : prettyConfig.getMappings())
         {
            if (!"".equals(m.getViewId()) && strippedUrl.startsWith(m.getViewId()))
            {
               matches.add(m);
            }
         }

         Collections.sort(matches, UrlMapping.ORDINAL_COMPARATOR);

         Iterator<UrlMapping> iterator = matches.iterator();
         while (iterator.hasNext())
         {
            UrlMapping m = iterator.next();

            if (m.isOutbound())
            {
               List<UIParameter> uiParams = new ArrayList<UIParameter>();

               QueryString qs = QueryString.build("");
               if (url.contains("?"))
               {
                  qs.addParameters(url);
               }
               Map<String, String[]> queryParams = qs.getParameterMap();

               List<PathParameter> pathParams = m.getPatternParser().getPathParameters();

               int pathParamsFound = 0;
               for (PathParameter p : pathParams)
               {
                  UIParameter uip = new UIParameter();
                  String[] values = queryParams.get(p.getName());
                  if ((values != null) && (values.length > 0))
                  {
                     String value = values[0];
                     uip.setValue(value);
                     if ((value != null) && !"".equals(value))
                     {
                        pathParamsFound++;
                     }
                  }
                  queryParams.remove(p.getName());
                  uiParams.add(uip);
               }

               for (Entry<String, String[]> entry : queryParams.entrySet())
               {
                  UIParameter uip = new UIParameter();
                  uip.setName(entry.getKey());
                  uip.setValue(entry.getValue());
                  uiParams.add(uip);
               }

               if (pathParams.size() == pathParamsFound)
               {
                  PrettyURLBuilder builder = new PrettyURLBuilder();
                  result = contextPath + builder.build(m, uiParams);
                  break;
               }
            }
         }
      }
      return result;
   }

   private String rewrite(final String url)
   {
      String result = "";
      if (url != null)
      {
         String strippedUrl = stripContextPath(url);

         if (!strippedUrl.equals(url))
         {
            result = contextPath;
         }

         try
         {
            for (RewriteRule c : prettyConfig.getGlobalRewriteRules())
            {
               strippedUrl = rewriteEngine.processOutbound(c, strippedUrl);
            }
            result += strippedUrl;
         }
         catch (Exception e)
         {
            throw new PrettyException("Error occurred during canonicalization of request <[" + url + "]>", e);
         }
      }
      return result;
   }
   
   /**
    * If the given URL is prefixed with this request's context-path, return the
    * URI without the context path. Otherwise return the URI unchanged.
    */
   private String stripContextPath(String uri)
   {
      if (uri.startsWith(contextPath))
      {
         uri = uri.substring(contextPath.length());
      }
      return uri;
   }   
}
