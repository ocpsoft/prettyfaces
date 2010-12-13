package com.ocpsoft.pretty.faces.spi;

import javax.servlet.ServletContext;

import com.ocpsoft.pretty.faces.util.ServiceLoader;

/**
 * <p>
 * Implementations of this interfaces help PrettyFaces in getting the name a
 * bean has in the EL context.
 * </p>
 * 
 * <p>
 * Implementations are looked up at runtime via the {@link ServiceLoader}
 * mechanism.
 * </p>
 * 
 * @author Christian Kaltepoth
 * 
 */
public interface ELBeanNameResolver
{
   /**
    * This method is called once prior to calls of {@link #getBeanName(Class)}
    * and should be used to initialize the resolver. Implementations must return
    * <code>true</code> when the initialization succeeded. If <code>false</code>
    * is returned, the resolver will be disabled!
    * 
    * @param servletContext The {@link ServletContext}
    * @param classLoader A {@link ClassLoader} to use
    * @return <code>true</code> if initialization succeeded. <code>false</code>
    *         will disable the resolver.
    */
   public boolean init(ServletContext servletContext, ClassLoader classLoader);

   /**
    * This method returns the name of a bean with the supplied type. The method
    * MUST return <code>null</code>, if it cannot resolve the bean name or isn't
    * absolutely certain about it.
    * 
    * @param clazz Class of the bean
    * @return The bean name or <code>null</code>.
    */
   public String getBeanName(Class<?> clazz);

}
