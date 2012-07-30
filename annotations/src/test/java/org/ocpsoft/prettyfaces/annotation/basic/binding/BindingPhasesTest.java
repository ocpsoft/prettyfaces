package org.ocpsoft.prettyfaces.annotation.basic.binding;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(Arquillian.class)
public class BindingPhasesTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(BindingPhasesBean.class)
               .addAsWebResource("basic/binding-phases.xhtml", "binding-phases.xhtml");
   }

   @Test
   public void testBindingPhases() throws Exception
   {

      // initial load of page
      HtmlPage firstPage = getWebClient("/binding/hello/").getPage();

      // reload so we get a postback that visits all the phases
      HtmlPage secondPage = firstPage.getElementById("form:reload").click();

      // assert actions invoked in correct phases
      String secondPageContent = secondPage.getWebResponse().getContentAsString();
      Assert.assertTrue(secondPageContent.contains("defaultPhase:RESTORE_VIEW"));
      Assert.assertTrue(secondPageContent.contains("beforeRenderResponse:RENDER_RESPONSE"));
      Assert.assertTrue(secondPageContent.contains("afterInvokeApplication:INVOKE_APPLICATION"));

   }

}
