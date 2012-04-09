package org.ocpsoft.prettyfaces.annotation.basic;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class BasicMappingTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(BasicMappingBean.class)
               .addAsWebResource("basic/basic.xhtml", "basic.xhtml");
   }

   @Test
   public void testBasicMapping() throws Exception
   {

      String page = getPageAsString("/basic/hello");

      assertTrue(page.contains("Parameter = [hello]"));
      assertTrue(page.contains("Action invoked = [true]"));

   }

}
