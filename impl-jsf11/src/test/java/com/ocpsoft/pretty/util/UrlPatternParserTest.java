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
