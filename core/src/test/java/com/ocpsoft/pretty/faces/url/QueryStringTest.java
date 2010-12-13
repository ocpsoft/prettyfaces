package com.ocpsoft.pretty.faces.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.RequestParameter;

public class QueryStringTest
{
   @Test
   public void testCreateEmptyQueryString()
   {
      Map<String, String[]> params = new TreeMap<String, String[]>();

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();
      assertEquals("", queryString);

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }

   @Test
   public void testCreateStringQueryString() throws Exception
   {
      String query = "?bar=555&foo=hello&foo=friend";
      QueryString qs = QueryString.build(query);

      String result = qs.toQueryString();

      assertTrue(result.startsWith("?"));
      assertTrue(result.contains("bar=555"));
      assertTrue(result.contains("foo=hello"));
      assertTrue(result.contains("foo=friend"));
   }

   @Test
   public void testCreateStringQueryStringWithExtraPreCharacters() throws Exception
   {
      String query = "?bar=555&foo=hello&foo=friend";
      QueryString qs = QueryString.build("www.ocpsoft.com/" + query);

      String result = qs.toQueryString();

      assertTrue(result.startsWith("?"));
      assertTrue(result.contains("bar=555"));
      assertTrue(result.contains("foo=hello"));
      assertTrue(result.contains("foo=friend"));
   }

   @Test
   public void testCreateFromQuestionMarkYieldsEmptyQueryString() throws Exception
   {
      String query = "?";
      QueryString qs = QueryString.build(query);

      String result = qs.toQueryString();

      assertTrue(result.length() == 0);
   }

   @Test
   public void testCreateQueryString()
   {
      Map<String, String[]> params = new TreeMap<String, String[]>();
      params.put("p1", new String[] { "val1" });

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();

      assertEquals("?p1=val1", queryString);

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }

   @Test
   public void testCreateUnValuedQueryString()
   {
      Map<String, String[]> params = new TreeMap<String, String[]>();
      params.put("p1", null);

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();

      assertEquals("?p1", queryString);

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }

   @Test
   public void testCreateBlankValuedQueryString()
   {
      Map<String, String[]> params = new TreeMap<String, String[]>();
      params.put("p1", new String[] { "" });

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();

      assertEquals("?p1=", queryString);

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }

   @Test
   public void testCreateMultiValueQueryString()
   {
      Map<String, String[]> params = new TreeMap<String, String[]>();
      params.put("p1", new String[] { "val1", "val2" });

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();

      assertEquals("?p1=val1&p1=val2", queryString);

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }

   @Test
   public void testCreateMultiKeyedMultiValueQueryString()
   {
      Map<String, String[]> params = new TreeMap<String, String[]>();
      params.put("p1", new String[] { "val1", "val2" });
      params.put("p2", new String[] { "val3", "val4" });

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();

      assertTrue(queryString.startsWith("?"));
      assertTrue(queryString.contains("p1=val1"));
      assertTrue(queryString.contains("p1=val2"));
      assertTrue(queryString.contains("p2=val3"));
      assertTrue(queryString.contains("p2=val4"));

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }

   @Test
   public void testCreateMultiKeyedMultiValueQueryStringList()
   {
      List<RequestParameter> params = new ArrayList<RequestParameter>();
      params.add(new QueryParameter("p1", "val1"));
      params.add(new QueryParameter("p1", "val2"));
      params.add(new QueryParameter("p2", "val3"));
      params.add(new QueryParameter("p2", "val4"));

      QueryString qs = QueryString.build(params);
      String queryString = qs.toQueryString();

      assertTrue(queryString.startsWith("?"));
      assertTrue(queryString.contains("p1=val1"));
      assertTrue(queryString.contains("p1=val2"));
      assertTrue(queryString.contains("p2=val3"));
      assertTrue(queryString.contains("p2=val4"));

      qs = QueryString.build(queryString);
      assertEquals(queryString, qs.toQueryString());
   }
}
