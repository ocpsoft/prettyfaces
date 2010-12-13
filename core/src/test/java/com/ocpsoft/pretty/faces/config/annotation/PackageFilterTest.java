package com.ocpsoft.pretty.faces.config.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PackageFilterTest
{

   @Test
   public void testEmptyFilter()
   {
      assertTrue(new PackageFilter(null).isAllowedPackage("com.ocpsoft"));
      assertTrue(new PackageFilter("").isAllowedPackage("com.ocpsoft"));
      assertTrue(new PackageFilter("  ").isAllowedPackage("com.ocpsoft"));
      assertTrue(new PackageFilter(" ,, ").isAllowedPackage("com.ocpsoft"));
   }

   @Test
   public void testSinglePackage()
   {

      // test simple filter
      PackageFilter filter = new PackageFilter("com.ocpsoft");
      assertEquals(true, filter.isAllowedPackage("com.ocpsoft.pretty.faces"));
      assertEquals(true, filter.isAllowedPackage("com.ocpsoft"));
      assertEquals(false, filter.isAllowedPackage("com"));
      assertEquals(false, filter.isAllowedPackage("de"));

      // test some danger configuration parameter inputs
      assertEquals(true, new PackageFilter("  com.ocpsoft ").isAllowedPackage("com.ocpsoft.pretty.faces"));
      assertEquals(true, new PackageFilter(" , com.ocpsoft, ").isAllowedPackage("com.ocpsoft.pretty.faces"));

   }

   @Test
   public void testMultiplePackages()
   {

      // test simple filter
      PackageFilter filter = new PackageFilter("com.ocpsoft,de.chkal");
      assertEquals(true, filter.isAllowedPackage("com.ocpsoft.pretty.faces"));
      assertEquals(true, filter.isAllowedPackage("com.ocpsoft"));
      assertEquals(true, filter.isAllowedPackage("de.chkal.bla"));
      assertEquals(true, filter.isAllowedPackage("de.chkal"));
      assertEquals(false, filter.isAllowedPackage("com"));
      assertEquals(false, filter.isAllowedPackage("com.google"));
      assertEquals(false, filter.isAllowedPackage("de"));

      // test some danger configuration parameter inputs
      assertEquals(true, new PackageFilter("  com.ocpsoft , de.chkal ").isAllowedPackage("com.ocpsoft.pretty.faces"));
      assertEquals(true, new PackageFilter(" , com.ocpsoft, de.chkal ,,").isAllowedPackage("de.chkal.bla"));

   }

}
