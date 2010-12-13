package com.ocpsoft.pretty.config;

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
import java.util.ArrayList;
import java.util.List;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.util.UrlPatternParser;

public class PrettyConfig
{
    public static final String CONFIG_REQUEST_KEY = "pretty_CONFIG_REQUEST_KEY";
    private List<PrettyUrlMapping> mappings = new ArrayList<PrettyUrlMapping>();

    public List<PrettyUrlMapping> getMappings()
    {
        return mappings;
    }

    public void setMappings(final List<PrettyUrlMapping> mappings)
    {
        this.mappings = mappings;
    }

    public void addMapping(final PrettyUrlMapping mapping)
    {
        mappings.add(mapping);
    }

    public PrettyUrlMapping getMappingForUrl(final String url)
    {
        for (PrettyUrlMapping mapping : getMappings())
        {
            String urlPattern = mapping.getPattern();
            UrlPatternParser um = new UrlPatternParser(urlPattern);
            if (um.matches(url))
            {
                return mapping;
            }
        }
        return null;
    }

    public boolean isMappingId(final String action)
    {
        PrettyUrlMapping mapping = getMappingById(action);
        return mapping instanceof PrettyUrlMapping;
    }

    public boolean isURLMapped(final String url)
    {
        PrettyUrlMapping mapping = getMappingForUrl(url);
        return (mapping != null);
    }

    public boolean isViewMapped(String viewId)
    {
        if (viewId != null)
        {
            viewId = viewId.trim();
            PrettyUrlMapping needle = new PrettyUrlMapping();
            needle.setViewId(viewId);
            if (viewId.startsWith("/"))
            {
                if (getMappings().contains(needle))
                {
                    return true;
                }
                needle.setViewId(viewId.substring(1));
            }
            return getMappings().contains(needle);
        }
        return false;
    }

    /**
     * This method accepts a mapping id (with or without prefix)
     * 
     * @param id
     *            Mapping id
     * @return PrettyUrlMapping Corresponding mapping
     */
    public PrettyUrlMapping getMappingById(String id)
    {
        if (id != null)
        {
            if (id.startsWith(PrettyContext.PRETTY_PREFIX))
            {
                id = id.substring(PrettyContext.PRETTY_PREFIX.length());
            }
            for (PrettyUrlMapping mapping : getMappings())
            {
                if (mapping.getId().equals(id))
                {
                    return mapping;
                }
            }
        }
        return null;
    }
}
