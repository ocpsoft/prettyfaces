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
