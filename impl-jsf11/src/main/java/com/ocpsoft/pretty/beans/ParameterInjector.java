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
