/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER3
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */

package com.ocpsoft.pretty.faces.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRegistration;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class MockFacesServletRegistration implements ServletRegistration
{

   public String getName()
   {
      return "Faces Servlet";
   }

   public String getClassName()
   {
      return "javax.faces.webapp.FacesServlet";
   }

   public boolean setInitParameter(String name, String value)
   {
      return false;
   }

   public String getInitParameter(String name)
   {
      return null;
   }

   public Set<String> setInitParameters(Map<String, String> initParameters)
   {
      return null;
   }

   public Map<String, String> getInitParameters()
   {
      return new HashMap<String, String>();
   }

   public Set<String> addMapping(String... urlPatterns)
   {
      return new HashSet<String>();
   }

   public Collection<String> getMappings()
   {
      ArrayList<String> results = new ArrayList<String>();
      results.add("/faces/*");
      return results;
   }

   public String getRunAsRole()
   {
      return null;
   }

}
