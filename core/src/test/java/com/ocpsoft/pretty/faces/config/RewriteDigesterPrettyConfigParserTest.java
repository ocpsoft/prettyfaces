package com.ocpsoft.pretty.faces.config;

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
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.faces.config.DigesterPrettyConfigParser;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.rewrite.Case;
import com.ocpsoft.pretty.faces.config.rewrite.Redirect;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.config.rewrite.TrailingSlash;

public class RewriteDigesterPrettyConfigParserTest
{
    private static final String CONFIG_PATH = "rewrite-pretty-config.xml";
    private PrettyConfig config;

    @Before
    public void configure() throws IOException, SAXException
    {
        final PrettyConfigBuilder builder = new PrettyConfigBuilder();
        new DigesterPrettyConfigParser().parse(builder, getClass().getClassLoader().getResourceAsStream(CONFIG_PATH));
        config = builder.build();
    }

    @Test
    public void testDefaultRewriteValues() throws Exception
    {
        RewriteRule c = new RewriteRule();
        assertEquals("", c.getMatch());
        assertEquals("", c.getSubstitute());
        assertEquals("", c.getUrl());
        assertEquals(Redirect.PERMANENT, c.getRedirect());
        assertEquals(true, c.isOutbound());
        assertEquals(Case.IGNORE, c.getToCase());
        assertEquals(TrailingSlash.IGNORE, c.getTrailingSlash());
    }

    @Test
    public void testParseRewriteEntries() throws Exception
    {
        List<RewriteRule> rules = config.getGlobalRewriteRules();
        RewriteRule r = rules.get(0);
        assertEquals("^(.*[^/])$", r.getMatch());
        assertEquals("$1/", r.getSubstitute());
        assertEquals(Redirect.CHAIN, r.getRedirect());
        assertTrue(r.isOutbound());
        assertEquals(Case.IGNORE, r.getToCase());
        assertEquals(TrailingSlash.IGNORE, r.getTrailingSlash());

        r = rules.get(1);
        assertEquals("", r.getMatch());
        assertEquals("", r.getSubstitute());
        assertEquals(Redirect.PERMANENT, r.getRedirect());
        assertEquals(true, r.isOutbound());
        assertEquals(Case.LOWERCASE, r.getToCase());
        assertEquals(TrailingSlash.APPEND, r.getTrailingSlash());

        r = rules.get(2);
        assertEquals("", r.getMatch());
        assertEquals("", r.getSubstitute());
        assertEquals("http://www.google.com", r.getUrl());
        assertEquals(Redirect.TEMPORARY, r.getRedirect());
        assertEquals(false, r.isOutbound());
        assertEquals(Case.UPPERCASE, r.getToCase());
        assertEquals(TrailingSlash.REMOVE, r.getTrailingSlash());
    }

    @Test
    public void testParse()
    {
        UrlMapping mapping = config.getMappingById("0");

        assertEquals("0", mapping.getId());
        assertEquals("/project/#{pid:viewProjectBean.projectId}/", mapping.getPattern());
        assertEquals("/faces/viewProject.xhtml", mapping.getViewId());
    }

}
