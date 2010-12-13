package com.ocpsoft.pretty.faces.application;

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
import java.util.Map;
import java.util.Set;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.util.FacesNavigationURLCanonicalizer;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyNavigationHandler extends ConfigurableNavigationHandler
{
   private static final Log log = LogFactory.getLog(PrettyNavigationHandler.class);

   private final ConfigurableNavigationHandler parent;
   private final PrettyRedirector pr = PrettyRedirector.getInstance();

   public PrettyNavigationHandler(final ConfigurableNavigationHandler parent)
   {
      this.parent = parent;
   }

   @Override
   public void handleNavigation(final FacesContext context, final String fromAction, final String outcome)
   {
      log.debug("Navigation requested: fromAction [" + fromAction + "], outcome [" + outcome + "]");
      if (!pr.redirect(context, outcome))
      {
         log.debug("Not a PrettyFaces navigation string - passing control to default nav-handler");
         PrettyContext prettyContext = PrettyContext.getCurrentInstance();
         prettyContext.setInNavigation(true);

         String originalViewId = context.getViewRoot().getViewId();
         parent.handleNavigation(context, fromAction, outcome);
         String newViewId = context.getViewRoot().getViewId();

         /*
          * Navigation is complete if the viewId has not changed or the response
          * is complete
          */
         if ((true == context.getResponseComplete()) || originalViewId.equals(newViewId))
         {

            prettyContext.setInNavigation(false);
         }
      }

   }

   @Override
   public NavigationCase getNavigationCase(final FacesContext context, final String fromAction, final String outcome)
   {
      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      PrettyConfig config = prettyContext.getConfig();
      if ((outcome != null) && PrettyContext.PRETTY_PREFIX.equals(outcome))
      {
         String viewId = context.getViewRoot().getViewId();
         NavigationCase navigationCase = parent.getNavigationCase(context, fromAction, viewId);
         return navigationCase;
      }
      else if ((outcome != null) && outcome.startsWith(PrettyContext.PRETTY_PREFIX) && config.isMappingId(outcome))
      {
         /*
          * FIXME this will not work with dynamic view IDs... figure out another
          * solution (<rewrite-view>/faces/views/myview.xhtml</rewrite-view> ?
          * For now. Do not support it.
          */
         String viewId = config.getMappingById(outcome).getViewId();
         String normalizedViewId = FacesNavigationURLCanonicalizer.normalizeRequestURI(context, viewId);
         NavigationCase navigationCase = parent.getNavigationCase(context, fromAction, normalizedViewId);
         return navigationCase;
      }
      else
      {
         NavigationCase navigationCase = parent.getNavigationCase(context, fromAction, outcome);
         return navigationCase;
      }
   }

   @Override
   public Map<String, Set<NavigationCase>> getNavigationCases()
   {
      return parent.getNavigationCases();
   }

   @Override
   public void performNavigation(final String outcome)
   {
      parent.performNavigation(outcome);
   }
}