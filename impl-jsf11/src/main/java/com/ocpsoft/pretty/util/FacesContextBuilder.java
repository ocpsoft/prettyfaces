/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see the file COPYING.LESSER or visit the GNU website at
 * <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.util;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FacesContextBuilder
{
    public FacesContext getFacesContext(final ServletRequest request, final ServletResponse response)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null)
        {
            return facesContext;
        }

        FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder
                .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
                .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

        ServletContext servletContext = ((HttpServletRequest) request).getSession().getServletContext();
        facesContext = contextFactory.getFacesContext(servletContext, request, response, lifecycle);
        InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);

        return facesContext;
    }

    private abstract static class InnerFacesContext extends FacesContext
    {
        protected static void setFacesContextAsCurrentInstance(final FacesContext facesContext)
        {
            FacesContext.setCurrentInstance(facesContext);
        }
    }

}
