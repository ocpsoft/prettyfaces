/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.faces.el.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

import com.ocpsoft.logging.Logger;
import com.ocpsoft.pretty.faces.spi.ELBeanNameResolver;
import com.ocpsoft.pretty.faces.util.SimpleXMLParserBase;

/**
 * 
 * <p>
 * Implementation of {@link ELBeanNameResolver} handling beans configured via the standard XML configuration mechanism.
 * </p>
 * 
 * <p>
 * This resolver handles beans specified in faces-config.xml files. The resolver will pick up the default configuration
 * file <code>/WEB-INF/faces-config.xml</code>, all <code>/META-INF/faces-config.xml</code> found by the supplied
 * {@link ClassLoader} and all files specified via the <code>javax.faces.CONFIG_FILES</code> init parameter.
 * </p>
 * 
 * @author Christian Kaltepoth
 * 
 */
public class FacesConfigBeanNameResolver implements ELBeanNameResolver
{

   private final static Logger log = Logger.getLogger(FacesConfigBeanNameResolver.class);

   /**
    * Name of default <code>faces-config.xml</code> loaded via {@link ServletContext#getResource(String)}
    */
   private final static String WEB_INF_FACES_CONFIG_XML = "/WEB-INF/faces-config.xml";

   /**
    * Name of <code>faces-config.xml</code> loaded via {@link ClassLoader#getResource(String)}
    */
   private final static String META_INF_FACES_CONFIG_XML = "META-INF/faces-config.xml";

   /**
    * Lookup map: FQCN -> bean name
    */
   private final Map<String, String> beanNameMap = new HashMap<String, String>();

   /**
    * Initialization procedure. Reads all faces-config.xml files and registers the beans found.
    */
   public boolean init(final ServletContext servletContext, final ClassLoader classLoader)
   {

      // list of beans found
      List<FacesConfigEntry> facesConfigEntries = new ArrayList<FacesConfigEntry>();

      // get the list of faces configuration files to process
      Set<URL> facesConfigs = getFacesConfigFiles(servletContext, classLoader);

      // process all configuration files
      for (URL url : facesConfigs)
      {
         processFacesConfig(url, facesConfigEntries);
      }

      // Create bean name lookup map from all entries found
      for (FacesConfigEntry entry : facesConfigEntries)
      {
         beanNameMap.put(entry.getBeanClass(), entry.getName());
      }

      // debug statement containing number of classes found
      if (log.isDebugEnabled())
      {
         log.debug("Found " + beanNameMap.size() + " bean names in faces configuration.");
      }

      // we will always enable this resolver
      return true;
   }

   /**
    * Returns a set of faces-config.xml files. This set includes the default configuration file, all additional files
    * mentioned via the <code>javax.faces.CONFIG_FILES</code> init parameter and all files found in META-INF folders.
    * 
    * @param servletContext The ServletContext
    * @param classLoader The classloader used to find files in META-INF directories
    * @return A set of URLs
    */
   private Set<URL> getFacesConfigFiles(final ServletContext servletContext, final ClassLoader classLoader)
   {

      // set of URLs to process
      Set<URL> result = new HashSet<URL>();

      try
      {

         // get default faces-config.xml
         URL defaultFacesConfig = servletContext.getResource(WEB_INF_FACES_CONFIG_XML);
         if (defaultFacesConfig != null)
         {
            result.add(defaultFacesConfig);
         }

         // get additional configuration files from init parameter
         result.addAll(getConfigFilesFromInitParameter(servletContext));

         // Find configuration files META-INF directories
         try
         {
            Enumeration<URL> resources = classLoader.getResources(META_INF_FACES_CONFIG_XML);
            while (resources.hasMoreElements())
            {
               result.add(resources.nextElement());
            }
         }
         catch (IOException e)
         {
            log.error("Failed to load faces-config.xml files from META-INF directories", e);
         }

      }
      catch (MalformedURLException e)
      {
         // should not happen, because URLs are hard-coded
         throw new IllegalArgumentException(e);
      }

      return result;
   }

