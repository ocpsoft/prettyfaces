package com.ocpsoft.pretty.faces.rewrite;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.rewrite.Case;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.config.rewrite.TrailingSlash;
import com.ocpsoft.pretty.faces.rewrite.processor.MockCustomClassProcessor;

public class RewriteEngineTest
{
   String url = "/my/foo/is/COOL";
   RewriteEngine rewriteEngine = new RewriteEngine();

   @Test
   public void testRegex() throws Exception
   {
      RewriteRule c = new RewriteRule();
      c.setMatch("foo");
      c.setSubstitute("bar");

      assertEquals("/my/bar/is/COOL", rewriteEngine.processInbound(c, url));
   }

   @Test
   public void testTrailingSlash() throws Exception
   {
      RewriteRule c = new RewriteRule();
      c.setTrailingSlash(TrailingSlash.APPEND);
      assertEquals("/my/foo/is/COOL/", rewriteEngine.processInbound(c, url));
   }

   @Test
   public void testRemoveSingleTrailingSlash() throws Exception
   {
      RewriteRule c = new RewriteRule();
      c.setTrailingSlash(TrailingSlash.APPEND);
      assertEquals("/", rewriteEngine.processInbound(c, "/"));
   }

   @Test
   public void testToLowerCase() throws Exception
   {
      RewriteRule c = new RewriteRule();
      c.setToCase(Case.LOWERCASE);
      assertEquals("/my/foo/is/cool", rewriteEngine.processInbound(c, url));
   }

   @Test
   public void testCustomClassProcessor() throws Exception
   {
      RewriteRule c = new RewriteRule();
      c.setProcessor(MockCustomClassProcessor.class.getName());
      assertEquals(MockCustomClassProcessor.RESULT, rewriteEngine.processInbound(c, url));
   }
}
