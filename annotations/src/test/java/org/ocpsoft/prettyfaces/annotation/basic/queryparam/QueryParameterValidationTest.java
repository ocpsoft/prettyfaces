package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.annotation.basic.validate.EvenLengthValidator;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class QueryParameterValidationTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(QueryParameterValidationBean.class)
               .addClass(EvenLengthValidator.class)
               .addAsWebResource("basic/query-param-validation.xhtml", "query-param-validation.xhtml");
   }

   @Test
   public void testValidQueryParameter() throws Exception
   {
      String page = getPageAsString("/page?q=abcd");
      assertTrue(page.contains("Query Parameter = [abcd]"));
   }

   @Test
   public void testInvalidQueryParameter() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpGet get = new HttpGet(getFullUrl("/page?q=abc"));
      HttpResponse response = client.execute(get);
      assertEquals(404, response.getStatusLine().getStatusCode());
   }

   @Test
   @Ignore
   // doesn't work for some reason
   public void testMissingQueryParameter() throws Exception
   {
      String page = getPageAsString("/page");
      assertTrue(page.contains("Query Parameter = []"));
   }

}
