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
package com.ocpsoft.pretty.faces.beans;

import java.util.List;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.mapping.UrlAction;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.FacesElUtils;
import com.ocpsoft.pretty.faces.util.FacesMessagesUtils;
import com.ocpsoft.pretty.faces.util.FacesStateUtils;
import com.ocpsoft.pretty.faces.util.PhaseIdComparator;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class ActionExecutor
{
   private static final Log log = LogFactory.getLog(ActionExecutor.class);
   private static final FacesElUtils elUtils = new FacesElUtils();
   private final FacesMessagesUtils mu = new FacesMessagesUtils();

   public void executeActions(final FacesContext context, final PhaseId currentPhaseId, final UrlMapping mapping)
   {
      List<UrlAction> actions = mapping.getActions();
      for (UrlAction action : actions)
      {
         if (shouldExecute(action, currentPhaseId, FacesStateUtils.isPostback(context)))
         {
            try
            {
               PrettyContext prettyContext = PrettyContext.getCurrentInstance();
               log.trace("Invoking method: " + action + ", on request: " + prettyContext.getRequestURL());
               Object result = elUtils.invokeMethod(context, action.getAction().getELExpression());
               if (result != null)
               {
                  mu.saveMessages(context, context.getExternalContext().getSessionMap());
                  String outcome = result.toString();
                  if (!"".equals(outcome))
                  {
                     NavigationHandler handler = context.getApplication().getNavigationHandler();
                     handler.handleNavigation(context, prettyContext.getCurrentViewId(), outcome);
                  }
                  return;
               }
            }
            catch (Exception e)
            {
               throw new PrettyException("Exception occurred while processing <" + mapping.getId() + ":" + action.getAction() + "> " + e.getMessage(), e);
            }
         }
      }
   }

   boolean shouldExecute(final UrlAction action, final PhaseId currentPhaseId, final boolean isPostback)
   {
      boolean result = false;
      if (PhaseIdComparator.equals(action.getPhaseId(), currentPhaseId) || PhaseIdComparator.equals(action.getPhaseId(), PhaseId.ANY_PHASE))
      {
         if (action.onPostback())
         {
            result = true;
         }
         else
         {
            if (!isPostback)
            {
               result = true;
            }
         }
      }
      return result;
   }
}