   /**
    * Returns a collection of faces configuration files mentioned via the default <code>javax.faces.CONFIG_FILES</code>
    * init parameter
    * 
    * @param servletContext The ServletContext
    * @return A collection of URLs (never null)
    */
   private Collection<URL> getConfigFilesFromInitParameter(final ServletContext servletContext)
   {

      // read init parameter
      String initParam = servletContext.getInitParameter(FacesServlet.CONFIG_FILES_ATTR);

      // empty? return empty set
      if ((initParam == null) || (initParam.trim().length() == 0))
      {
         return Collections.emptySet();
      }

      // split string at each comma
      String[] files = initParam.split(",");

      // the result
      Set<URL> result = new HashSet<URL>();

      // process each single file
      for (String file : files)
      {

         // ignore empty entries
         if (file.trim().length() == 0)
         {
            continue;
         }

         try
         {
            // try get URL for this file
            URL url = servletContext.getResource(file.trim());

            // add it to the result, if it exists
            if (url != null)
            {
               result.add(url);
            }
         }
         catch (MalformedURLException e)
         {
            // log on debug level, because the JSF implementation should
            // handle such a case
            log.debug("Invalid entry in javax.faces.CONFIG_FILES init parameter: " + file);
         }

      }

      return result;
   }

   /**
    * Process a single <code>faces-config.xml</code> file and add all beans found to the supplied list of
    * {@link FacesConfigEntry} objects.
    * 
    * @param url The faces-config.xml file
    * @param facesConfigEntries list of entries to add the beans to
    */
   private void processFacesConfig(final URL url, final List<FacesConfigEntry> facesConfigEntries)
   {

      // log name of current file
      if (log.isTraceEnabled())
      {
         log.trace("Loading bean names from: " + url.toString());
      }

      FacesConfigParser parser = new FacesConfigParser();

      InputStream stream = null;

      try
      {
         // open the file and let digester pares it
         stream = url.openStream();
         parser.parse(stream);

         // add new faces config entries
         facesConfigEntries.addAll(parser.getEntries());

      }
      catch (IOException e)
      {
         // may be thrown when reading the file
         log.info("Failed to parse: " + url.toString() + ": " + e.getMessage());
      }
      finally
      {
         // close stream
         if (stream != null)
         {
            try
            {
               stream.close();
            }
            catch (IOException e)
            {
               // ignore IOExceptions on close
            }

         }
      }

   }

   /*
    * Implementation if interface method
    */
   public String getBeanName(final Class<?> clazz)
   {
      // first check bean name map containing faces-config.xml entries
      String name = beanNameMap.get(clazz.getName());
      if (name != null)
      {
         return name;
      }

      return null;
   }

   /**
    * Private class to hold values of single "managed-bean" entries
    */
   public static class FacesConfigEntry
   {

      private String name;

      private String beanClass;

      public String getName()
      {
         return name;
      }

      public void setName(final String name)
      {
         this.name = name;
      }

      public String getBeanClass()
      {
         return beanClass;
      }

      public void setBeanClass(final String beanClass)
      {
         this.beanClass = beanClass;
      }

   }

   /**
    * Very simple parser to read the managed bean entries for a faces-config.xml
    */
   private static class FacesConfigParser extends SimpleXMLParserBase
   {

      private final List<FacesConfigEntry> entries = new ArrayList<FacesConfigEntry>();

      private FacesConfigEntry currentEntry = null;

      @Override
      public void processStartElement(final String name)
      {
         // <managed-bean>
         if (elements("faces-config", "managed-bean"))
         {
            currentEntry = new FacesConfigEntry();
         }
      }

      @Override
      public void processEndElement(final String name)
      {
         // </managed-bean>
         if (elements("faces-config", "managed-bean"))
         {
            entries.add(currentEntry);
            currentEntry = null;
         }
      }

      @Override
      public void processCharacters(final String text)
      {

         // <managed-bean-name>
         if (elements("faces-config", "managed-bean", "managed-bean-name"))
         {
            currentEntry.setName(text.trim());
         }

         // <managed-bean-class>
         if (elements("faces-config", "managed-bean", "managed-bean-class"))
         {
            currentEntry.setBeanClass(text.trim());
         }

      }

      public List<FacesConfigEntry> getEntries()
      {
         return entries;
      }

   }

}
