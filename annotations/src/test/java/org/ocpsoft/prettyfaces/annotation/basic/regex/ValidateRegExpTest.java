package org.ocpsoft.prettyfaces.annotation.basic.regex;

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
public class ValidateRegExpTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(ValidateRegExpBean.class)
               .addAsWebResource("basic/validate-regexp.xhtml", "validate-regexp.xhtml");
   }

   @Test
   public void testMatchingExpression() throws Exception
   {
      HttpAction<HttpGet> action = get("/check/ab");
      assertEquals(200, action.getStatusCode());
   }

   @Test
   public void testNotMatchingExpression() throws Exception
   {
      HttpAction<HttpGet> action = get("/check/abc");
      assertEquals(404, action.getStatusCode());
   }

}
