package com.ocpsoft.pretty.faces.config.mapping;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

public class PathParameterTest
{
    @Test
    public void testIsNamedFalseWhenNameNull()
    {
        PathParameter parameter = new PathParameter();
        assertFalse(parameter.isNamed());
    }

    @Test
    public void testIsNamedFalseWhenNameEmpty()
    {
        PathParameter parameter = new PathParameter();
        parameter.setName("");
        assertFalse(parameter.isNamed());
    }

    @Test
    public void testIsNamedTrueWhenNameSet()
    {
        PathParameter parameter = new PathParameter();
        parameter.setName("name");
        assertTrue(parameter.isNamed());
    }
}
