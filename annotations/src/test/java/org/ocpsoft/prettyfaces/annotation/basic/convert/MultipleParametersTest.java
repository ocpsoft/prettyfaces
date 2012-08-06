package org.ocpsoft.prettyfaces.annotation.basic.convert;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;

@RunWith(Arquillian.class)
public class MultipleParametersTest extends PrettyFacesTest
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(MultipleParametersBean.class)
               .addClass(ReverseConverter.class)
               .addClass(UppercaseConverter.class)
               .addAsWebResource("basic/multiple-parameters.xhtml", "multiple-parameters.xhtml");
   }

   @Test
   @Ignore // doesn't work correctly. Bug in AddBindingVisitor?
   public void testBasicJoin() throws Exception
   {

      String page = get("/path/christian").getResponseContent();

      assertTrue(page.contains("Hostname: [1.0.0.721]"));
      assertTrue(page.contains("Path: [CHRISTIAN]"));

   }

}
