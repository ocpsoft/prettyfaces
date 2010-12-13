package com.ocpsoft.pretty.application;

/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER or visit the GNU
 * website at <http://www.gnu.org/licenses/>.
 */
import java.io.IOException;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.PrettyContext;

public class PrettyViewHandler extends ViewHandler
{
    protected ViewHandler parent;

    public PrettyViewHandler(final ViewHandler viewHandler)
    {
        super();
        parent = viewHandler;
    }

    @Override
    public Locale calculateLocale(final FacesContext facesContext)
    {
        return parent.calculateLocale(facesContext);
    }

    @Override
    public String calculateRenderKitId(final FacesContext facesContext)
    {
        return parent.calculateRenderKitId(facesContext);
    }

    @Override
    public UIViewRoot createView(final FacesContext context, final String arg1)
    {
        return parent.createView(context, arg1);
    }

    @Override
    public String getActionURL(final FacesContext context, final String viewId)
    {
        PrettyContext prettyContext = PrettyContext.getCurrentInstance();
        if (prettyContext.isPrettyRequest() && !prettyContext.isInNavigation() && viewId != null
                && viewId.equals(context.getViewRoot().getViewId()))
        {
            return context.getExternalContext().encodeActionURL(prettyContext.getCalculatedActionUri());
        }
        else
        {
            return parent.getActionURL(context, viewId);
        }
    }

    @Override
    public String getResourceURL(final FacesContext facesContext, final String arg1)
    {
        return parent.getResourceURL(facesContext, arg1);
    }

    @Override
    public void renderView(final FacesContext facesContext, final UIViewRoot arg1) throws IOException, FacesException
    {
        parent.renderView(facesContext, arg1);
    }

    @Override
    public UIViewRoot restoreView(final FacesContext facesContext, final String arg1)
    {
        return parent.restoreView(facesContext, arg1);
    }

    @Override
    public void writeState(final FacesContext facesContext) throws IOException
    {
        parent.writeState(facesContext);
    }

}