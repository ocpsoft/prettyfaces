package com.ocpsoft.pretty.faces.test.loop;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jboss.arquillian.MavenArtifactResolver;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ocpsoft.pretty.PrettyContext;

@RunWith(Arquillian.class)
public class InfiniteLoopTest
{
   @Deployment
   public static Archive<?> createDeployment()
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
               .addWebResource("faces-config.xml")
               .addResource("loop/loop.xhtml", "loop.xhtml")
               .addWebResource("loop/loop-pretty-config.xml", "pretty-config.xml")
               .addLibrary(MavenArtifactResolver.resolve(
                        "com.ocpsoft:prettyfaces-jsf2:3.1.1-SNAPSHOT"
                        ))
               .setWebXML("jsf-web.xml");
   }

   @Test
   public void testRewriteTrailingSlashToLowerCase() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/loop.jsf");

      PrettyContext context = PrettyContext.getCurrentInstance();
      assertEquals("/loop.jsf", context.getRequestURL().toURL());

      JSFServerSession server = jsfSession.getJSFServerSession();
      assertEquals("/loop.xhtml", server.getCurrentViewID());
   }
}
