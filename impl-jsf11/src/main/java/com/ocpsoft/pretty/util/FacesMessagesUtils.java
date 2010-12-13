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
package com.ocpsoft.pretty.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesMessagesUtils
{
    private static final String requestToken = "_PRETTYFACES_MESSAGES";

    @SuppressWarnings("unchecked")
    public int saveMessages(final FacesContext facesContext)
    {
        List<FacesMessage> messages = new ArrayList<FacesMessage>();
        for (Iterator<FacesMessage> iter = facesContext.getMessages(null); iter.hasNext();)
        {
            messages.add(iter.next());
            iter.remove();
        }

        if (messages.size() == 0)
        {
            return 0;
        }

        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        List<FacesMessage> existingMessages = (List<FacesMessage>) requestMap.get(requestToken);
        if (existingMessages != null)
        {
            existingMessages.addAll(messages);
        }
        else
        {
            requestMap.put(requestToken, messages);
        }
        return messages.size();
    }

    @SuppressWarnings("unchecked")
    public int restoreMessages(final FacesContext facesContext)
    {
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        List<FacesMessage> messages = (List<FacesMessage>) requestMap.remove(requestToken);

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