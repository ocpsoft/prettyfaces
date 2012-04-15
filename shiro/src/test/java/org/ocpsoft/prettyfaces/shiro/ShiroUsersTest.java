package org.ocpsoft.prettyfaces.shiro;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class ShiroUsersTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive createDeployment()
   {

      // web.xml have to be removed first because we bundle a different one
      WebArchive baseDeployment = PrettyFacesTestBase.getBaseDeployment();
      baseDeployment.delete("/WEB-INF/web.xml");

      return baseDeployment
               .addClass(AdminPageBean.class)
               .addClass(LoginServlet.class)
               .addClass(LogoutServlet.class)
               .addClass(ShiroTestRealm.class)
               .addAsLibraries(resolveDependencies("org.apache.shiro:shiro-core:jar:1.2.0"))
               .setWebXML("shiro-web.xml")
               .addAsWebInfResource("shiro.ini")
               .addAsWebResource("protected-page.xhtml");
   }

   @Test
   public void testShiroAsAnonymousUser() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpResponse response = get(client, "/admin/something");
      assertEquals(404, response.getStatusLine().getStatusCode());
      readBody(response);
   }

   @Test
   public void testShiroAsAuthorizedUser() throws Exception
   {

      HttpClient client = new DefaultHttpClient();

      // before login
      HttpResponse beforeLogin = get(client, "/admin/something");
      assertEquals(404, beforeLogin.getStatusLine().getStatusCode());
      readBody(beforeLogin);

      // login as admin
      HttpResponse login = get(client, "/login?user=ck");
      assertEquals(200, login.getStatusLine().getStatusCode());
      readBody(login);

      // page is available
      HttpResponse afterLogin = get(client, "/admin/something");
      assertEquals(200, afterLogin.getStatusLine().getStatusCode());
      assertTrue(readBody(afterLogin).contains("Protected admin page"));

      // logout as admin
      HttpResponse logout = get(client, "/logout");
      assertEquals(200, logout.getStatusLine().getStatusCode());
      readBody(logout);

      // after logout
      HttpResponse afterLogout = get(client, "/admin/something");
      assertEquals(404, afterLogout.getStatusLine().getStatusCode());
      readBody(afterLogout);

   }

   @Test
   public void testShiroAsOtherUser() throws Exception
   {

      HttpClient client = new DefaultHttpClient();

      // before login
      HttpResponse beforeLogin = get(client, "/admin/something");
      assertEquals(404, beforeLogin.getStatusLine().getStatusCode());
      readBody(beforeLogin);

      // login as admin
      HttpResponse login = get(client, "/login?user=somebody");
      assertEquals(200, login.getStatusLine().getStatusCode());
      readBody(login);

      // wrong role
      HttpResponse afterLogin = get(client, "/admin/something");
      assertEquals(404, afterLogin.getStatusLine().getStatusCode());
      readBody(afterLogin);

      // logout as admin
      HttpResponse logout = get(client, "/logout");
      assertEquals(200, logout.getStatusLine().getStatusCode());
      readBody(logout);

      // after logout
      HttpResponse afterLogout = get(client, "/admin/something");
      assertEquals(404, afterLogout.getStatusLine().getStatusCode());
      readBody(afterLogout);

   }

   private HttpResponse get(HttpClient client, String path) throws IOException
   {
      HttpGet get = new HttpGet(getFullUrl(path));
      HttpResponse response = client.execute(get);
      return response;
   }

   private String readBody(HttpResponse response) throws IOException
   {
      return EntityUtils.toString(response.getEntity());
   }

}
