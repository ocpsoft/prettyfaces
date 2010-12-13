package com.ocpsoft.pretty.faces.config.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UrlMappingTest
{

   @Test
   public void testSetPatternSetsPatternParser()
   {
      UrlMapping mapping = new UrlMapping();
      assertEquals("", mapping.getPatternParser().getPattern());

      mapping.setPattern("/foo/bar");
      assertNotNull(mapping.getPatternParser());
      assertEquals("/foo/bar", mapping.getPatternParser().getPattern());
   }

}
