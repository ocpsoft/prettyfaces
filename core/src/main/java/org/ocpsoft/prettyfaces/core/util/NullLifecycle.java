package org.ocpsoft.prettyfaces.core.util;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

public class NullLifecycle extends Lifecycle
{

   @Override
   public void addPhaseListener(PhaseListener arg0)
   {
      // ignore
   }

   @Override
   public void execute(FacesContext arg0) throws FacesException
   {
      // ignore
   }

   @Override
   public PhaseListener[] getPhaseListeners()
   {
      return new PhaseListener[0];
   }

   @Override
   public void removePhaseListener(PhaseListener arg0)
   {
      // ignore
   }

   @Override
   public void render(FacesContext arg0) throws FacesException
   {
      // ignore
   }

}
