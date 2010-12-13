package com.ocpsoft.pretty.application;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class MultiPageMessagesSupport implements PhaseListener
{

    private static final long serialVersionUID = 1250469273857785274L;
    private static final String sessionToken = "MULTI_PAGE_MESSAGES_SUPPORT";

    public PhaseId getPhaseId()
    {
        return PhaseId.ANY_PHASE;
    }

    /*
     * Check to see if we are "naturally" in the RENDER_RESPONSE phase. If we
     * have arrived here and the response is already complete, then the page is
     * not going to show up: don't display messages yet.
     */
    public void beforePhase(final PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();
        saveMessages(facesContext);

        if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId()))
        {
            if (!facesContext.getResponseComplete())
            {
                restoreMessages(facesContext);
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
            saveMessages(facesContext);
        }
    }

    @SuppressWarnings("unchecked")
    private int saveMessages(final FacesContext facesContext)
    {
        List<FacesMessage> messages = new ArrayList<FacesMessage>();
        for (Iterator<FacesMessage> iter = facesContext.getMessages(); iter.hasNext();)
        {
            messages.add(iter.next());
            iter.remove();
        }

        if (messages.size() == 0)
        {
            return 0;
        }

        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        List<FacesMessage> existingMessages = (List<FacesMessage>) sessionMap.get(sessionToken);
        if (existingMessages != null)
        {
            existingMessages.addAll(messages);
        }
        else
        {
            sessionMap.put(sessionToken, messages);
        }
        return messages.size();
    }

    @SuppressWarnings("unchecked")
    private int restoreMessages(final FacesContext facesContext)
    {
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        List<FacesMessage> messages = (List<FacesMessage>) sessionMap.remove(sessionToken);

        if (messages == null)
        {
            return 0;
        }

        int restoredCount = messages.size();
        for (Object element : messages)
        {
            facesContext.addMessage(null, (FacesMessage) element);
        }
        return restoredCount;
    }
}