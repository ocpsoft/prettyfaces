package org.ocpsoft.prettyfaces.annotation.basic.convert;

import static junit.framework.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class JsfConverterTest extends PrettyFacesTestBase
{

   @Deployment(testable = false)
   public static WebArchive getDeployment()
   {
      return getBaseDeployment()
               .addClass(JsfConverterBean.class)
               .addClass(UppercaseConverter.class)
               .addAsWebResource("basic/convert.xhtml", "convert.xhtml");
   }

   @Test
   public void testConvert() throws Exception
   {
      String page = getPageAsString("/convert/hello");
      assertTrue(page.contains("Parameter = [HELLO]"));
   }

}
