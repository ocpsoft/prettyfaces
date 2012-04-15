package org.ocpsoft.prettyfaces.annotation.jaas;

import static junit.framework.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class JaasRolesTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(JaasRolesBean.class)
               .addAsWebResource("jaas/protected-page.xhtml", "protected-page.xhtml");
   }

   @Test
   public void testJAASAnonymousUser() throws Exception
   {

      HttpResponse response = get("/admin/something");
      assertEquals(404, response.getStatusLine().getStatusCode());

   }

}
