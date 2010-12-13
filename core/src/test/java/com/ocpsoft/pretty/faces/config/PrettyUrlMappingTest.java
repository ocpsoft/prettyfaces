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
 * Public License along with this program. If not, see the file COPYING.LESSER3
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.PathValidator;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.url.URLPatternParser;

/**
 * @author lb3
 */
public class PrettyUrlMappingTest
{
   UrlMapping m = new UrlMapping();

   public PrettyUrlMappingTest()
   {
      m.setId("foo");
      m.setPattern("/project/#{pb.name}");
      m.addPathValidator(new PathValidator(0, "val1 val2 val3", "#{handler.handle}"));
      m.addPathValidator(new PathValidator(2, "val4 val5", "#{handler.handle}"));
   }

   @Test
   public void testGetPatternParser()
   {
      URLPatternParser parser = m.getPatternParser();
      assertEquals(1, parser.getParameterCount());
   }

   @Test
   public void testGetValidatorsIdsForPathParam()
   {

      PathParameter p = new PathParameter();
      p.setPosition(0);

      List<PathValidator> validators = m.getValidatorsForPathParam(p);
      assertEquals(1, validators.size());

      PathParameter p2 = new PathParameter();
      p2.setPosition(2);

      validators = m.getValidatorsForPathParam(p2);
      assertEquals(1, validators.size());
   }

   @Test
   public void testMatches()
   {
      assertTrue(m.matches(new URL("/project/scrumshark")));
      assertFalse(m.matches(new URL("/project/foo/bar")));
   }

}
