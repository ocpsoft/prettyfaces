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

import javax.faces.event.PhaseId;

/**
 * @author lb3
 */
public class UrlAction
{
    private String action;
    private PhaseId phaseId = PhaseId.RESTORE_VIEW;
    private boolean onPostback = true;

    public UrlAction()
    {}

    public UrlAction(final String action)
    {
        this.action = action;
    }

    public UrlAction(final String action, final PhaseId phaseId)
    {
        this.action = action;
        this.phaseId = phaseId;
    }

    public PhaseId getPhaseId()
    {
        return phaseId;
    }

    public boolean onPostback()
    {
        return onPostback;
    }

    public void setOnPostback(final boolean onPostback)
    {
        this.onPostback = onPostback;
    }

    public void setPhaseId(final String phaseId)
    {
        if ("ANY_PHASE".equals(phaseId))
        {
            this.phaseId = PhaseId.ANY_PHASE;
        }
        else if ("APPLY_REQUEST_VALUES".equals(phaseId))
        {
            this.phaseId = PhaseId.APPLY_REQUEST_VALUES;
        }
        else if ("PROCESS_VALIDATIONS".equals(phaseId))
        {
            this.phaseId = PhaseId.PROCESS_VALIDATIONS;
        }
        else if ("UPDATE_MODEL_VALUES".equals(phaseId))
        {
            this.phaseId = PhaseId.UPDATE_MODEL_VALUES;
        }
        else if ("INVOKE_APPLICATION".equals(phaseId))
        {
            this.phaseId = PhaseId.INVOKE_APPLICATION;
        }
        else if ("RENDER_RESPONSE".equals(phaseId))
        {
            this.phaseId = PhaseId.RENDER_RESPONSE;
        }
        else
        {
            this.phaseId = PhaseId.RESTORE_VIEW;
        }
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(final String action)
    {
        this.action = action;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((phaseId == null) ? 0 : phaseId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof UrlAction))
        {
            return false;
        }
        UrlAction other = (UrlAction) obj;
        if (action == null)
        {
            if (other.action != null)
            {
                return false;
            }
        }
        else if (!action.equals(other.action))
        {
            return false;
        }
        if (phaseId == null)
        {
            if (other.phaseId != null)
            {
                return false;
            }
        }
        else if (!phaseId.equals(other.phaseId))
        {
            return false;
        }
        return true;
    }

}
