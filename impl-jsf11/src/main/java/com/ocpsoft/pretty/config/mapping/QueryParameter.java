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
package com.ocpsoft.pretty.config.mapping;

/**
 * @author lb3
 */
public class QueryParameter
{
    private String name;
    private String expression;
    private boolean decode = true;

    public QueryParameter()
    {}

    public QueryParameter(final String name, final String expression)
    {
        this.name = name;
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

    public String getExpression()
    {
        return expression;
    }

    public void setExpression(final String expression)
    {
        this.expression = expression;
    }

    public boolean decode()
    {
        return decode;
    }

    public void setDecode(final boolean decode)
    {
        this.decode = decode;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (!(obj instanceof QueryParameter))
        {
            return false;
        }
        QueryParameter other = (QueryParameter) obj;
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
}
