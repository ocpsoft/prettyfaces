/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER3 or visit the
 * GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;
import com.ocpsoft.pretty.faces.config.mapping.UrlAction;

/**
 * @author lb3
 */
public class ActionExecutorTest
{
   ActionExecutor executor = new ActionExecutor();

   @Test
   public void testShouldExecuteOnPostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.ANY_PHASE);
      action.setOnPostback(true);
      assertTrue(executor.shouldExecute(action, javax.faces.event.PhaseId.RESTORE_VIEW, true));
   }

   @Test
   public void testShouldExecuteOnNonPostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.ANY_PHASE);
      action.setOnPostback(true);
      assertTrue(executor.shouldExecute(action, javax.faces.event.PhaseId.RESTORE_VIEW, false));
   }

   @Test
   public void testShouldExecuteOnPhaseNonPostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.RESTORE_VIEW);
      action.setOnPostback(true);
      assertTrue(executor.shouldExecute(action, javax.faces.event.PhaseId.RESTORE_VIEW, false));
   }

   @Test
   public void testShouldNotExecuteOnWrongPhaseNonPostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.RESTORE_VIEW);
      action.setOnPostback(true);
      assertFalse(executor.shouldExecute(action, javax.faces.event.PhaseId.APPLY_REQUEST_VALUES, false));
   }

   @Test
   public void testShouldExecuteOnPhasePostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.RESTORE_VIEW);
      action.setOnPostback(true);
      assertTrue(executor.shouldExecute(action, javax.faces.event.PhaseId.RESTORE_VIEW, true));
   }

   @Test
   public void testShouldNotExecuteOnWrongPhasePostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.RESTORE_VIEW);
      action.setOnPostback(true);
      assertFalse(executor.shouldExecute(action, javax.faces.event.PhaseId.APPLY_REQUEST_VALUES, true));
   }

   @Test
   public void testShouldNotExecuteOnPostbackFalse()
   {
      UrlAction action = new UrlAction("action", PhaseId.ANY_PHASE);
      action.setOnPostback(false);
      assertFalse(executor.shouldExecute(action, javax.faces.event.PhaseId.RESTORE_VIEW, true));
   }

   @Test
   public void testShouldExecuteOnPostbackFalseWhenNotPostback()
   {
      UrlAction action = new UrlAction("action", PhaseId.ANY_PHASE);
      action.setOnPostback(false);
      assertTrue(executor.shouldExecute(action, javax.faces.event.PhaseId.RESTORE_VIEW, false));
   }

}
