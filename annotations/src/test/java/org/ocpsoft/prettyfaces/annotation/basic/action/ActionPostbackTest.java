package org.ocpsoft.prettyfaces.annotation.basic.action;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(Arquillian.class)
public class ActionPostbackTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(ActionPostbackBean.class)
               .addAsWebResource("basic/action-postback.xhtml", "action-postback.xhtml");
   }

   @Test
   public void testBasicMapping() throws Exception
   {

      // initial load of page
      HtmlPage firstPage = getWebClient("/action").getPage();

      // first page visit
      String firstPageContent = firstPage.getWebResponse().getContentAsString();
      assertTrue(firstPageContent.contains("actionOnPostbackDefault = [true]"));
      assertTrue(firstPageContent.contains("actionOnPostbackTrue = [true]"));
      assertTrue(firstPageContent.contains("actionOnPostbackFalse = [true]"));

      // click the reload button
      HtmlPage secondPage = firstPage.getElementById("form:reload").click();

      // first page visit
      String secondPageContent = secondPage.getWebResponse().getContentAsString();
      assertTrue(secondPageContent.contains("actionOnPostbackDefault = [true]"));
      assertTrue(secondPageContent.contains("actionOnPostbackTrue = [true]"));
      assertTrue(secondPageContent.contains("actionOnPostbackFalse = [false]"));

   }

}
