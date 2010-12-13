package com.ocpsoft.pretty.event;

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
 * along with this program. If not, see the file COPYING.LESSER or visit the GNU
 * website at <http://www.gnu.org/licenses/>.
 */
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
