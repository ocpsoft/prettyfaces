package com.ocpsoft.pretty.faces.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FacesNavigationURLCanonicalizerTest
{

   /**
    * don't fail for null values
    */
   @Test
   public void testNormalizeRequestUriWithNullArguments()
   {
      assertEquals(null, FacesNavigationURLCanonicalizer.normalizeRequestURI(null, null, null));
   }

   /**
    * Test extension mapping: *.jsf
    */
   @Test
   public void testNormalizeRequestUriWithExtensionMapping()
   {
      assertEquals("/page2.jsf", FacesNavigationURLCanonicalizer.normalizeRequestURI("/page1.jsf", null, "/page2.jsf"));
   }

   /**
    * Test path mapping: /faces/*
    */
   @Test
   public void testNormalizeRequestUriWithPathMapping()
   {
      assertEquals("/page2.xhtml", FacesNavigationURLCanonicalizer.normalizeRequestURI("/faces", "/page1.xhtml", "/faces/page2.xhtml"));
   }

}
