package com.ocpsoft.pretty.faces.config;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.URL;

public class PrettyConfigTest
{
   private static PrettyConfig config = new PrettyConfig();
   private static UrlMapping mapping = new UrlMapping();
   private static UrlMapping mapping1 = new UrlMapping();
   private static UrlMapping mapping2 = new UrlMapping();

   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
      List<UrlMapping> mappings = new ArrayList<UrlMapping>();
      mapping.setId("testid");
      mapping.setPattern("/home/en/#{testBean.someProperty}/");
      mapping.setViewId("/faces/view.jsf");
      mappings.add(mapping);

      mapping1.setId("testid2");
      mapping1.setPattern("/home/en/#{testBean.someProperty2}/");
      mapping1.setViewId("/faces/view.jsf");
      mappings.add(mapping1);

      mapping2.setId("testid2");
      mapping2.setPattern("/home/en/#{testBean.someProperty2}/");
      mapping2.setViewId("/faces/view.jsf");
      mappings.add(mapping2);

      config.setMappings(mappings);
   }

   @Test
   public void testGetMappingById()
   {
      UrlMapping mapping2 = config.getMappingById("testid");
      assertEquals(mapping, mapping2);
   }

   @Test
   public void testGetMappingByNullIdReturnsNull()
   {
      UrlMapping mapping2 = config.getMappingById(null);
      assertEquals(null, mapping2);
   }

   @Test
   public void testGetMappingForUrl()
   {
      UrlMapping mapping2 = config.getMappingForUrl(new URL("/home/en/test/"));
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
      assertTrue(config.isURLMapped(new URL("/home/en/test/")));
      assertFalse(config.isViewMapped("/home/en/notmapped/okthen"));
   }

}
