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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.RequestParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlAction;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.el.ConstantExpression;

public class MappingDigesterPrettyConfigParserTest
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
      UrlMapping mapping = config.getMappingById("0");

      assertEquals("0", mapping.getId());
      assertEquals("/project/#{viewProjectBean.projectId}/", mapping.getPattern());
      assertEquals("#{viewProjectBean.getPrettyTarget}", mapping.getViewId());

      List<UrlAction> actions = mapping.getActions();
      assertTrue(actions.contains(new UrlAction("#{viewProjectBean.load}")));
      assertTrue(actions.contains(new UrlAction("#{viewProjectBean.authorize}")));
   }

   @Test
   public void testOnPostbackDefaultsToTrue()
   {
      UrlMapping mapping = config.getMappingById("0");
      assertEquals(true, mapping.isOnPostback());
   }

   @Test
   public void testOnPostbackSetToFalse()
   {
      UrlMapping mapping = config.getMappingById("1");
      assertEquals(false, mapping.isOnPostback());
   }

   @Test
   public void testParseWithPostbackAction() throws Exception
   {
      UrlMapping mapping = config.getMappingById("1");
      assertFalse(mapping.getActions().get(0).onPostback());
      assertTrue(mapping.getActions().get(1).onPostback());
   }

   @Test
   public void testParseActionDefaultsToPostbackTrue() throws Exception
   {
      UrlMapping mapping = config.getMappingById("2");
      assertTrue(mapping.getActions().get(0).onPostback());
      assertTrue(mapping.getActions().get(1).onPostback());
   }

   @Test
   public void testParseWithAnyPhaseAction() throws Exception
   {
      UrlMapping mapping = config.getMappingById("2");
      assertEquals(PhaseId.ANY_PHASE, mapping.getActions().get(1).getPhaseId());
   }

   @Test
   public void testParseWithPreRenderAction() throws Exception
   {
      UrlMapping mapping = config.getMappingById("3");
      assertEquals(PhaseId.RENDER_RESPONSE, mapping.getActions().get(1).getPhaseId());
   }

   @Test
   public void testParseWithMappedQueryParam() throws Exception
   {
      UrlMapping mapping = config.getMappingById("4");
      List<QueryParameter> params = mapping.getQueryParams();

      assertEquals(1, params.size());
      RequestParameter param = params.get(0);
      RequestParameter expected = new QueryParameter("user", null, new ConstantExpression("#{deleteUserBean.userName}"));
      assertEquals(expected, param);
   }

   @Test
   public void testParseWithMappedMultipleQueryParams() throws Exception
   {
      UrlMapping mapping = config.getMappingById("6");
      List<QueryParameter> params = mapping.getQueryParams();

      assertEquals(2, params.size());
      RequestParameter name = new QueryParameter("name", null, new ConstantExpression("#{searchUserBean.userName}"));
      RequestParameter gender = new QueryParameter("gender", null, new ConstantExpression("#{searchUserBean.userGender}"));
      assertArrayEquals(new Object[] { name, gender }, params.toArray());
   }

   @Test
   public void testParseWithNoQueryParams() throws Exception
   {
      UrlMapping mapping = config.getMappingById("7");
      List<QueryParameter> params = mapping.getQueryParams();
      assertEquals(0, params.size());
   }

   @Test
   public void testParseWithPathValidators() throws Exception
   {
      UrlMapping mapping = config.getMappingById("validate");
      assertEquals("/validate/#{validationBean.pathInput}", mapping.getPattern());
      assertEquals(2, mapping.getPathValidators().size());
      assertEquals(0, mapping.getPathValidators().get(0).getIndex());
      assertEquals("validator1", mapping.getPathValidators().get(0).getValidatorIds());
      assertEquals("#{validationBean.handle}", mapping.getPathValidators().get(0).getOnError());
      assertEquals(null, mapping.getPathValidators().get(0).getValidatorExpression());

      assertEquals(1, mapping.getPathValidators().get(1).getIndex());
      assertEquals("validator2", mapping.getPathValidators().get(1).getValidatorIds());
      assertEquals("#{validationBean.handle2}", mapping.getPathValidators().get(1).getOnError());
      assertEquals("#{validationBean.validateMethod}", mapping.getPathValidators().get(1).getValidatorExpression().getELExpression());
      List<QueryParameter> params = mapping.getQueryParams();
      assertEquals(1, params.size());
   }

   @Test
   public void testParseWithQueryValidators() throws Exception
   {
      UrlMapping mapping = config.getMappingById("validate");
      List<QueryParameter> params = mapping.getQueryParams();
      assertEquals(1, params.size());

      QueryParameter p = params.get(0);
      assertEquals("validator1 validator2", p.getValidatorIds());
      assertEquals("pretty:demo", p.getOnError());
      assertEquals("p", p.getName());
      assertEquals("#{validationBean.validateMethod}", p.getValidatorExpression().getELExpression());

   }

   @Test
   public void testQueryParameterOnPostbackAttribute() throws Exception
   {
      UrlMapping mapping = config.getMappingById("8");
      List<QueryParameter> params = mapping.getQueryParams();
      assertEquals(3, params.size());

      assertEquals("withoutAttribute", params.get(0).getName());
      assertEquals(true, params.get(0).isOnPostback());

      assertEquals("attributeSetToFalse", params.get(1).getName());
      assertEquals(false, params.get(1).isOnPostback());

      assertEquals("attributeSetToTrue", params.get(2).getName());
      assertEquals(true, params.get(2).isOnPostback());

   }

}
