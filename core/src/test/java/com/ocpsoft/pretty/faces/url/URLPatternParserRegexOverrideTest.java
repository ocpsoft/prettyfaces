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

import java.util.List;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class URLPatternParserRegexOverrideTest
{

   @Test
   public void testRegexNamedParser() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/foo/#{ /(?!=admin)[^/]+/ named}/");
      List<PathParameter> params = parser.parse(new URL("/foo/love/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("love", p.getValue());
      assertEquals("named", p.getName());
      assertEquals("#{named}", p.getExpression().getELExpression());
      assertEquals("(?!=admin)[^/]+", p.getRegex());
   }

   @Test
   public void testMultiURLSegmentParsing() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/foo/#{ /.*/ named}/");
      List<PathParameter> params = parser.parse(new URL("/foo/love/again/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("love/again", p.getValue());
      assertEquals("named", p.getName());
      assertEquals("#{named}", p.getExpression().getELExpression());
      assertEquals(".*", p.getRegex());
   }

   @Test
   public void testLeadingTrailingSlashWithInternalSlashes() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/#{ /.*/ named}/");
      List<PathParameter> params = parser.parse(new URL("/foo/love/again/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("foo/love/again", p.getValue());
      assertEquals("named", p.getName());
      assertEquals("#{named}", p.getExpression().getELExpression());
      assertEquals(".*", p.getRegex());
   }

   @Test
   public void testMultiURLSegmentParsingInjected() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/foo/#{ /(\\\\d+/\\\\w+)/ inje.cted}/");
      List<PathParameter> params = parser.parse(new URL("/foo/2010/again/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("2010/again", p.getValue());
      assertEquals("com.ocpsoft.vP_0", p.getName());
      assertEquals("#{inje.cted}", p.getExpression().getELExpression());
      assertEquals("(\\\\d+/\\\\w+)", p.getRegex());
   }

   @Test
   public void testMultiURLSegmentParsingNamedNoTrailingSlash() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/foo/#{ /(\\\\d+/\\\\w+)/ inje.cted}/and-#{valued}");
      List<PathParameter> params = parser.parse(new URL("/foo/2010/again/and-avalue"));
      assertEquals(2, params.size());

      PathParameter p = params.get(1);
      assertEquals(1, p.getPosition());
      assertEquals("avalue", p.getValue());
      assertEquals("valued", p.getName());
      assertEquals("#{valued}", p.getExpression().getELExpression());
      assertEquals("[^/]+", p.getRegex());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRegexNamedParseInvalidURL() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/foo/#{ /(?!=admin)[^/]+/ named}/");
      parser.parse(new URL("/admin/love/"));
   }
}
