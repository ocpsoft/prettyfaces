package com.ocpsoft.pretty.faces.config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
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
      new JAXBPrettyConfigParser().parse(builder, xml, false);
      config = builder.build();
   }

   @Test
   public void testParseMappings()
   {
      assertNotNull("PrettyConfig has not been created!", config);
      assertNotNull("No mappings parsed", config.getGlobalRewriteRules());
      assertEquals(3, config.getMappings().size());
      
      /*
       * an empty mapping
       */
      UrlMapping emptyMapping = config.getMappings().get(0);
      assertEquals("", emptyMapping.getId());
      assertEquals("", emptyMapping.getParentId());
      assertEquals(true, emptyMapping.isOutbound());
      assertEquals("", emptyMapping.getViewId());
      assertEquals("", emptyMapping.getPattern());
      assertEquals(true, emptyMapping.isOnPostback());
      assertEquals(0, emptyMapping.getPathValidators().size());
      assertEquals(0, emptyMapping.getActions().size());
      assertEquals(0, emptyMapping.getQueryParams().size());

      /*
       * some standard mapping
       */
      UrlMapping storeMapping = config.getMappings().get(1);
      assertEquals("store", storeMapping.getId());
      assertEquals("", storeMapping.getParentId());
      assertEquals(true, storeMapping.isOutbound());
      assertEquals("/faces/shop/store.jsf", storeMapping.getViewId());
      assertEquals("/store/", storeMapping.getPattern());
      assertEquals(true, storeMapping.isOnPostback());
      assertEquals(0, storeMapping.getActions().size());
      assertEquals(0, storeMapping.getPathValidators().size());

      assertEquals(1, storeMapping.getQueryParams().size());
      assertEquals("language", storeMapping.getQueryParams().get(0).getName());
      assertEquals("#{bean.language}", storeMapping.getQueryParams().get(0).getExpression().getELExpression());
      
      /*
       * a complete mapping
       */
      UrlMapping completeMapping = config.getMappings().get(2);
      assertEquals("complete", completeMapping.getId());
      assertEquals("parentId", completeMapping.getParentId());
      assertEquals(false, completeMapping.isOutbound());
      assertEquals("/faces/validate.jsf", completeMapping.getViewId());
      assertEquals("/#{bean.path}", completeMapping.getPattern());
      assertEquals(false, completeMapping.isOnPostback());
      
      assertEquals(1, completeMapping.getActions().size());
      assertEquals("#{bean.action}", completeMapping.getActions().get(0).getAction().getELExpression());
      assertEquals(PhaseId.RENDER_RESPONSE, completeMapping.getActions().get(0).getPhaseId());
      assertEquals(false, completeMapping.getActions().get(0).onPostback());
      
      assertEquals(1, completeMapping.getQueryParams().size());
      assertEquals("q", completeMapping.getQueryParams().get(0).getName());
      assertEquals("#{bean.query}", completeMapping.getQueryParams().get(0).getExpression().getELExpression());
      assertEquals("pretty:error", completeMapping.getQueryParams().get(0).getOnError());
      assertEquals("validatorId", completeMapping.getQueryParams().get(0).getValidatorIds());
      assertEquals("#{bean.validate}", completeMapping.getQueryParams().get(0).getValidatorExpression().getELExpression());
      assertEquals(false, completeMapping.getQueryParams().get(0).isOnPostback());

      assertEquals(1, completeMapping.getPathValidators().size());
      assertEquals(0, completeMapping.getPathValidators().get(0).getIndex());
      assertEquals("#{bean.validatorError}", completeMapping.getPathValidators().get(0).getOnError());
      assertEquals("validator1", completeMapping.getPathValidators().get(0).getValidatorIds());
      assertEquals("#{bean.validate}", completeMapping.getPathValidators().get(0).getValidatorExpression().getELExpression());
      
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
