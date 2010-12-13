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
