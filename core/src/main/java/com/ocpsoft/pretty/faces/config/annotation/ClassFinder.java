package com.ocpsoft.pretty.faces.config.annotation;

/**
 * Common interface for implementations that scan for classes on the classpath.
 * 
 * @author Christian Kaltepoth
 */
public interface ClassFinder
{

   /**
    * Starting to search for classes. Every class found by the finder will be
    * reported to the supplied {@link PrettyAnnotationHandler}.
    * 
    * @param classHandler
    *            The handler to notify
    */
   public void findClasses(PrettyAnnotationHandler classHandler);

}