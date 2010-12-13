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
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;

public class PrettyNavigationHandler extends NavigationHandler
{
    private static final Log log = LogFactory.getLog(PrettyNavigationHandler.class);

    private final NavigationHandler parent;

    public PrettyNavigationHandler(final NavigationHandler parent)
    {
        this.parent = parent;
    }

    @Override
    public void handleNavigation(final FacesContext context, final String fromAction, final String outcome)
    {
        log.debug("Navigation requested: fromAction [" + fromAction + "], outcome [" + outcome + "]");
        if (!PrettyRedirector.getInstance().redirect(context, outcome))
        {
            processFacesNavigation(context, fromAction, outcome);
        }
    }

    private void processFacesNavigation(final FacesContext context, final String fromAction, final String outcome)
    {
        PrettyContext prettyContext = PrettyContext.getCurrentInstance();
        log.debug("Not a PrettyFaces navigation string - passing control to default nav-handler");
        prettyContext.setInNavigation(true);

        String originalViewId = context.getViewRoot().getViewId();
        parent.handleNavigation(context, fromAction, outcome);
        String newViewId = context.getViewRoot().getViewId();

        if ((true == context.getResponseComplete()) || (originalViewId.equals(newViewId)))
        {
            // Navigation is complete if the viewId has not changed or the
            // response is complete
            prettyContext.setInNavigation(false);
        }
    }

}