package com.ocpsoft.pretty.faces.config;

import javax.faces.application.ProjectStage;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.util.FacesFactory;

public class PrettyConfigListener implements ServletRequestListener
{

   public void requestDestroyed(final ServletRequestEvent sre)
   {
   }

   public void requestInitialized(final ServletRequestEvent sre)
   {
      if (!ProjectStage.Production.equals(FacesFactory.getApplication().getProjectStage()) && !PrettyContext.isInstantiated(sre.getServletRequest()))
      {
         PrettyConfigurator configurator = new PrettyConfigurator(sre.getServletContext());
         configurator.configure();
      }
   }

}
