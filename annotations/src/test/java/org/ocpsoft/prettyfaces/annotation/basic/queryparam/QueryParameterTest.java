package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class QueryParameterTest extends PrettyFacesTestBase
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
      String page = getPageAsString("/page?q=christian");
      assertTrue(page.contains("Query Parameter = [christian]"));
   }

   @Test
   public void testMissingQueryParameterValue() throws Exception
   {
      String page = getPageAsString("/page");
      assertTrue(page.contains("Query Parameter = []"));
   }

   @Test
   public void testMultipleQueryParameterValues() throws Exception
   {
      String page = getPageAsString("/page?q=abc&q=123");
      assertTrue(page.contains("Query Parameter = [abc]"));
   }

}
