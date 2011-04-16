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
package com.ocpsoft.pretty.faces.config;

import javax.faces.application.ProjectStage;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.util.FacesFactory;

public class PrettyConfigListener implements ServletRequestListener
{

   /**
    * The configuration is reloaded after this amount of time
    */
   private final static long CONFIG_RELOAD_DELAY = 2000l;

   /**
    * Keeps track of the last time the configuration was updated
    */
   private long lastUpdate = 0;

   public void requestDestroyed(final ServletRequestEvent sre)
   {
      // nothing
   }

   public void requestInitialized(final ServletRequestEvent sre)
   {

      // Don't do this if the project stage is 'production'
      if (!ProjectStage.Production.equals(FacesFactory.getApplication().getProjectStage()) && !PrettyContext.isInstantiated(sre.getServletRequest()))
      {

         // the point in time the configuration will be reloaded
         long nextUpdate = lastUpdate + CONFIG_RELOAD_DELAY;

         if (System.currentTimeMillis() > nextUpdate)
         {

            /*
             * first update the 'lastUpdate' so that concurrent requests won't
             * also do an update of the configuration.
             */
            lastUpdate = System.currentTimeMillis();

            // run the configuration procedure again
            PrettyConfigurator configurator = new PrettyConfigurator(sre.getServletContext());
            configurator.configure();
         }

      }
   }

}
