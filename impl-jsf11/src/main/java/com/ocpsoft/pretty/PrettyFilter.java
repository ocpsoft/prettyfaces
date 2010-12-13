package com.ocpsoft.pretty;

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
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyConfigurator;
import com.ocpsoft.pretty.util.FacesContextBuilder;
import com.ocpsoft.pretty.util.FacesMessagesUtils;
import com.ocpsoft.pretty.util.HTTPDecoder;

public class PrettyFilter implements Filter
{
    private static final Log log = LogFactory.getLog(PrettyFilter.class);

    public static final String CONFIG_FILES_ATTR = "com.ocpsoft.pretty.CONFIG_FILES";
    private final FacesContextBuilder facesContextBuilder = new FacesContextBuilder();
    private final FacesMessagesUtils messagesUtils = new FacesMessagesUtils();
    private static final HTTPDecoder decoder = new HTTPDecoder();

    private ServletContext servletContext;

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException
    {
        String requestURI = getRequestURI(request);
        if (getConfig().isURLMapped(requestURI))
        {
            PrettyContext prettyContext = PrettyContext.newInstance((HttpServletRequest) request);
            FacesContext facesContext = initFacesContext(request, response);
            prettyContext.injectParameters();
            prettyContext.computeDynamicViewId();
            String viewId = prettyContext.getCurrentCalculatedViewId();
            messagesUtils.saveMessages(facesContext);
            facesContext.release();

            log.info("Forwarding mapped request [" + requestURI + "] to JSF viewId [" + viewId + "]");
            if (!response.isCommitted())
            {
                request.getRequestDispatcher(viewId).forward(request, response);
            }
        }
        else
        {
            ensurePopulatedContext(request);
            log.debug("Request is not mapped using PrettyFaces. Continue.");
            chain.doFilter(request, response);
        }
    }

    private void ensurePopulatedContext(final ServletRequest request)
    {
        PrettyContext.getCurrentInstance((HttpServletRequest) request);
    }

    public PrettyConfig getConfig()
    {
        return (PrettyConfig) servletContext.getAttribute(CONFIG_FILES_ATTR);
    }

    private void setConfig(final PrettyConfig config)
    {
        log.trace("Setting config into ServletContext");
        servletContext.setAttribute(CONFIG_FILES_ATTR, config);
    }

    private FacesContext initFacesContext(final ServletRequest request, final ServletResponse response)
    {
        log.trace("Building FacesContext");
        FacesContext facesContext = facesContextBuilder.getFacesContext(request, response);
        return facesContext;
    }

    private String getRequestURI(final ServletRequest request)
    {
        String result = ((HttpServletRequest) request).getRequestURI();
        String contextPath = ((HttpServletRequest) request).getContextPath();

        result = decoder.decode(result);
        if (result.startsWith(contextPath))
        {
            result = result.substring(contextPath.length());
        }
        return result;
    }

    /**
     * Load and cache configurations
     */
    public void init(final FilterConfig filterConfig) throws ServletException
    {
        final PrettyConfigurator configurator = new PrettyConfigurator(filterConfig.getServletContext());

        try
        {
            log.info("PrettyFilter starting up...");
            PrettyConfig config = configurator.configure();
            servletContext = filterConfig.getServletContext();
            setConfig(config);
            log.info("PrettyFilter initialized.");
        }
        catch (IOException ioex)
        {
            throw new ServletException("Error configuring Pretty Faces, could not load the configuration.", ioex);
        }
        catch (SAXException saxex)
        {
            throw new ServletException("Error configuring Pretty Faces, could not parse the configuration.", saxex);
        }
    }

    public void destroy()
    {}
}