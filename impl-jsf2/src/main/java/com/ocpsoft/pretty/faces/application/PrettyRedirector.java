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

package com.ocpsoft.pretty.faces.application;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.beans.ExtractedValuesURLBuilder;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyRedirector
{
   private static final Log log = LogFactory.getLog(PrettyRedirector.class);
   private final ExtractedValuesURLBuilder builder = new ExtractedValuesURLBuilder();

   public static PrettyRedirector getInstance()
   {
      return new PrettyRedirector();
   }

   public boolean redirect(final FacesContext context, final String action)
   {
      try
      {
         PrettyContext prettyContext = PrettyContext.getCurrentInstance();
         PrettyConfig config = prettyContext.getConfig();
         ExternalContext externalContext = context.getExternalContext();

         String contextPath = prettyContext.getContextPath();
         if (PrettyContext.PRETTY_PREFIX.equals(action) && prettyContext.isPrettyRequest())
         {
            URL url = prettyContext.getRequestURL();
            QueryString query = prettyContext.getRequestQueryString();

            String target = contextPath + url.encode().toURL() + query.toQueryString();
            log.trace("Refreshing requested page [" + url + "]");
            String redirectUrl = externalContext.encodeRedirectURL(target, null);
            externalContext.redirect(redirectUrl);
            return true;
         }
         else if (isPrettyNavigationCase(action))
         {
            UrlMapping mapping = config.getMappingById(action);
            if (mapping != null)
            {
               String url = contextPath + builder.buildURL(mapping).encode()
                        + builder.buildQueryString(mapping).toString();
               log.trace("Redirecting to mappingId [" + mapping.getId() + "], [" + url + "]");
               String redirectUrl = externalContext.encodeRedirectURL(url, null);
               externalContext.redirect(redirectUrl);
            }
            else
            {
               throw new PrettyException("PrettyFaces: Invalid mapping id supplied to navigation handler: " + action);
            }
            return true;
         }
      }
      catch (IOException e)
      {
         throw new RuntimeException("PrettyFaces: redirect failed for target: " + action, e);
      }
      return false;
   }

   public void send404(final FacesContext facesContext)
   {
      try
      {
         HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
         response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      catch (IOException e)
      {
         throw new PrettyException(e);
      }
   }

   private boolean isPrettyNavigationCase(final String action)
   {
      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      PrettyConfig config = prettyContext.getConfig();
      return (action != null) && config.isMappingId(action) && action.trim().startsWith(PrettyContext.PRETTY_PREFIX);
   }
}
