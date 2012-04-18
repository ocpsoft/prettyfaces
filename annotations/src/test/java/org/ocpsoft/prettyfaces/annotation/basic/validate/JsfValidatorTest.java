package org.ocpsoft.prettyfaces.annotation.basic.validate;

import static junit.framework.Assert.assertTrue;

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
public class JsfValidatorTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(JsfValidatorBean.class)
               .addClass(EvenLengthValidator.class)
               .addAsWebResource("basic/validate.xhtml", "validate.xhtml");
   }

   @Test
   public void testValidationSuccess() throws Exception
   {
      HttpAction<HttpGet> action = get("/validate/abcd");
      assertTrue(action.getResponseContent().contains("Parameter = [abcd]"));
   }

   @Test
   public void testValidationFailed() throws Exception
   {
      HttpAction<HttpGet> action = get("/validate/abc");
      Assert.assertEquals(404, action.getStatusCode());
      Assert.assertTrue(action.getResponseContent().contains("404"));
   }

}
