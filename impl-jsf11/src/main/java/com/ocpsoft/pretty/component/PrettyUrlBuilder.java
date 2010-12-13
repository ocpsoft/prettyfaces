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
