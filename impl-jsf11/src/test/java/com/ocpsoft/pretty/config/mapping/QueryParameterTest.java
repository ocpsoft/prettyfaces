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
package com.ocpsoft.pretty.config.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.ocpsoft.pretty.config.mapping.QueryParameter;

/**
 * @author lb3
 */
public class QueryParameterTest
{
    @Test
    public void testHashCodeAndEquals()
    {
        QueryParameter param1 = new QueryParameter("foo", "bar");
        QueryParameter param2 = new QueryParameter("foo", "bar");
        QueryParameter param3 = new QueryParameter("foo", "bar2");
        QueryParameter param4 = new QueryParameter("foo2", "bar");

        assertNotSame(param1.hashCode(), param3.hashCode());
        assertNotSame(param1.hashCode(), param4.hashCode());
        assertNotSame(param3.hashCode(), param4.hashCode());
        assertEquals(param1.hashCode(), param2.hashCode());

        assertNotSame(param1, param3);
        assertNotSame(param1, param4);
        assertNotSame(param3, param4);
        assertEquals(param1, param2);
    }

    @Test
    public void testQueryParameterStringString()
    {
        QueryParameter param1 = new QueryParameter("foo", "bar");
        assertEquals("foo", param1.getName());
        assertEquals("bar", param1.getExpression());
    }
}
