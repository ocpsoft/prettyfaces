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
package com.ocpsoft.pretty.faces.application;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import com.ocpsoft.logging.Logger;
import com.ocpsoft.pretty.PrettyContext;

public class PrettyNavigationHandler extends NavigationHandler
{
   private static final Logger log = Logger.getLogger(PrettyNavigationHandler.class);

   private final NavigationHandler parent;
   private final PrettyRedirector pr = PrettyRedirector.getInstance();

   public PrettyNavigationHandler(final NavigationHandler parent)
   {
      this.parent = parent;
   }

   @Override
   public void handleNavigation(final FacesContext context, final String fromAction, final String outcome)
   {
      log.debug("Navigation requested: fromAction [" + fromAction + "], outcome [" + outcome + "]");
      if (!pr.redirect(context, outcome))
      {
         processFacesNavigation(context, fromAction, outcome);
      }
   }

   private void processFacesNavigation(final FacesContext context, final String fromAction, final String outcome)
   {
      PrettyContext prettyContext = PrettyContext.getCurrentInstance(context);
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