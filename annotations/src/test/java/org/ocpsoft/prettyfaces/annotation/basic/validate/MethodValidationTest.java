package org.ocpsoft.prettyfaces.annotation.basic.validate;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;
import org.ocpsoft.rewrite.test.HttpAction;

@RunWith(Arquillian.class)
public class MethodValidationTest extends PrettyFacesTest
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
      assertTrue(get("/validate/test123").getResponseContent().contains("Parameter = [test123]"));
   }

   @Test
   public void testValidationMethodFailed() throws Exception
   {
      HttpAction<HttpGet> action = get("/validate/test666");
      assertEquals(404, action.getStatusCode());
   }

}
