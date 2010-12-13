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
import java.util.Map;

import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.config.PrettyUrlMapping;
import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.util.FacesElUtils;
import com.ocpsoft.pretty.util.UrlPatternParser;

/**
 * @author lb3
 */
public class ParameterInjector
{
    private static final FacesElUtils elUtils = new FacesElUtils();

    public void injectParameters(final FacesContext context)
    {
        PrettyContext prettyContext = PrettyContext.getCurrentInstance();

        String url = prettyContext.stripContextPath(prettyContext.getOriginalUri());
        for (PrettyUrlMapping mapping : prettyContext.getConfig().getMappings())
        {
            UrlPatternParser parser = new UrlPatternParser(mapping.getPattern());
            if (parser.matches(url))
            {
                injectParams(context, url, mapping, parser);
                injectQueryParams(context, mapping);
            }
        }
    }

    private void injectQueryParams(final FacesContext context, final PrettyUrlMapping mapping)
    {
        List<QueryParameter> params = mapping.getQueryParams();
        if (params != null)
        {
            for (QueryParameter param : params)
            {
                String name = param.getName();
                String el = param.getExpression();
                try
                {
                    if (elUtils.getExpectedType(context, el).isArray())
                    {
                        String[] values = (String[]) context.getExternalContext().getRequestParameterValuesMap().get(
                                name);
                        elUtils.setValue(context, el, values);
                    }
                    else
                    {
                        String value = (String) context.getExternalContext().getRequestParameterMap().get(name);
                        elUtils.setValue(context, el, value);
                    }
                }
                catch (Exception e)
                {
                    throw new PrettyException("PrettyFaces: Exception occurred while processing mapping<"
                            + mapping.getId() + ":" + el + "> for query parameter named<" + name + "> "
                            + e.getMessage(), e);
                }
            }
        }
    }

    private void injectParams(final FacesContext context, final String url, final PrettyUrlMapping mapping,
            final UrlPatternParser parser)
    {
        Map<String, String> params = parser.getMappedParameters(url);
        if (params != null)
        {
            for (Map.Entry<String, String> param : params.entrySet())
            {
                String el = param.getKey();
                String value = param.getValue();
                try
                {
                    elUtils.setValue(context, el, value);
                }
                catch (Exception e)
                {
                    throw new PrettyException("PrettyFaces: Exception occurred while processing <" + mapping.getId()
                            + ":" + el + "> for URL <" + url + ">", e);
                }
            }
        }
    }

}
