package com.ocpsoft.pretty.faces.test.convert;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ocpsoft.pretty.faces.test.PrettyFacesTestBase;

@RunWith(Arquillian.class)
public class ParameterConversionTest extends PrettyFacesTestBase
{

   @Deployment
   public static WebArchive createDeployment()
   {
      return PrettyFacesTestBase.createDeployment()
               .addClass(CustomNumberConverter.class)
               .addClass(StartPageBean.class)
               .addClass(ParamConversionBean.class)
               .addClass(AnnotationQueryParamConversionBean.class)
               .addClass(AnnotationPathParamConversionBean.class)
               .addWebResource("convert/pretty-config.xml", "pretty-config.xml")
               .addResource("convert/index.xhtml", "index.xhtml")
               .addResource("convert/convert.xhtml", "convert.xhtml");
   }

   @Test
   public void testXmlDefaultConverterForQueryParameter() throws Exception
   {
      
      // access the page
      JSFSession jsfSession = new JSFSession("/xml/queryparam/default?number=3");
      
      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // assert that correct values has been injected and that the page has been rendered
      assertEquals(3, server.getManagedBeanValue("#{paramConversionBean.number}"));
      assertTrue(client.getPageAsText().contains("Result page rendered successfully!"));
      
   }
   
   @Test
   public void testXmlDefaultConverterForPathParameter() throws Exception
   {
      
      // access the page
      JSFSession jsfSession = new JSFSession("/xml/pathparam/default/1");
      
      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // assert that correct values has been injected and that the page has been rendered
      assertEquals(1, server.getManagedBeanValue("#{paramConversionBean.number}"));
      assertTrue(client.getPageAsText().contains("Result page rendered successfully!"));
      
   }
   
   @Test
   public void testXmlCustomConverterForQueryParameter() throws Exception
   {
      
      // access the page
      JSFSession jsfSession = new JSFSession("/xml/queryparam/custom?number=one");
      
      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // assert that correct values has been injected and that the page has been rendered
      assertEquals(1, server.getManagedBeanValue("#{paramConversionBean.number}"));
      assertTrue(client.getPageAsText().contains("Result page rendered successfully!"));
      
   }
   
   @Test
   public void testXmlCustomConverterForPathParameter() throws Exception
   {
      
      // access the page
      JSFSession jsfSession = new JSFSession("/xml/pathparam/custom/two");
      
      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // assert that correct values has been injected and that the page has been rendered
      assertEquals(2, server.getManagedBeanValue("#{paramConversionBean.number}"));
      assertTrue(client.getPageAsText().contains("Result page rendered successfully!"));
      
   }
   
   @Test
   public void testAnnotationCustomConverterForQueryParameter() throws Exception
   {

      // access the page
      JSFSession jsfSession = new JSFSession("/annotation/queryparam/custom?number=two");

      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      // assert that correct values has been injected and that the page has been rendered
      assertEquals(2, server.getManagedBeanValue("#{annotationQueryParamConversionBean.number}"));
      assertTrue(client.getPageAsText().contains("Result page rendered successfully!"));
      
   }

   @Test
   public void testAnnotationCustomConverterForPathParameter() throws Exception
   {

      // access the page
      JSFSession jsfSession = new JSFSession("/annotation/pathparam/custom/three");

      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();

      // assert that correct values has been injected and that the page has been rendered
      assertEquals(3, server.getManagedBeanValue("#{annotationPathParamConversionBean.number}"));
      assertTrue(client.getPageAsText().contains("Result page rendered successfully!"));

   }
   
   @Test
   public void testConversionWithPrettyLink() throws Exception
   {
      
      // access the page
      JSFSession jsfSession = new JSFSession("/index.jsf");
      
      // get the client/server objects
      JSFClientSession client = jsfSession.getJSFClientSession();

      // default converter
      assertEquals("/test/xml/queryparam/default?number=1", 
               client.getElement("queryParameterDefaultConverterLink").getAttribute("href"));
      assertEquals("/test/xml/pathparam/default/2", 
               client.getElement("pathParameterDefaultConverterLink").getAttribute("href"));
      
      // custom converter (doesn't work correctly at the moment)
      /*
       
      assertEquals("/test/xml/queryparam/default?number=one", 
               client.getElement("queryParameterDefaultConverterLink").getAttribute("href"));
      assertEquals("/test/xml/pathparam/default/two", 
               client.getElement("pathParameterDefaultConverterLink").getAttribute("href"));
      */

   }

   @Test
   public void testConversionWithActionMethods() throws Exception
   {
      
      // access the page
      JSFSession jsfSession = new JSFSession("/index.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();

      // default converter query parameters
      client.click("queryParameterDefaultConverterButton");
      assertTrue("Invalid URL generated: " + client.getContentPage().getUrl().toString(),
               client.getContentPage().getUrl().toString().endsWith("/test/xml/queryparam/default?number=2"));
      client.click("back");
      
      // default converter path parameters
      client.click("pathParameterDefaultConverterButton");
      assertTrue("Invalid URL generated: " + client.getContentPage().getUrl().toString(),
               client.getContentPage().getUrl().toString().endsWith("/test/xml/pathparam/default/2"));
      client.click("back");
      
      // custom converter query parameters
      client.click("queryParameterCustomConverterButton");
      assertTrue("Invalid URL generated: " + client.getContentPage().getUrl().toString(),
               client.getContentPage().getUrl().toString().endsWith("/test/xml/queryparam/custom?number=two"));
      client.click("back");
      
      // custom converter path parameters
      client.click("pathParameterCustomConverterButton");
      assertTrue("Invalid URL generated: " + client.getContentPage().getUrl().toString(),
               client.getContentPage().getUrl().toString().endsWith("/test/xml/pathparam/custom/two"));
      client.click("back");

   }

}
