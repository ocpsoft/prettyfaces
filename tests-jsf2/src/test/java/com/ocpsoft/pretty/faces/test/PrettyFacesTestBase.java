/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ocpsoft.pretty.faces.test;

import org.jboss.arquillian.MavenArtifactResolver;
import org.jboss.arquillian.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PrettyFacesTestBase
{
   @Deployment
   public static WebArchive createDeployment()
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
               .addWebResource("faces-config.xml")
               .addLibrary(MavenArtifactResolver.resolve(
                        "com.ocpsoft.rewrite:rewrite-api:1.0.0-SNAPSHOT"))
               .addLibrary(MavenArtifactResolver.resolve(
                        "com.ocpsoft.rewrite:rewrite-impl-servlet:1.0.0-SNAPSHOT"))
               .addLibrary(MavenArtifactResolver.resolve(
                        "com.ocpsoft:prettyfaces-jsf2:3.3.1-SNAPSHOT"))
               .setWebXML("jsf-web.xml");
   }
}
