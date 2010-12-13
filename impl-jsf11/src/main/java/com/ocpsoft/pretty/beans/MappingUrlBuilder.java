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
package com.ocpsoft.pretty.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.config.PrettyUrlMapping;
import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.util.FacesElUtils;
import com.ocpsoft.pretty.util.UrlPatternParser;

/**
 * @author lb3
 */
public class MappingUrlBuilder
{
    private static final FacesElUtils elUtils = new FacesElUtils();

    /**
     * For all required values of the given PrettyUrlMapping, extract values
     * from their mapped backing beans and create a URL based on the url-pattern
     * and any mapped query-parameters.
     * 
     * @param mapping
     *            Mapping for which to extract values and generate URL
     * @return The fully constructed URL
     */
    // TODO Test MappingUrlBuilder
    // TODO Divide into two methods, getURL and getQueryString
    public String getURL(final PrettyUrlMapping mapping)
    {
        UrlPatternParser um = new UrlPatternParser(mapping.getPattern());
        FacesContext context = FacesContext.getCurrentInstance();

        StringBuffer sb = new StringBuffer();
        String result = mapping.getPattern();
        String expression = "";
        Object value = null;
        try
        {
            Map<String, String> parameters = um.getMappedParameters(mapping.getPattern());
            for (Map.Entry<String, String> injection : parameters.entrySet())
            {
                expression = injection.getKey();
                value = elUtils.getValue(context, expression);
                if (value == null)
                {
                    throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                            + mapping.getId() + " >, Required value " + " < " + expression + " > was null");
                }
                value = URLEncoder.encode(value.toString(), "UTF-8");
                result = result.replace(expression, value.toString());
            }

            List<QueryParameter> queryParams = mapping.getQueryParams();
            boolean paramWritten = false;
            for (QueryParameter injection : queryParams)
            {
                String name = injection.getName();
                expression = injection.getExpression();
                value = elUtils.getValue(context, expression);
                if ((name != null) && (value != null))
                {
                    String pv = value.toString();
                    sb.append((paramWritten ? '&' : '?'));
                    sb.append(URLEncoder.encode(name.toString(), "UTF-8"));
                    sb.append('=');
                    if ((value != null) && (pv.length() != 0))
                    {
                        sb.append(URLEncoder.encode(pv, "UTF-8"));
                    }
                    paramWritten = true;
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                    + mapping.getId() + " >, Error occurred while attempting to encode value < " + expression + ":"
                    + value + " >", e);
        }
        catch (ELException e)
        {
            throw new PrettyException("PrettyFaces: Exception occurred while building URL for MappingId < "
                    + mapping.getId() + " >, Error occurred while extracting values from backing bean" + " < "
                    + expression + ":" + value + " >", e);
        }

        String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        return contextPath + result + sb.toString();
    }

}
