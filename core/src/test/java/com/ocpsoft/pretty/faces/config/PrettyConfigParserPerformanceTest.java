package com.ocpsoft.pretty.faces.config;

import static junit.framework.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

public class PrettyConfigParserPerformanceTest
{
   
   private final static int NUMBER_OF_ITERATIONS = 50;

   @Test
   public void testParserPerformance() throws Exception {

      // don't measure initial startup costs for both parsers
      parseUsingDigester();
      parseUsingJAXB();
      
      // parse using JAXB
      long jaxbStart = System.currentTimeMillis();
      for(int i=0; i<NUMBER_OF_ITERATIONS; i++) {
         parseUsingJAXB();
      }
      long jaxbTime = System.currentTimeMillis() - jaxbStart;
      
      // parse using Digester
      long digesterStart = System.currentTimeMillis();
      for(int i=0; i<NUMBER_OF_ITERATIONS; i++) {
         parseUsingDigester();
      }
      long digesterTime = System.currentTimeMillis() - digesterStart;
      
      // log the result
      System.out.println("Time for "+NUMBER_OF_ITERATIONS+" iterations: Digester = "+
            digesterTime+", JAXB = "+jaxbTime);

      // JAXB should be faster :)
      assertTrue("Digester is faster than JAXB", jaxbTime < digesterTime);
      
   }
   
   private void parseUsingJAXB() throws Exception {
      JAXBPrettyConfigParser jaxb = new JAXBPrettyConfigParser();
      jaxb.parse(new PrettyConfigBuilder(), getTestData(), false);
   }

   private void parseUsingDigester() throws Exception {
      DigesterPrettyConfigParser digester = new DigesterPrettyConfigParser();
      digester.parse(new PrettyConfigBuilder(), getTestData(), false);
   }
   
   private InputStream getTestData()
   {
      return PrettyConfigParserPerformanceTest.class.getClassLoader().getResourceAsStream("mock-pretty-config.xml");
   }
   
}
