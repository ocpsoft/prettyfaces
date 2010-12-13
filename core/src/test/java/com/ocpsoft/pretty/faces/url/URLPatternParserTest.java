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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class URLPatternParserTest
{
   @Test
   public void testSimplestMatch() throws Exception
   {
      URLPatternParser parser = new URLPatternParser("/");
      assertTrue(parser.matches(new URL("/")));
      List<PathParameter> parameters = parser.parse(new URL("/"));
      assertEquals(0, parameters.size());
   }

   @Test
   public void testGetNamedParameterList()
   {
      URLPatternParser namedParamParser = new URLPatternParser("/foo/#{named}/");
      List<PathParameter> params = namedParamParser.parse(new URL("/foo/love/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("love", p.getValue());
      assertEquals("named", p.getName());
      assertEquals("#{named}", p.getExpression().getELExpression());
   }

   @Test
   public void testGetInjectedParameterList()
   {
      URLPatternParser namedParamParser = new URLPatternParser("/foo/#{:injected}/");
      List<PathParameter> params = namedParamParser.parse(new URL("/foo/love/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("love", p.getValue());
      assertEquals("#{injected}", p.getExpression().getELExpression());
   }

   @Test
   public void testGetNamedValuedParameterList()
   {
      URLPatternParser namedValuedParamParser = new URLPatternParser("/foo/#{named:bean.value}/");
      List<PathParameter> params = namedValuedParamParser.parse(new URL("/foo/love/"));
      assertEquals(1, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("love", p.getValue());
      assertEquals("named", p.getName());
      assertEquals("#{bean.value}", p.getExpression().getELExpression());
   }

   @Test
   public void testMatches()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      assertTrue(parser.matches(new URL("/project/starfish1/starfish2/story1")));
      assertFalse(parser.matches(new URL("project/starfish1/starfish2/story1")));
      assertFalse(parser.matches(new URL("/project/starfish1/starfish2/story1/")));
      assertFalse(parser.matches(new URL("project/starfish1/starfish2/story1/test")));
      assertFalse(parser.matches(new URL("project/starfish2/story1")));
      assertFalse(parser.matches(new URL("project/starfish1/starfish2")));
      assertFalse(parser.matches(new URL("project/starfish1/starfish2/")));
   }

   @Test
   public void testMatchesPrefixedSegmentExpressionInjected()
   {
      URLPatternParser parser = new URLPatternParser("/project/#{paramsBean.project}/story-#{paramsBean.story}/");
      assertTrue(parser.matches(new URL("/project/starfish/story-1/")));
      assertFalse(parser.matches(new URL("/project/starfish/story-1/-")));
      assertFalse(parser.matches(new URL("/project/starfish/story-/")));
      assertFalse(parser.matches(new URL("project/starfish/story-23/")));
   }

   @Test
   public void testMatchesPrefixedSegmentExpressionNamed()
   {
      URLPatternParser parser = new URLPatternParser("/project/#{paramsBean.project}/story-#{story}/");
      assertTrue(parser.matches(new URL("/project/starfish/story-1/")));
   }

   @Test
   public void testMatchesMultipleExpressionNamedSegment()
   {
      URLPatternParser parser = new URLPatternParser("/project/#{paramsBean.project}/story-#{story}-#{comment}");
      assertTrue(parser.matches(new URL("/project/starfish/story-1-23")));
      assertFalse(parser.matches(new URL("/project/starfish/story-1-23/")));
   }

   @Test
   public void testMatchesPrefixedSegmentExpressionNamedInjected()
   {
      URLPatternParser parser = new URLPatternParser("/project/#{paramsBean.project}/story-#{story:paramsBean.story}/");
      assertTrue(parser.matches(new URL("/project/starfish/story-1/")));
   }

   @Test
   public void testGetMappedParameters()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      List<PathParameter> params = parser.parse(new URL("/project/starfish1/sprint1/story1"));
      assertEquals(3, params.size());
      assertEquals("starfish1", params.get(0).getValue());
      assertEquals("sprint1", params.get(1).getValue());
      assertEquals("story1", params.get(2).getValue());
   }

   @Test
   public void testGetMappedParametersDuplicatesAreRepresented()
   {
      URLPatternParser duplicateParamsParser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.project}/#{paramsBean.story}");
      List<PathParameter> params = duplicateParamsParser.parse(new URL("/project/starfish1/sprint1/story1"));
      assertEquals(3, params.size());
      assertEquals("starfish1", params.get(0).getValue());
      assertEquals("sprint1", params.get(1).getValue());
      assertEquals("story1", params.get(2).getValue());
   }

   @Test
   public void testGetMappedParameterList()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      List<PathParameter> params = parser.parse(new URL("/project/starfish1/sprint1/story1"));
      assertEquals(3, params.size());

      PathParameter p = params.get(0);
      assertEquals(0, p.getPosition());
      assertEquals("starfish1", p.getValue());
      assertEquals("#{paramsBean.project}", p.getExpression().getELExpression());

      PathParameter p1 = params.get(1);
      assertEquals(1, p1.getPosition());
      assertEquals("sprint1", p1.getValue());
      assertEquals("#{paramsBean.iteration}", p1.getExpression().getELExpression());

      PathParameter p2 = params.get(2);
      assertEquals(2, p2.getPosition());
      assertEquals("story1", p2.getValue());
      assertEquals("#{paramsBean.story}", p2.getExpression().getELExpression());
   }

   @Test
   public void testGetMappedUrl()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      String mappedUrl = parser.getMappedURL("p1", 22, 55).toURL();
      assertEquals("/project/p1/22/55", mappedUrl);
   }

   @Test
   public void testGetParameterCount()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      assertEquals(3, parser.getParameterCount());
   }

   @Test
   public void testGetMappedUrlWithList()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      List<Object> params = new ArrayList<Object>();
      params.add("p1");
      params.add(22);
      params.add(55);
      String mappedUrl = parser.getMappedURL(params).toURL();
      assertEquals("/project/p1/22/55", mappedUrl);
      params.remove(2);
      try
      {
         mappedUrl = parser.getMappedURL(params, 55).toURL();
         assertEquals("/project/p1/22/55", mappedUrl);
         assertTrue("Parameter count is wrong.", false);
      }
      catch (PrettyException pe)
      {
         assertTrue(true);
      }
      assertNotSame("/project/p1/22/55", parser.getMappedURL(params, 22, 55).toURL());
   }

   @Test
   public void testGetMappedUrlWithArray()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      Object[] params = new Object[3];
      params[0] = "p1";
      params[1] = 22;
      params[2] = 55;
      String mappedUrl = parser.getMappedURL(params).toURL();
      assertEquals("/project/p1/22/55", mappedUrl);
      params[2] = null;
      params[1] = null;
      try
      {
         mappedUrl = parser.getMappedURL(params, 55).toURL();
         assertEquals("/project/p1/22/55", mappedUrl);
         assertTrue("An exception should have been thrown by now", false);
      }
      catch (PrettyException pe)
      {
         assertTrue(true);
      }
      assertNotSame("/project/p1/22/55", parser.getMappedURL(params, 22, 55).toURL());
   }

   @Test
   public void testGetMappedUrlWithMap()
   {
      URLPatternParser parser = new URLPatternParser(
               "/project/#{paramsBean.project}/#{paramsBean.iteration}/#{paramsBean.story}");
      Map<String, String> params = new HashMap<String, String>();
      params.put("1", "p1");
      params.put("2", "22");
      params.put("3", "55");
      try
      {
         String mappedUrl = parser.getMappedURL(params).toURL();
         assertEquals("An exception should have been thrown: Map parameters are not supported at this time.", mappedUrl);
      }
      catch (PrettyException pe)
      {
         assertTrue(true);
      }
      assertNotSame("/project/p1/22/55", parser.getMappedURL(params, 22, 55).toURL());
   }

   @Test
   public void testGetMappedUrlWithListWithoutParams()
   {
      URLPatternParser noParamsParser = new URLPatternParser("/no/param");
      List<?> params = new ArrayList<Object>();
      String mappedUrl = noParamsParser.getMappedURL(params).toURL();
      assertEquals("Empty list failed", "/no/param", mappedUrl);
   }

   @Test
   public void testGetMappedUrlNullListWithoutParams()
   {
      URLPatternParser noParamsParser = new URLPatternParser("/no/param");
      List<?> params = null;
      String mappedUrl = noParamsParser.getMappedURL(params).toURL();
      assertEquals("Null param failed", "/no/param", mappedUrl);
   }

   @Test
   public void testGetMappedUrlWithRegexes()
   {
      URLPatternParser regexParser = new URLPatternParser("/(foo|bar|baz|cat|dog).jsf");
      List<PathParameter> params = regexParser.parse(new URL("/foo.jsf"));
   }
}
