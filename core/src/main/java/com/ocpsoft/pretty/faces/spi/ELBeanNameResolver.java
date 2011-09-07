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
package com.ocpsoft.pretty.faces.spi;

import javax.servlet.ServletContext;

import com.ocpsoft.common.services.ServiceLoader;

/**
 * <p>
 * Implementations of this interfaces help PrettyFaces in getting the name a bean has in the EL context.
 * </p>
 * 
 * <p>
 * Implementations are looked up at runtime via the {@link ServiceLoader} mechanism.
 * </p>
 * 
 * @author Christian Kaltepoth
 * 
 */
public interface ELBeanNameResolver
{
   /**
    * This method is called once prior to calls of {@link #getBeanName(Class)} and should be used to initialize the
    * resolver. Implementations must return <code>true</code> when the initialization succeeded. If <code>false</code>
    * is returned, the resolver will be disabled!
    * 
    * @param servletContext The {@link ServletContext}
    * @param classLoader A {@link ClassLoader} to use
    * @return <code>true</code> if initialization succeeded. <code>false</code> will disable the resolver.
    */
   public boolean init(ServletContext servletContext, ClassLoader classLoader);

   /**
    * This method returns the name of a bean with the supplied type. The method MUST return <code>null</code>, if it
    * cannot resolve the bean name or isn't absolutely certain about it.
    * 
    * @param clazz Class of the bean
    * @return The bean name or <code>null</code>.
    */
   public String getBeanName(Class<?> clazz);

}
