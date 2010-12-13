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

import com.ocpsoft.pretty.faces.el.ConstantExpression;
import com.ocpsoft.pretty.faces.el.PrettyExpression;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public abstract class RequestParameter
{
    private PrettyExpression expression;
    private String value = "";
    private String name = "";

    public RequestParameter()
    {}

    public RequestParameter(final String name, final String value)
    {
        super();
        this.name = name;
        this.value = value;
    }

    public RequestParameter(final String name, final String value, final PrettyExpression expression)
    {
        super();
        this.name = name;
        this.value = value;
        this.expression = expression;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public PrettyExpression getExpression()
    {
        return expression;
    }

    public String getValue()
    {
        return value;
    }

    public void setExpression(final PrettyExpression expression)
    {
        this.expression = expression;
    }

    /**
     * Extra setter method creating a {@link ConstantExpression}.
     * Used only for Digester only.
     */
    public void setExpression(final String expression)
    {
        this.expression = new ConstantExpression(expression);
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (expression == null ? 0 : expression.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
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
        RequestParameter other = (RequestParameter) obj;
        if (expression == null)
        {
            if (other.expression != null)
            {
                return false;
            }
        }
        else if (!expression.equals(other.expression))
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "RequestParameter [expression=" + expression + ", name=" + name + ", value=" + value + "]";
    }
}
