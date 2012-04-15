package org.ocpsoft.prettyfaces.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.el.ExpressionFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import com.sun.el.ExpressionFactoryImpl;

public class PrettyFacesTestBase
{

   public static WebArchive getBaseDeployment()
   {

      return ShrinkWrap
               .create(WebArchive.class, "prettyfaces-test.war")

               /*
                * PrettyFaces
                */
               .addAsLibraries(getPrettyFacesArchive())

               /*
                * Rewrite
                */
               .addAsLibraries(resolveDependencies("org.ocpsoft.rewrite:rewrite-impl-servlet:jar:1.0.6-SNAPSHOT"))
               .addAsLibraries(resolveDependencies("org.ocpsoft.rewrite:rewrite-config-annotations-impl:jar:1.0.6-SNAPSHOT"))
               .addAsLibraries(resolveDependencies("org.ocpsoft.rewrite:rewrite-integration-faces:jar:1.0.6-SNAPSHOT"))

               /*
                * JSF implementation
                */
               .addAsLibraries(resolveDependencies("org.glassfish:javax.faces:jar:2.1.7"))

               /*
                * Set the EL implementation
                */
               .addAsLibraries(resolveDependencies("org.glassfish.web:el-impl:jar:2.2"))
               .addAsServiceProvider(ExpressionFactory.class, ExpressionFactoryImpl.class)

               /*
                * Set up container configuration
                */
               .setWebXML("jetty-web.xml")
               .addAsWebInfResource("faces-config.xml", "faces-config.xml");

   }

   protected static Collection<GenericArchive> resolveDependencies(final String coords)
   {
      return DependencyResolvers.use(MavenDependencyResolver.class)
               .artifacts(coords)
               .loadReposFromPom("pom.xml")
               .resolveAs(GenericArchive.class);
   }

   protected static JavaArchive getPrettyFacesArchive()
   {
      return ShrinkWrap.create(JavaArchive.class, "prettyfaces.jar")
               .addAsResource(new File("../annotations/target/classes"));
   }

   protected HttpResponse get(String path) throws IOException
   {
      return get(new DefaultHttpClient(), path);
   }

   protected HttpResponse get(HttpClient client, String path) throws IOException
   {
      HttpGet get = new HttpGet(getFullUrl(path));
      HttpResponse response = client.execute(get);
      return response;
   }

   protected String readBody(HttpResponse response) throws IOException
   {
      return EntityUtils.toString(response.getEntity());
   }

   protected String getPageAsString(String path) throws IOException
   {
      return readBody(get(new DefaultHttpClient(), path));
   }

   protected String getFullUrl(String path)
   {
      return "http://localhost:9090/prettyfaces-test" + path;
   }

}
