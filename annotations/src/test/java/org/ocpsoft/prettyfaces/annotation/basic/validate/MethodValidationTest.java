package org.ocpsoft.prettyfaces.annotation.basic.validate;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class MethodValidationTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(MethodValidationBean.class)
               .addAsWebResource("basic/method-validation.xhtml", "method-validation.xhtml");
   }

   @Test
   public void testValidationMethodSuccess() throws Exception
   {
      String page = getPageAsString("/validate/test123");
      assertTrue(page.contains("Parameter = [test123]"));
   }

   @Test
   public void testValidationMethodFailed() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpGet get = new HttpGet(getFullUrl("/validate/test666"));
      HttpResponse response = client.execute(get);
      assertEquals(404, response.getStatusLine().getStatusCode());
   }

}
