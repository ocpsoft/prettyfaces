package org.ocpsoft.prettyfaces.annotation.basic.convert;

import static junit.framework.Assert.assertTrue;

import org.apache.http.client.methods.HttpGet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettyfaces.test.PrettyFacesTest;
import org.ocpsoft.rewrite.test.HttpAction;

@RunWith(Arquillian.class)
public class JsfConverterTest extends PrettyFacesTest
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
      HttpAction<HttpGet> action = get("/convert/hello");
      assertTrue(action.getResponseContent().contains("Parameter = [HELLO]"));
   }

}
