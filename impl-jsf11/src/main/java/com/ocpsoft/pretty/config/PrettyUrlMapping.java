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

import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.config.mapping.UrlAction;

public class PrettyUrlMapping
{
    private String id;
    private String viewId;
    private List<UrlAction> actions = new ArrayList<UrlAction>();
    private String pattern;
    private List<QueryParameter> queryParams = new ArrayList<QueryParameter>();

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getViewId()
    {
        return viewId;
    }

    public void setViewId(final String viewId)
    {
        this.viewId = viewId;
    }

    public List<UrlAction> getActions()
    {
        return actions;
    }

    public void setActions(final List<UrlAction> actions)
    {
        this.actions = actions;
    }

    public void addAction(final UrlAction action)
    {
        actions.add(action);
    }

    public String getPattern()
    {
        return pattern;
    }

    public void setPattern(final String pattern)
    {
        this.pattern = pattern;
    }

    public boolean addQueryParam(final QueryParameter param)
    {
        return queryParams.add(param);
    }

    public List<QueryParameter> getQueryParams()
    {
        return queryParams;
    }

    public void setQueryParams(final List<QueryParameter> queryParams)
    {
        this.queryParams = queryParams;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((viewId == null) ? 0 : viewId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof PrettyUrlMapping))
        {
            return false;
        }
        PrettyUrlMapping other = (PrettyUrlMapping) obj;
        if (viewId == null)
        {
            if (other.viewId != null)
            {
                return false;
            }
        }
        else if (!viewId.equals(other.viewId))
        {
            return false;
        }
        return true;
    }
}
