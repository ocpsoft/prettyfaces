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
package com.ocpsoft.pretty.config;

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
