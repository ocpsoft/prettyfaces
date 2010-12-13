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
package com.ocpsoft.pretty.event;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.beans.ActionExecutor;
import com.ocpsoft.pretty.config.PrettyUrlMapping;
import com.ocpsoft.pretty.util.FacesMessagesUtils;

public class PrettyPhaseListener implements PhaseListener
{
   private static final long serialVersionUID = 2345410822999587673L;
   private final FacesMessagesUtils messagesUtils = new FacesMessagesUtils();
   private final ActionExecutor executor = new ActionExecutor();

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   public void beforePhase(final PhaseEvent event)
   {
      if (!PhaseId.RESTORE_VIEW.equals(event.getPhaseId()))
      {
         processEvent(event);
      }
   }

   public void afterPhase(final PhaseEvent event)
   {
      if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId()))
      {
         FacesContext context = event.getFacesContext();
         messagesUtils.restoreMessages(context);
         processEvent(event);
      }
   }

   private void processEvent(final PhaseEvent event)
   {
      FacesContext context = event.getFacesContext();

      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      PrettyUrlMapping mapping = prettyContext.getCurrentMapping();
      if (mapping != null)
      {
         executor.executeActions(context, event.getPhaseId(), mapping);
      }
   }
}
