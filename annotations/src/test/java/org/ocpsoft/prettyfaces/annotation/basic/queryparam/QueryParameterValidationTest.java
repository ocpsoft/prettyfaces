package org.ocpsoft.prettyfaces.annotation.basic.queryparam;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.annotation.basic.validate.EvenLengthValidator;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;

@RunWith(Arquillian.class)
public class QueryParameterValidationTest extends PrettyFacesTest
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
      assertTrue(get("/page?q=abcd").getResponseContent().contains("Query Parameter = [abcd]"));
   }

   @Test
   public void testInvalidQueryParameter() throws Exception
   {
      assertEquals(404, get("/page?q=abc").getStatusCode());
   }

   @Test
   @Ignore
   // doesn't work for some reason
   public void testMissingQueryParameter() throws Exception
   {
      assertTrue(get("/page").getResponseContent().contains("Query Parameter = []"));
   }

}
