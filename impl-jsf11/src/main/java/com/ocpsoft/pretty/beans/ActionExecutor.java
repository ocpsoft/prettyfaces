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
package com.ocpsoft.pretty.beans;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.application.PrettyRedirector;
import com.ocpsoft.pretty.config.PrettyUrlMapping;
import com.ocpsoft.pretty.config.mapping.UrlAction;
import com.ocpsoft.pretty.util.FacesElUtils;
import com.ocpsoft.pretty.util.FacesStateUtils;

/**
 * @author lb3
 */
public class ActionExecutor
{
	private static final FacesElUtils elUtils = new FacesElUtils();
	private static final FacesStateUtils facesStateUtils = new FacesStateUtils();
	private final PrettyRedirector pr = PrettyRedirector.getInstance();

	public void executeActions(final FacesContext context, final PhaseId currentPhaseId, final PrettyUrlMapping mapping)
	{
		List<UrlAction> actions = mapping.getActions();
		for (UrlAction action : actions)
		{
			if (shouldExecute(action, currentPhaseId, facesStateUtils.isPostback()))
			{
				try
				{
					Object result = elUtils.invokeMethod(context, action.getAction());
					if (result != null)
					{
						String targetMappingId = result.toString();
						if (!"".equals(targetMappingId) && !pr.redirect(context, targetMappingId))
						{
							throw new PrettyException("Invalid mappingId returned: " + targetMappingId);
						}
						return;
					}
				}
				catch (Exception e)
				{
					throw new PrettyException("Exception occurred while processing <" + mapping.getId() + ":"
							+ action.getAction() + "> " + e.getMessage(), e);
				}
			}
		}
	}

	boolean shouldExecute(final UrlAction action, final PhaseId currentPhaseId, final boolean isPostback)
	{
		boolean result = false;
		if (currentPhaseId.equals(action.getPhaseId()) || PhaseId.ANY_PHASE.equals(action.getPhaseId()))
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
