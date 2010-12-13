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
package com.ocpsoft.pretty.component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;
import com.ocpsoft.pretty.util.UrlPatternParser;

/**
 * @author lb3
 */
public class PrettyUrlBuilder
{
	@SuppressWarnings("unchecked")
	public List<UIParameter> extractParameters(final UIComponent component)
	{
		List<UIParameter> results = new ArrayList<UIParameter>();
		for (UIComponent child : (List<UIComponent>) component.getChildren())
		{
			if (child instanceof UIParameter)
			{
				results.add((UIParameter) child);
			}
		}
		return results;
	}

	public String buildMappedUrl(final UIComponent component)
	{
		String mappingId = (String) component.getAttributes().get("mappingId");
		if (mappingId == null)
		{
			throw new PrettyException("Mapping id was null when attempting to build URL for component: "
					+ component.toString() + " <" + component.getClientId(FacesContext.getCurrentInstance()) + ">");
		}
		PrettyContext prettyContext = PrettyContext.getCurrentInstance();
		PrettyConfig prettyConfig = prettyContext.getConfig();
		PrettyUrlMapping urlMapping = prettyConfig.getMappingById(mappingId);
		return buildMappedUrl(urlMapping, extractParameters(component));
	}

	public String buildMappedUrl(final PrettyUrlMapping urlMapping, final List<UIParameter> params)
	{
		if (urlMapping != null)
		{
			UrlPatternParser parser = new UrlPatternParser(urlMapping.getPattern());
			List<String> linkParams = new ArrayList<String>();
			StringBuffer sb = new StringBuffer();
			boolean paramWritten = false;

			if ((params.size() == 1)
					&& ((params.get(0).getValue() instanceof List) || (params.get(0).getValue() == null)))
			{
				return parser.getMappedUrl(params.get(0).getValue());
			}

			for (UIParameter parameter : params)
			{
				try
				{
					Object name = parameter.getName();
					Object value = parameter.getValue();

					if ((name != null) && (value != null))
					{
						String pv = value.toString();
						sb.append((paramWritten ? '&' : '?'));
						sb.append(URLEncoder.encode(name.toString(), "UTF-8"));
						sb.append('=');
						if ((pv != null) && (pv.length() != 0))
						{
							sb.append(URLEncoder.encode(pv, "UTF-8"));
						}
						paramWritten = true;
					}
					else if (value != null)
					{
						linkParams.add(URLEncoder.encode(value.toString(), "UTF-8"));
					}
				}
				catch (UnsupportedEncodingException e)
				{
					throw new PrettyException(e);
				}
			}
			return parser.getMappedUrl(linkParams.toArray()) + sb.toString();
		}
		return "";
	}

}
