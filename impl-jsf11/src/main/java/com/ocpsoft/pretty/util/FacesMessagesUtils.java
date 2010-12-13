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