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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class PrettyConfigTest
{
   private static PrettyConfig config = new PrettyConfig();
   private static PrettyUrlMapping mapping = new PrettyUrlMapping();
   private static PrettyUrlMapping mapping1 = new PrettyUrlMapping();
   private static PrettyUrlMapping mapping2 = new PrettyUrlMapping();

   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
      mapping.setId("testid");
      mapping.setPattern("/home/en/#{testBean.someProperty}/");
      mapping.setViewId("/faces/view.jsf");
      config.addMapping(mapping);

      mapping1.setId("testid2");
      mapping1.setPattern("/home/en/#{testBean.someProperty2}/");
      mapping1.setViewId("/faces/view.jsf");
      config.addMapping(mapping1);

      mapping2.setId("testid2");
      mapping2.setPattern("/home/en/#{testBean.someProperty2}/");
      mapping2.setViewId("/faces/view.jsf");
      config.addMapping(mapping2);
   }

   @Test
   public void testGetMappingById()
   {
      PrettyUrlMapping mapping2 = config.getMappingById("testid");
      assertEquals(mapping, mapping2);
   }

   @Test
   public void testGetMappingByNullIdReturnsNull()
   {
      PrettyUrlMapping mapping2 = config.getMappingById(null);
      assertEquals(null, mapping2);
   }

   @Test
   public void testGetMappingForUrl()
   {
      PrettyUrlMapping mapping2 = config.getMappingForUrl("/home/en/test/");
      assertEquals(mapping, mapping2);
   }

   @Test
   public void isViewMapped() throws Exception
   {
      assertTrue(config.isViewMapped("/faces/view.jsf"));
      assertFalse(config.isViewMapped("/faces/view2.jsf"));
   }

   @Test
   public void isNullViewMappedReturnsFalse() throws Exception
   {
      assertFalse(config.isViewMapped(null));
   }

   @Test
   public void testIsURLMapped() throws Exception
   {
      assertTrue(config.isURLMapped("/home/en/test/"));
      assertFalse(config.isViewMapped("/home/en/notmapped/okthen"));
   }

}
