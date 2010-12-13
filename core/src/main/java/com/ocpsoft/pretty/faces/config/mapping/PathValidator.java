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
 * Public License along with this program. If not, see the file COPYING.LESSER3
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ocpsoft.pretty.faces.el.ConstantExpression;
import com.ocpsoft.pretty.faces.el.PrettyExpression;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PathValidator
{
    private int index;
    private String validatorIds = "";
    private PrettyExpression validatorExpression = null;
    private String onError = "";

    public PathValidator()
    {}

    public PathValidator(final int index, final String validatorIds, final String onError)
    {
        this.index = index;
        this.validatorIds = validatorIds;
        this.onError = onError;
    }

    public boolean hasValidators()
    {
        return validatorIds.trim().length() > 0;
    }

    public List<String> getValidatorIdList()
    {
        List<String> result = new ArrayList<String>();
        if (hasValidators())
        {
            String[] ids = validatorIds.split(" ");
            Collections.addAll(result, ids);
        }
        return result;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(final int index)
    {
        this.index = index;
    }

    public String getValidatorIds()
    {
        return validatorIds;
    }

    public void setValidatorIds(final String validatorIds)
    {
        this.validatorIds = validatorIds;
    }

    public String getOnError()
    {
        return onError;
    }

    public void setOnError(final String onError)
    {
        this.onError = onError;
    }

    public PrettyExpression getValidatorExpression()
    {
       return validatorExpression;
    }

    public void setValidatorExpression(PrettyExpression validatorExpression)
    {
       this.validatorExpression = validatorExpression;
    }

    /**
     * Extra setter method creating a {@link ConstantExpression}.
     * Used only for Digester only.
     */
    public void setValidator(final String validator)
    {
       this.validatorExpression = new ConstantExpression(validator);
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (onError == null ? 0 : onError.hashCode());
        result = prime * result + index;
        result = prime * result + (validatorIds == null ? 0 : validatorIds.hashCode());
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
        if (getClass() != obj.getClass())
        {
            return false;
        }
        PathValidator other = (PathValidator) obj;
        if (onError == null)
        {
            if (other.onError != null)
            {
                return false;
            }
        }
        else if (!onError.equals(other.onError))
        {
            return false;
        }
        if (index != other.index)
        {
            return false;
        }
        if (validatorIds == null)
        {
            if (other.validatorIds != null)
            {
                return false;
            }
        }
        else if (!validatorIds.equals(other.validatorIds))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "PathValidator [index=" + index + ", onError=" + onError + ", validatorIds=" + validatorIds + "]";
    }
}
