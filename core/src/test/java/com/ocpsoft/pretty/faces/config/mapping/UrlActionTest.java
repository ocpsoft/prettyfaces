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
package com.ocpsoft.pretty.faces.config.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;

/**
 * @author lb3
 */
public class UrlActionTest
{

   @Test
   public void testUrlActionSetsDefaultPhaseIdAndEmptyAction()
   {
      UrlAction urlAction = new UrlAction();
      assertNull(urlAction.getAction());
      assertEquals(PhaseId.RESTORE_VIEW, urlAction.getPhaseId());
   }

   @Test
   public void testUrlActionStringSetsActionMethod()
   {
      String action = "#{this.is.my.action}";
      UrlAction urlAction = new UrlAction(action);
      assertEquals(action, urlAction.getAction().getELExpression());
   }

   @Test
   public void testUrlActionStringPhaseId()
   {
      String action = "#{this.is.my.action}";
      UrlAction urlAction = new UrlAction(action, PhaseId.APPLY_REQUEST_VALUES);
      assertEquals(action, urlAction.getAction().getELExpression());
      assertEquals(PhaseId.APPLY_REQUEST_VALUES, urlAction.getPhaseId());
   }

}
