package org.ocpsoft.prettyfaces.test;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.ocpsoft.rewrite.test.RewriteTest;
import org.ocpsoft.rewrite.test.RewriteTestBase;

public class PrettyFacesTest extends RewriteTestBase
{
   public static WebArchive getBaseDeployment()
   {

      WebArchive deployment = ShrinkWrap
               .create(WebArchive.class, "rewrite-test.war")

               /*
                * PrettyFaces
                */
               .addAsLibraries(getPrettyFacesArchive())

               /*
                * Rewrite
                */
               .addAsLibraries(resolveDependencies("org.ocpsoft.rewrite:rewrite-impl-servlet"))
               .addAsLibraries(resolveDependencies("org.ocpsoft.rewrite:rewrite-config-annotations-impl"))
               .addAsLibraries(resolveDependencies("org.ocpsoft.rewrite:rewrite-integration-faces"))

               .addAsWebInfResource("faces-config.xml", "faces-config.xml");

      if(RewriteTest.isJetty())
      {
         /*
          * Set the JSF implementation
          */
         deployment.setWebXML("jetty-web.xml");
      }
      
      return deployment;

   }

   protected static JavaArchive getPrettyFacesArchive()
   {
      JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "prettyfaces.jar")
               .addAsResource(new File("../core/target/classes/org"))
               .addAsResource(new File("../annotations/target/classes/org"))
               .addAsResource(new File("../annotations/target/classes/META-INF"));
      
      return archive;
   }

}
