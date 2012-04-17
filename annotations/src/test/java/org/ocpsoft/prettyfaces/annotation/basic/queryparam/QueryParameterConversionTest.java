package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.annotation.basic.convert.UppercaseConverter;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class QueryParameterConversionTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(QueryParameterConversionBean.class)
               .addClass(UppercaseConverter.class)
               .addAsWebResource("basic/query-param-conversion.xhtml", "query-param-conversion.xhtml");
   }

   @Test
   public void testQueryParameterConversion() throws Exception
   {
      String page = getPageAsString("/page?q=abcd");
      assertTrue(page.contains("Query Parameter = [ABCD]"));
   }

   @Test
   public void testQueryParameterConversionWithMissingParameter() throws Exception
   {
      String page = getPageAsString("/page");
      assertTrue(page.contains("Query Parameter = []"));
   }

}
