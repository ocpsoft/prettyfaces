package com.ocpsoft.pretty.faces.test.dynaview;

import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ocpsoft.pretty.faces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class PathParamDynaViewTest extends PrettyFacesTestBase
{

   @Deployment
   public static WebArchive createDeployment()
   {
      return PrettyFacesTestBase.createDeployment()
            .addClass(PathParamDynaViewBean.class)
            .addResource("dynaview/index.xhtml", "index.xhtml")
            .addResource("dynaview/correct.xhtml", "correct.xhtml");
   }

   @Test
   public void testInjectionHappensBeforeViewDetermination() throws Exception
   {
      
      // First visit the start page
      JSFSession jsfSession = new JSFSession("/index.jsf");

      // click on the link talking the user to /dynaview/correct
      JSFClientSession client = jsfSession.getJSFClientSession();
      client.click("link");

      // the dynaview code should send the user to the correct page.
      assertTrue(client.getPageAsText().contains("The path parameter was correctly injected"));
      
   }

}
