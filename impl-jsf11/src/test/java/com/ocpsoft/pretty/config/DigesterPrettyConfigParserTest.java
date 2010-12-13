package com.ocpsoft.pretty.config;

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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.faces.event.PhaseId;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.config.mapping.UrlAction;

public class DigesterPrettyConfigParserTest
{
    private static final String CONFIG_PATH = "mock-pretty-config.xml";
    private PrettyConfig config;

    @Before
    public void configure() throws IOException, SAXException
    {
        final PrettyConfigBuilder builder = new PrettyConfigBuilder();
        new DigesterPrettyConfigParser().parse(builder, getClass().getClassLoader().getResourceAsStream(CONFIG_PATH));
        config = builder.build();
    }

    @Test
    public void testParse()
    {
        PrettyUrlMapping mapping = config.getMappingById("0");

        assertEquals("0", mapping.getId());
        assertEquals("/project/#{viewProjectBean.projectId}/", mapping.getPattern());
        assertEquals("#{viewProjectBean.getPrettyTarget}", mapping.getViewId());

        List<UrlAction> actions = mapping.getActions();
        assertTrue(actions.contains(new UrlAction("#{viewProjectBean.load}")));
        assertTrue(actions.contains(new UrlAction("#{viewProjectBean.authorize}")));
    }

    @Test
    public void testParseWithPostbackAction() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("1");
        assertFalse(mapping.getActions().get(0).onPostback());
        assertTrue(mapping.getActions().get(1).onPostback());
    }

    @Test
    public void testParseActionDefaultsToPostbackTrue() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("2");
        assertTrue(mapping.getActions().get(0).onPostback());
        assertTrue(mapping.getActions().get(1).onPostback());
    }

    @Test
    public void testParseWithAnyPhaseAction() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("2");
        assertEquals(PhaseId.ANY_PHASE, mapping.getActions().get(1).getPhaseId());
    }

    @Test
    public void testParseWithPreRenderAction() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("3");
        assertEquals(PhaseId.RENDER_RESPONSE, mapping.getActions().get(1).getPhaseId());
    }

    @Test
    public void testParseWithMappedQueryParam() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("4");
        List<QueryParameter> params = mapping.getQueryParams();

        assertEquals(1, params.size());
        QueryParameter param = params.get(0);
        QueryParameter expected = new QueryParameter("user", "#{deleteUserBean.userName}");
        assertEquals(expected, param);
    }

    @Test
    public void testParseWithMappedQueryParamDefaultsToDecodeTrue() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("4");
        List<QueryParameter> params = mapping.getQueryParams();

        QueryParameter param = params.get(0);
        assertTrue(param.decode());
    }

    @Test
    public void testParseWithMappedQueryParamSetToDecodeFalse() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("5");
        List<QueryParameter> params = mapping.getQueryParams();

        QueryParameter param = params.get(0);
        assertFalse(param.decode());
    }

    @Test
    public void testParseWithMappedMultipleQueryParams() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("6");
        List<QueryParameter> params = mapping.getQueryParams();

        assertEquals(2, params.size());
        QueryParameter name = new QueryParameter("name", "#{searchUserBean.userName}");
        QueryParameter gender = new QueryParameter("gender", "#{searchUserBean.userGender}");
        assertArrayEquals(new Object[] { name, gender }, params.toArray());
    }

    @Test
    public void testParseWithNoQueryParams() throws Exception
    {
        PrettyUrlMapping mapping = config.getMappingById("7");
        List<QueryParameter> params = mapping.getQueryParams();
        assertEquals(0, params.size());
    }
}
