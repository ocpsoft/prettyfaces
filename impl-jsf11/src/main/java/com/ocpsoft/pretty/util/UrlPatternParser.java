package com.ocpsoft.pretty.util;

/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see the file COPYING.LESSER or visit the GNU website at
 * <http://www.gnu.org/licenses/>.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ocpsoft.pretty.PrettyException;

public class UrlPatternParser
{
    private String originalPattern;
    private String urlPattern;

    private final List<String> expressions = new ArrayList<String>();

    public UrlPatternParser(final String pattern)
    {
        setUrlPattern(pattern);
    }

    /**
     * Return true of this parser matches the given URL, otherwise, return
     * false.
     */
    public boolean matches(final String url)
    {
        return Pattern.compile(urlPattern).matcher(url).matches();
    }

    /**
     * Builds a Map of Expression,Value pairs for this UrlPattern, extracted
     * from the provided URL string. This does not return duplicate entries,
     * hence, if an el expression is used twice in the same patter, those two or
     * more entries will be stored as one entry in this Map.
     */
    public Map<String, String> getMappedParameters(final String url)
    {
        Map<String, String> result = new HashMap<String, String>();
        Matcher matcher = Pattern.compile(urlPattern).matcher(url);
        if (matcher.matches())
        {
            for (int i = 0; i < expressions.size(); i++)
            {
                String el = expressions.get(i);
                String value = matcher.group(i + 1);
                result.put(el, value);
            }
        }
        return result;
    }

    /**
     * @param params
     *            Array of Object parameters, in order, to be substituted for el
     *            expressions. This method will call the toString() method on
     *            each object provided.
     *            <p>
     *            If only one param is specified and it is an instance of List,
     *            the list items will be used as parameters instead. An empty
     *            list or a single null parameter are both treated as if no
     *            parameters were specified.
     *            </p>
     *            E.g: getMappedUrl(12,55,"foo","bar") for a pattern of
     *            /#{el.one}/#{el.two}/#{el.three}/#{el.four}/ will return the
     *            String: "/12/55/foo/bar/"
     * @return A URL based on this object's urlPatten, with values substituted
     *         for el expressions in the order provided
     */
    public String getMappedUrl(final Object... params)
    {
        StringBuffer result = new StringBuffer();
        if (params != null)
        {
            Matcher matcher = FacesElUtils.elPattern.matcher(originalPattern);
            Object[] parameters = params;

            if ((params.length == 1) && (params[0] != null) && (params[0] instanceof List))
            {
                List list = ((List) params[0]);
                if (list.size() == 0)
                {
            		parameters = new Object[0];
                } else
                {
                    parameters = list.toArray(params);
                }
            }
            else if ((params.length == 1) && (params[0] == null))
            {
            	parameters = new Object[0];
            }
            
            int i = 0;
            if (getParameterCount() != parameters.length)
            {
                throw new PrettyException("Invalid number of parameters supplied for pattern: " + originalPattern
                        + ", expected <" + getParameterCount() + ">, got <" + parameters.length + ">");
            }
            while (matcher.find())
            {
                matcher.appendReplacement(result, parameters[i].toString());
                i++;
            }
            matcher.appendTail(result);
        }
        else if (getParameterCount() > 0)
        {
            throw new PrettyException("Invalid number of parameters supplied: " + originalPattern + ", expected <"
                    + getParameterCount() + ">, got <0>");
        }

        return result.toString();
    }

    /**
     * Set the pattern for which this parser will match. Find and replace all el
     * expressions with regular expressions to extract values from parsed URLs
     * 
     * @param urlPattern
     *            Pattern to use as a parse template
     */
    public void setUrlPattern(final String urlPattern)
    {
        originalPattern = urlPattern;
        expressions.clear();
        Matcher matcher = FacesElUtils.elPattern.matcher(urlPattern);
        while (matcher.find())
        {
            expressions.add(matcher.group());
        }
        this.urlPattern = matcher.replaceAll("([^/]+)");
    }

    /**
     * Get the number of URL parameters that this parser expects to find in any
     * given input string
     * 
     * @return Number of parameters
     */
    public int getParameterCount()
    {
        return expressions.size();
    }
}