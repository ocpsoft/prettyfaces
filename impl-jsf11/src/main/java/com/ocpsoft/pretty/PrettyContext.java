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

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.beans.MappingUrlBuilder;
import com.ocpsoft.pretty.beans.ParameterInjector;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyConfigurator;
import com.ocpsoft.pretty.config.PrettyUrlMapping;
import com.ocpsoft.pretty.util.FacesElUtils;

public class PrettyContext implements Serializable
{
   private static final Log log = LogFactory.getLog(PrettyContext.class);
   private static final long serialVersionUID = -4593906924975844541L;

   public static final String PRETTY_PREFIX = "pretty:";
   private static final String CONTEXT_REQUEST_KEY = "prettyContext";
   private static final ParameterInjector injector = new ParameterInjector();
   private static final FacesElUtils elUtils = new FacesElUtils();

   private PrettyConfig config;

   private final String originalUri;
   private final String originalQueryString;
   private final String contextPath;
   private String calculatedUri = "";
   private String calculatedQueryString = "";
   private String currentViewId = "";
   private PrettyUrlMapping currentMapping;

   private boolean inNavigation = false;

   /**
    * Must create instance through the initialize() method
    */
   private PrettyContext(final HttpServletRequest request)
   {
      currentViewId = "";
      contextPath = request.getContextPath();
      originalUri = request.getRequestURI();
      originalQueryString = request.getQueryString();
      calculatedUri = request.getRequestURI();
      calculatedQueryString = request.getQueryString();
   }

   /**
    * Get the current PrettyFaces context object, or construct a new one if it
    * does not yet exist for this request. (Delegates to FacesContext to
    * retrieve the current HttpServletRequest object)
    * 
    * @return current context instance
    */
   public static PrettyContext getCurrentInstance()
   {
      FacesContext context = FacesContext.getCurrentInstance();
      return getCurrentInstance((HttpServletRequest) context.getExternalContext().getRequest());
   }

   /**
    * Get the current PrettyFaces context object, or construct a new one if it
    * does not yet exist for this request.
    * 
    * @return current context instance
    */
   public static PrettyContext getCurrentInstance(final HttpServletRequest request)
   {
      PrettyContext prettyContext = (PrettyContext) request.getAttribute(CONTEXT_REQUEST_KEY);
      if (prettyContext instanceof PrettyContext)
      {
         log.trace("Retrieved PrettyContext from Request");
         return prettyContext;
      }
      else
      {
         return newInstance(request);
      }
   }

   /**
    * Package private -- only PrettyFilter should be calling this method -- it
    * overwrites existing contexts in Request object
    * 
    * @param request
    * @return
    */
   static PrettyContext newInstance(final HttpServletRequest request)
   {
      PrettyContext prettyContext;
      log.trace("PrettyContext not found in Request - building new instance");
      prettyContext = new PrettyContext(request);
      PrettyConfig prettyConfig = (PrettyConfig) request.getSession().getServletContext().getAttribute(
                PrettyFilter.CONFIG_FILES_ATTR);
      prettyContext.setConfig(prettyConfig);
      request.setAttribute(CONTEXT_REQUEST_KEY, prettyContext);
      return prettyContext;
   }

