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
package com.ocpsoft.pretty.faces.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

/**
 * @author lb3
 */
public class URLPatternParserNamedParameterTest
{
   URLPatternParser parser = new URLPatternParser("/project/#{1:paramsBean.project}/#{two:paramsBean.iteration}/#{3:paramsBean.story}");

   @Test
   public void testUrlPatternParser()
   {
      assertTrue(parser instanceof URLPatternParser);
   }

   @Test
   public void testGetNamedParameters()
   {
      List<PathParameter> params = parser.parse(new URL("/project/starfish1/sprint1/story1"));
      assertEquals(3, params.size());
      assertEquals("starfish1", params.get(0).getValue());
      assertEquals("sprint1", params.get(1).getValue());
      assertEquals("story1", params.get(2).getValue());

      assertEquals("1", params.get(0).getName());
      assertEquals("two", params.get(1).getName());
      assertEquals("3", params.get(2).getName());

      assertEquals("#{paramsBean.project}", params.get(0).getExpression().getELExpression());
      assertEquals("#{paramsBean.iteration}", params.get(1).getExpression().getELExpression());
      assertEquals("#{paramsBean.story}", params.get(2).getExpression().getELExpression());
   }
}
