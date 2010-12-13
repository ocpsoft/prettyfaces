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
package com.ocpsoft.pretty.application;

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