package com.ocpsoft.pretty.faces.config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.ocpsoft.pretty.faces.config.rewrite.Case;
import com.ocpsoft.pretty.faces.config.rewrite.Redirect;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.config.rewrite.TrailingSlash;

public class JAXBPrettyConfigParserTest
{

   private PrettyConfig config;

   @Before
   public void setUp() throws Exception
   {
      InputStream xml = getClass().getClassLoader().getResourceAsStream("complete-pretty-config.xml");
      assertNotNull("Cannot find pretty-config.xml file for test!", xml);
      PrettyConfigBuilder builder = new PrettyConfigBuilder();
      new JAXBPrettyConfigParser().parse(builder, xml);
      config = builder.build();
   }

   @Test
   public void testParseRewriteRules()
   {

      assertNotNull("PrettyConfig has not been created!", config);
      assertNotNull("No rewrite rules parsed", config.getGlobalRewriteRules());
      assertEquals(2, config.getGlobalRewriteRules().size());

      // no attributes specified in the XML file. Check for default values
      RewriteRule minimalRule = config.getGlobalRewriteRules().get(0);
      assertEquals("", minimalRule.getMatch());
      assertEquals("", minimalRule.getSubstitute());
      assertEquals("", minimalRule.getUrl());
      assertEquals(Redirect.PERMANENT, minimalRule.getRedirect());
      assertEquals(Case.IGNORE, minimalRule.getToCase());
      assertEquals(TrailingSlash.IGNORE, minimalRule.getTrailingSlash());
      assertEquals(true, minimalRule.isInbound());
      assertEquals(true, minimalRule.isOutbound());
      assertEquals("", minimalRule.getProcessor());

      // all attributes specified. Verify values from XML file.
      RewriteRule fullRule = config.getGlobalRewriteRules().get(1);
      assertEquals("/something", fullRule.getMatch());
      assertEquals("/new", fullRule.getSubstitute());
      assertEquals("http://www.ocpsoft.com/", fullRule.getUrl());
      assertEquals(Redirect.TEMPORARY, fullRule.getRedirect());
      assertEquals(Case.UPPERCASE, fullRule.getToCase());
      assertEquals(TrailingSlash.REMOVE, fullRule.getTrailingSlash());
      assertEquals(false, fullRule.isInbound());
      assertEquals(false, fullRule.isOutbound());
      assertEquals("com.ocpsoft.MyProcessor", fullRule.getProcessor());

   }

}
