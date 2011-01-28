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
package com.ocpsoft.pretty;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigurator;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.rewrite.Redirect;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.rewrite.RewriteEngine;
import com.ocpsoft.pretty.faces.servlet.PrettyFacesWrappedRequest;
import com.ocpsoft.pretty.faces.servlet.PrettyFacesWrappedResponse;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyFilter implements Filter
{
   private static final Log log = LogFactory.getLog(PrettyFilter.class);

   private static final String REWRITE_OCCURRED_KEY = "com.ocpsoft.pretty.rewrite";

   private ServletContext servletContext;

   /**
    * Determine if the current request is mapped using PrettyFaces. If it is, process the pattern, storing parameters
    * into the request map, then forward the request to the specified viewId.
    */
   public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain)
            throws IOException, ServletException
   {
      HttpServletRequest request = (HttpServletRequest) req;

      HttpServletResponse response = new PrettyFacesWrappedResponse(request.getContextPath(), request,
               (HttpServletResponse) resp, getConfig());
      req.setAttribute(PrettyContext.CONFIG_KEY, getConfig());

      PrettyContext context = PrettyContext.newDetachedInstance(request);

      rewrite(request, response);

      if (resp.isCommitted())
      {
         log.trace("Rewrite occurred, reponse is committed - ending request.");
      }
      else
      {
         URL url = context.getRequestURL();
         if (getConfig().isURLMapped(url))
         {
            PrettyContext.setCurrentContext(request, context); // set

            String viewId = context.getCurrentViewId();
            if (!response.isCommitted())
            {
               if (context.shouldProcessDynaview())
               {
                  log.trace("Forwarding mapped request [" + url.toURL() + "] to dynaviewId [" + viewId + "]");
                  req.getRequestDispatcher(context.getDynaViewId()).forward(req, response);
               }
               else
               {
                  List<PathParameter> params = context.getCurrentMapping().getPatternParser().parse(url);
                  QueryString query = QueryString.build(params);

                  ServletRequest wrappedRequest = new PrettyFacesWrappedRequest(request,
                              query.getParameterMap());

                  log.trace("Sending mapped request [" + url.toURL() + "] to resource [" + viewId + "]");
                  if (url.decode().toURL().matches(viewId))
                  {
                     chain.doFilter(wrappedRequest, response);
                  }
                  else
                  {
                     req.getRequestDispatcher(viewId).forward(wrappedRequest, response);
                  }
               }
            }
         }
         else
         {
            log.trace("Request is not mapped using PrettyFaces. Continue.");
            chain.doFilter(req, response);
         }
      }
   }

   /**
    * Apply the given list of {@link RewriteRule}s to the URL (in order,) perform a redirect/forward if required.
    * Canonicalization is only invoked if it has not previously been invoked on this request. This method operates on
    * the requestUri, excluding contextPath.
    * 
    * @return True if forward/redirect occurred, false if not.
    */
   private void rewrite(final HttpServletRequest req, final HttpServletResponse resp)
   {
      /*
       * FIXME Refactor this horrible method.
       */
      if (!rewriteOccurred(req))
      {
         RewriteEngine rewriteEngine = new RewriteEngine();
         URL url = PrettyContext.getCurrentInstance(req).getRequestURL();

         try
         {

            String queryString = req.getQueryString();
            if ((queryString != null) && !"".equals(queryString))
            {
               queryString = "?" + queryString;
            }
            else if (queryString == null)
            {
               queryString = "";
            }

            // TODO test this now that query string is included in rewrites
            String originalUrl = url.toURL() + queryString;
            String newUrl = originalUrl;
            for (RewriteRule rule : getConfig().getGlobalRewriteRules())
            {
               if (rule.matches(newUrl))
               {
                  newUrl = rewriteEngine.processInbound(req, resp, rule, newUrl);
                  if (!Redirect.CHAIN.equals(rule.getRedirect()))
                  {
                     /*
                      * An HTTP redirect has been triggered; issue one if we have a url or if the current url has been
                      * modified.
                      */
                     String ruleUrl = rule.getUrl();
                     if (((ruleUrl == null) || "".equals(ruleUrl.trim())) && !originalUrl.equals(newUrl))
                     {
                        /*
                         * The current URL has been rewritten - do redirect
                         */

                        // search for the '?' character
                        String[] parts = newUrl.split("\\?", 2);

                        // build URL from everything before the '?'
                        URL encodedPath = new URL(parts[0]).encode();
                        encodedPath.setEncoding(url.getEncoding());

                        // no query parameters, just a plain URL
                        if ((parts.length < 2) || (parts[1] == null) || "".equals(parts[1]))
                        {
                           newUrl = encodedPath.toURL();
                        }
                        // we found query parameters, so we append them in encoded representation
                        else
                        {
                           newUrl = encodedPath.toURL() + QueryString.build(parts[1]).toQueryString();
                        }

                        // send redirect
                        String redirectURL = resp.encodeRedirectURL(req.getContextPath() + newUrl);
                        resp.setHeader("Location", redirectURL);
                        resp.setStatus(rule.getRedirect().getStatus());
                        resp.flushBuffer();
                        break;
                     }
                     else if ((ruleUrl != null) && !"".equals(ruleUrl.trim()))
                     {
                        /*
                         * This is a custom location - don't call encodeRedirectURL() and don't add context path, just
                         * redirect to the encoded URL
                         */
                        URL encodedNewUrl = new URL(newUrl).encode();
                        resp.setHeader("Location", encodedNewUrl.toURL());
                        resp.setStatus(rule.getRedirect().getStatus());
                        resp.setCharacterEncoding(url.getEncoding());
                        resp.flushBuffer();
                        break;
                     }
                  }
               }
            }

            if (!originalUrl.equals(newUrl) && !resp.isCommitted())
            {
               /*
                * The URL was modified, but no redirect occurred; forward instead.
                */
               req.getRequestDispatcher(newUrl).forward(req, resp);
            }

         }
         catch (Exception e)
         {
            throw new PrettyException("Error occurred during canonicalization of request <[" + url + "]>", e);
         }
         finally
         {
            setRewriteOccurred(req);
         }
      }
   }

   private void setRewriteOccurred(final ServletRequest req)
   {
      req.setAttribute(REWRITE_OCCURRED_KEY, true);
   }

   private boolean rewriteOccurred(final ServletRequest req)
   {
      return Boolean.TRUE.equals(req.getAttribute(REWRITE_OCCURRED_KEY));
   }

   public PrettyConfig getConfig()
   {
      if ((servletContext == null) || (servletContext.getAttribute(PrettyContext.CONFIG_KEY) == null))
      {
         log.warn("PrettyFilter is not registered in web.xml, but is registered with JSF "
                  + "Navigation and Action handlers -- this could cause unpredictable behavior.");
         return new PrettyConfig();
      }
      return (PrettyConfig) servletContext.getAttribute(PrettyContext.CONFIG_KEY);
   }

   /**
    * Load and cache configurations
    */
   public void init(final FilterConfig filterConfig) throws ServletException
   {
      log.info("PrettyFilter starting up...");
      servletContext = filterConfig.getServletContext();

      PrettyConfigurator configurator = new PrettyConfigurator(servletContext);
      configurator.configure();

      log.info("PrettyFilter initialized.");
   }

   public void destroy()
   {
      log.info("PrettyFilter shutting down...");
   }
}