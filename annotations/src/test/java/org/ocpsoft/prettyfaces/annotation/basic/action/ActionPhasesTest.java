package org.ocpsoft.prettyfaces.annotation.basic.action;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(Arquillian.class)
public class ActionPhasesTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(ActionPhasesBean.class)
               .addAsWebResource("basic/action-phases.xhtml", "action-phases.xhtml");
   }

   @Test
   public void testActionPhases() throws Exception
   {

      // initial load of page
      HtmlPage firstPage = getWebClient("/action").getPage();

      // reload so we get a postback that visits all the phases
      HtmlPage secondPage = firstPage.getElementById("form:reload").click();

      // assert actions invoked in correct phases
      String secondPageContent = secondPage.getWebResponse().getContentAsString();
      Assert.assertTrue(secondPageContent.contains("actionDefaultPhase:RESTORE_VIEW"));
      Assert.assertTrue(secondPageContent.contains("actionBeforeRenderResponse:RENDER_RESPONSE"));
      Assert.assertTrue(secondPageContent.contains("actionAfterInvokeApplication:INVOKE_APPLICATION"));

   }

}
