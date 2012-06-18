package org.ocpsoft.prettyfaces.compat.interaction;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;
import org.ocpsoft.rewrite.test.HttpAction;

@RunWith(Arquillian.class)
public class RewriteEngineUrlMappingInteractionTest extends PrettyFacesTest
{
   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {
      WebArchive archive = getBaseDeployment()
               .addClasses(InteractionDynaViewBean.class)
               .addAsLibraries(resolveDependencies("com.ocpsoft:prettyfaces-jsf2"))
               .addAsWebResource("interaction/interaction-page.xhtml", "page.xhtml")
               .addAsWebInfResource("interaction/interaction-pretty-config.xml", "pretty-config.xml");

      return archive;
   }

   /**
    * Accessing the page using the URL mapping
    */
   @Test
   public void testSimpleUrlMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/page");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/page"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

   /**
    * Accessing the page using a dynaview
    */
   @Test
   public void testDynaViewUrlMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/dyna/page");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/dyna/page"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

   /**
    * Rewrite rule forwards to the URL mapping
    */
   @Test
   public void testRewriteForwardsToUrlMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/rewrite-forwards-to-page-mapping");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/rewrite-forwards-to-page-mapping"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

   /**
    * Rewrite rule redirects to the URL mapping
    */
   @Test
   public void testRewriteRedirectsToUrlMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/rewrite-redirects-to-page-mapping");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/page"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

   /**
    * Rewrite rule forwards to the dynaview
    */
   @Test
   public void testRewriteForwardsToDynaviewMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/rewrite-forwards-to-dynaview");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/rewrite-forwards-to-dynaview"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

   /**
    * Rewrite rule redirects to the dynaview
    */
   @Test
   public void testRewriteRedirectsToDynaviewMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/rewrite-redirects-to-dynaview");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/dyna/page"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

   /**
    * Directly accessing the view-id should redirect to the pretty URL
    */
   @Test
   public void testJsfViewIdRedirectsToMapping() throws Exception
   {
      HttpAction<HttpGet> client = get("/page.jsf");
      Assert.assertEquals(200, client.getStatusCode());
      Assert.assertTrue(client.getCurrentURL().endsWith("/page"));
      Assert.assertTrue(client.getResponseContent().contains("The page rendered fine!"));
   }

}