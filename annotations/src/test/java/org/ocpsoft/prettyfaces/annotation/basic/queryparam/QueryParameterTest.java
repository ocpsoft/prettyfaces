package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;

@RunWith(Arquillian.class)
public class QueryParameterTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(QueryParameterBean.class)
               .addAsWebResource("basic/query-param-basic.xhtml", "query-param-basic.xhtml");
   }

   @Test
   public void testQueryParameterBinding() throws Exception
   {
      assertTrue(get("/page?q=christian").getResponseContent().contains("Query Parameter = [christian]"));
   }

   @Test
   public void testMissingQueryParameterValue() throws Exception
   {
      assertTrue(get("/page").getResponseContent().contains("Query Parameter = []"));
   }

   @Test
   public void testMultipleQueryParameterValues() throws Exception
   {
      assertTrue(get("/page?q=abc&q=123").getResponseContent().contains("Query Parameter = [abc]"));
   }

}
