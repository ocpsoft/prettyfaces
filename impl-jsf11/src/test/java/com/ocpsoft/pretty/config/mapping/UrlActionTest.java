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