   /**
    * Parse the current URL and inject values into mapped parameter beans. This
    * should only occur once per request, and it should only happen before any
    * faces processing occurs.
    */
   void injectParameters()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      injector.injectParameters(facesContext);
   }

   /**
    * Calculate the Faces ViewId to which this request URI resolves. This method
    * will recursively call any dynamic mapping viewId functions as needed until
    * a String viewId is returned, or supplied by a static mapping.
    * <p>
    * <i>Note: Precondition - parameter injection must take place before
    * this</i>
    * <p>
    * <i>Postcondition - currentViewId is set to computed View Id</i>
    * 
    * @return JSF viewID to which this request resolves.
    */
   void computeDynamicViewId()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      String currentUri = stripContextPath(originalUri);
      PrettyUrlMapping urlMapping = getConfig().getMappingForUrl(currentUri);
      if (urlMapping != null)
      {
         String viewId = urlMapping.getViewId();
         if (viewId == null)
         {
            viewId = "";
         }
         while (elUtils.isEl(viewId))
         {
            Object viewResult = elUtils.invokeMethod(facesContext, viewId);
            if (viewResult == null)
            {
               viewId = "";
               break;
            }
            else
            {
               viewId = viewResult.toString();
            }

            if (getConfig().isMappingId(viewId))
            {
               urlMapping = getConfig().getMappingById(viewId);
               viewId = urlMapping.getViewId();
               calculatedUri = new MappingUrlBuilder().getURL(urlMapping);
               calculatedQueryString = null;
            }
         }
         if ("".equals(viewId))
         {
            log.debug("ViewId for mapping with id <" + urlMapping.getId() + "> was blank");
         }
         setCurrentMapping(urlMapping);
         setCurrentViewId(viewId);
      }
   }

   /**
    * Get the requested URI, including query string. This may be different from
    * the original URI if dynamic view ID navigation has occurred
    */
   public String getCalculatedActionUri()
   {
      String result = calculatedUri;
      if ((calculatedQueryString != null) && (calculatedQueryString.length() > 0))
      {
         result += "?" + calculatedQueryString;
      }
      return result;
   }

   /**
    * Return the original URL string, including context path and query string,
    * with which this request was created
    */
   public String getOriginalRequestUrl()
   {
      String result = originalUri;
      String queryString = originalQueryString;
      if ((queryString != null) && (queryString.length() > 0))
      {
         result += "?" + queryString;
      }
      return result;
   }

   /**
    * Determine if this request URL is mapped by PrettyFaces
    */
   public boolean isPrettyRequest()
   {
      return getConfig().isURLMapped(stripContextPath(originalUri));
   }

   /**
    * Return true if the given URL corresponds to one or more mappings in the
    * current configuration. Strips context path information if necessary.
    */
   public boolean isURLMapped(final String url)
   {
      return getConfig().isURLMapped(stripContextPath(url));
   }

   /**
    * If the given URL is prefixed with this request's context-path, return the
    * URI without the context path. Otherwise return the URI unchanged.
    */
   public String stripContextPath(String uri)
   {
      if (uri.startsWith(contextPath))
      {
         uri = uri.substring(contextPath.length());
      }
      return uri;
   }

   /**
    * Get the mapped URI for which Pretty was initialized. Does not include
    * query string. This may be different from the originalUri because it can be
    * generated through calculation of a dynamic viewId.
    */
   public String getCurrentCalculatedUri()
   {
      return calculatedUri;
   }

   /**
    * Get the pretty-config.xml configurations as loaded by
    * {@link PrettyConfigurator} (This can be dynamically manipulated at runtime
    * in order to change or add any mappings.
    */
   public PrettyConfig getConfig()
   {
      return config;
   }

   void setConfig(final PrettyConfig config)
   {
      this.config = config;
   }

   /**
    * Get the PrettyUrlMapping representing the current request.
    */
   public PrettyUrlMapping getCurrentMapping()
   {
      return currentMapping;
   }

   private void setCurrentMapping(final PrettyUrlMapping mapping)
   {
      currentMapping = mapping;
   }

   /**
    * Return the current viewId to which the current request will be forwarded
    * to JSF.
    */
   public String getCurrentCalculatedViewId()
   {
      return currentViewId;
   }

   private void setCurrentViewId(final String viewId)
   {
      currentViewId = viewId;
   }

   /**
    * Return the original URL (exclusing query-parameters) for which this
    * Context was created.
    */
   public String getOriginalUri()
   {
      return originalUri;
   }

   /**
    * Return whether or not this faces application is in the Navigation State
    */
   public boolean isInNavigation()
   {
      return inNavigation;
   }

   /**
    * Set whether or not to treat this request as if it is in the Navigation
    * State
    */
   public void setInNavigation(final boolean value)
   {
      inNavigation = value;
   }

}
