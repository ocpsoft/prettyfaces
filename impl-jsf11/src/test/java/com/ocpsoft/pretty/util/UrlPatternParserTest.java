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
package com.ocpsoft.pretty.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/**
 * @author lb3
 */
public class UrlPatternParserTest
{
    UrlPatternParser parser = new UrlPatternParser(
            "/project/#{paramsBean.project}/#{paramsBean.project}/#{paramsBean.story}");

    @Test
    public void testUrlPatternParser()
    {
        assertTrue(parser instanceof UrlPatternParser);
    }

    @Test
    public void testMatches()
    {
        assertTrue(parser.matches("/project/starfish1/starfish2/story1"));
        assertFalse(parser.matches("project/starfish1/starfish2/story1"));
        assertFalse(parser.matches("/project/starfish1/starfish2/story1/"));
        assertFalse(parser.matches("project/starfish1/starfish2/story1/test"));
        assertFalse(parser.matches("project/starfish2/story1"));
        assertFalse(parser.matches("project/starfish1/starfish2"));
        assertFalse(parser.matches("project/starfish1/starfish2/"));
    }

    @Test
    public void testGetMappedParameters()
    {
        Map<String, String> params = parser.getMappedParameters("/project/starfish1/starfish2/story1");
        assertEquals(2, params.size());
    }

    @Test
    public void testGetMappedUrl()
    {
        String mappedUrl = parser.getMappedUrl("p1", 22, 55);
        assertEquals("/project/p1/22/55", mappedUrl);
    }

    @Test
    public void testSetUrlPattern()
    {
        UrlPatternParser parser = new UrlPatternParser(
                "/project/#{paramsBean.project}/#{paramsBean.project}/#{paramsBean.story}");
        assertEquals(3, parser.getParameterCount());
        parser.setUrlPattern("/public/home");
        assertEquals(0, parser.getParameterCount());
    }

    @Test
    public void testGetParameterCount()
    {
        assertEquals(3, parser.getParameterCount());
    }

}
