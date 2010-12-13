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

package com.ocpsoft.pretty.faces.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class FacesMessagesUtils
{
    private static final String token = "com.ocpsoft.pretty.SAVED_FACES_MESSAGES";

    @SuppressWarnings("unchecked")
    public int saveMessages(final FacesContext facesContext, final Map<String, Object> destination)
    {
        int restoredCount = 0;
        if (FacesContext.getCurrentInstance() != null)
        {
            List<FacesMessage> messages = new ArrayList<FacesMessage>();
            for (Iterator<FacesMessage> iter = facesContext.getMessages(null); iter.hasNext();)
            {
                messages.add(iter.next());
                iter.remove();
            }

            if (messages.size() > 0)
            {
                List<FacesMessage> existingMessages = (List<FacesMessage>) destination.get(token);
                if (existingMessages != null)
                {
                    existingMessages.addAll(messages);
                }
                else
                {
                    destination.put(token, messages);
                }
                restoredCount = messages.size();
            }
        }
        return restoredCount;
    }

    @SuppressWarnings("unchecked")
    public int restoreMessages(final FacesContext facesContext, final Map<String, Object> source)
    {
        int restoredCount = 0;
        if (FacesContext.getCurrentInstance() != null)
        {
            List<FacesMessage> messages = (List<FacesMessage>) source.remove(token);

            if (messages == null)
            {
                return 0;
            }

            restoredCount = messages.size();
            for (Object element : messages)
            {
                facesContext.addMessage(null, (FacesMessage) element);
            }
        }
        return restoredCount;
    }
}