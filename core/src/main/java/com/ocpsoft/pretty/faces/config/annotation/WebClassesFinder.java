package com.ocpsoft.pretty.faces.config.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 * Implementation of {@link ClassFinder} that searches for classes in the
 * <code>/WEB-INF/classes</code> directory of a web application.
 * 
 * @author Christian Kaltepoth
 */
public class WebClassesFinder extends AbstractClassFinder
{

   /**
    * The name of the <code>classes</code> directory
    */
   private final static String CLASSES_FOLDER = "/WEB-INF/classes/";

   /**
    * Initialization
    */
   public WebClassesFinder(ServletContext servletContext, ClassLoader classLoader, PackageFilter packageFilter)
   {
      super(servletContext, classLoader, packageFilter);
   }

   /*
    * @see
    * com.ocpsoft.pretty.faces.config.annotation.ClassFinder#findClasses(com
    * .ocpsoft.pretty.config.annotation.PrettyAnnotationHandler)
    */
   public void findClasses(PrettyAnnotationHandler handler)
   {
      try
      {
         // we start the recursive scan in the classes folder
         URL classesFolderUrl = servletContext.getResource(CLASSES_FOLDER);

         // abort if classes folder is missing
         if (classesFolderUrl == null)
         {
            log.warn("Cannot find classes folder: " + CLASSES_FOLDER);
            return;
         }

         // call recursive directory processing method
         processDirectory(classesFolderUrl, handler);

      }
      catch (MalformedURLException e)
      {
         throw new IllegalStateException("Invalid URL: " + e.getMessage(), e);
      }
   }

   /**
    * Scan for classes in a single directory. This method will call itself
    * recursively if it finds other directories and call
    * {@link #processClassURL(URL, PrettyAnnotationHandler)} when it finds a
    * file ending with ".class" and that is accepted by the
    * {@link PackageFilter}
    * 
    * @param directoryUrl
    *           The URL of the directory to scan
    * @param handler
    *           The handler class for classes found
    * @throws MalformedURLException
    *            for invalid URLs
    */
   @SuppressWarnings("unchecked")
   protected void processDirectory(URL directoryUrl, PrettyAnnotationHandler handler) throws MalformedURLException
   {
      // log directory name on trace level
      if (log.isTraceEnabled())
      {
         log.trace("Processing directory: " + directoryUrl.toString());
      }

      // build directory name relative to webapp root
      String relativeName = getWebappRelativeName(directoryUrl, CLASSES_FOLDER);

      // call getResourcePaths to get directory entries
      Set paths = servletContext.getResourcePaths(relativeName);

      // loop over all entries of the directory
      for (Object relativePath : paths)
      {

         // get full URL for this entry
         URL entryUrl = servletContext.getResource(relativePath.toString());

         // if this URL ends with .class it is a Java class
         if (entryUrl.getPath().endsWith(".class"))
         {

            // get class name
            String className = getClassName(entryUrl.toString(), CLASSES_FOLDER);

            // check filter
            if (mustProcessClass(className))
            {

               // the class file stream
               InputStream classFileStream = null;

               // close the stream in finally block
               try
               {

                  /*
                   * Try to open the .class file. If an IOException is thrown,
                   * we will scan it anyway.
                   */
                  try
                  {
                     classFileStream = entryUrl.openStream();
                  }
                  catch (IOException e)
                  {
                     if (log.isDebugEnabled())
                     {
                        log.debug("Cound not obtain InputStream for class file: "+entryUrl.toString(), e);
                     }
                  }

                  // analyze the class (with or without classFileStream)
                  processClass(className, classFileStream, handler);

               }
               finally
               {
                  try
                  {
                     if (classFileStream != null)
                     {
                        classFileStream.close();
                     }
                  }
                  catch (IOException e)
                  {
                     if (log.isDebugEnabled())
                     {
                        log.debug("Failed to close input stream: " + e.getMessage());
                     }
                  }
               }
            }

         }

         // if this URL ends with a slash, its a directory
         if (entryUrl.getPath().endsWith("/"))
         {

            // walk down the directory
            processDirectory(entryUrl, handler);

         }
      }
   }

}
