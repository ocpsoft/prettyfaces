/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see the file COPYING.LESSER or visit the GNU website at
 * <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.config.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.faces.event.PhaseId;

import org.junit.Test;

/**
 * @author lb3
 */
public class UrlActionTest
{

    @Test
    public void testUrlActionSetsDefaultPhaseIdAndNullAction()
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
        assertEquals(action, urlAction.getAction());
    }

    @Test
    public void testUrlActionStringPhaseId()
    {
        String action = "#{this.is.my.action}";
        UrlAction urlAction = new UrlAction(action, PhaseId.APPLY_REQUEST_VALUES);
        assertEquals(action, urlAction.getAction());
        assertEquals(PhaseId.APPLY_REQUEST_VALUES, urlAction.getPhaseId());
    }

    @Test
    public void testSetPhaseIdString()
    {
        UrlAction urlAction = new UrlAction();
        urlAction.setPhaseId("INVOKE_APPLICATION");
        assertEquals(PhaseId.INVOKE_APPLICATION, urlAction.getPhaseId());

        urlAction.setPhaseId("APPLY_REQUEST_VALUES");
        assertEquals(PhaseId.APPLY_REQUEST_VALUES, urlAction.getPhaseId());

        urlAction.setPhaseId("RENDER_RESPONSE");
        assertEquals(PhaseId.RENDER_RESPONSE, urlAction.getPhaseId());

        urlAction.setPhaseId("RESTORE_VIEW");
        assertEquals(PhaseId.RESTORE_VIEW, urlAction.getPhaseId());

        urlAction.setPhaseId("PROCESS_VALIDATIONS");
        assertEquals(PhaseId.PROCESS_VALIDATIONS, urlAction.getPhaseId());

        urlAction.setPhaseId("ANY_PHASE");
        assertEquals(PhaseId.ANY_PHASE, urlAction.getPhaseId());

        urlAction.setPhaseId("UPDATE_MODEL_VALUES");
        assertEquals(PhaseId.UPDATE_MODEL_VALUES, urlAction.getPhaseId());
    }

}
