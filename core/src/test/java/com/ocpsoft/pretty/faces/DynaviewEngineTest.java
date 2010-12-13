package com.ocpsoft.pretty.faces;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.dynaview.DynaviewEngine;

public class DynaviewEngineTest
{
    private final DynaviewEngine dynaview = new DynaviewEngine();

    @Test
    public void testDefaultMapping() throws Exception
    {
        String mapping = "/";
        String viewId = dynaview.buildDynaViewId(mapping);
        assertEquals("/" + DynaviewEngine.DYNAVIEW + ".jsf", viewId);
    }

    @Test
    public void testExtensionMapping() throws Exception
    {
        String mapping = "*.faces";
        String viewId = dynaview.buildDynaViewId(mapping);
        assertEquals("/" + DynaviewEngine.DYNAVIEW + ".faces", viewId);
    }

    @Test
    public void testPathMapping() throws Exception
    {
        String mapping = "/faces/*";
        String viewId = dynaview.buildDynaViewId(mapping);
        assertEquals("/faces/" + DynaviewEngine.DYNAVIEW + ".jsf", viewId);
    }
}
