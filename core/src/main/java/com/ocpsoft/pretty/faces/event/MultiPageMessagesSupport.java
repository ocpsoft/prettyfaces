package com.ocpsoft.pretty.faces.event;

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
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.ocpsoft.pretty.faces.util.FacesMessagesUtils;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class MultiPageMessagesSupport implements PhaseListener
{

    private static final long serialVersionUID = 1250469273857785274L;
    private final FacesMessagesUtils messagesUtils = new FacesMessagesUtils();

    public PhaseId getPhaseId()
    {
        return PhaseId.ANY_PHASE;
    }

    public void beforePhase(final PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();
        messagesUtils.saveMessages(facesContext, facesContext.getExternalContext().getSessionMap());

        if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
        {
            /*
             * Check to see if we are "naturally" in the RENDER_RESPONSE phase.
             * If we have arrived here and the response is already complete,
             * then the page is not going to show up: don't display messages
             * yet.
             */
            if (!facesContext.getResponseComplete())
            {
                messagesUtils.restoreMessages(facesContext, facesContext.getExternalContext().getSessionMap());
            }
        }
    }

    /*
     * Save messages into the session after every phase.
     */
    public void afterPhase(final PhaseEvent event)
    {
        if (!PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
        {
            FacesContext facesContext = event.getFacesContext();
            messagesUtils.saveMessages(facesContext, facesContext.getExternalContext().getSessionMap());
        }
    }
}