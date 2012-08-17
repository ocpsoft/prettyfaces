package org.ocpsoft.prettyfaces.annotation.basic.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.ocpsoft.prettyfaces.annotation.AfterPhase;
import org.ocpsoft.prettyfaces.annotation.BeforePhase;
import org.ocpsoft.prettyfaces.annotation.ForwardTo;
import org.ocpsoft.prettyfaces.annotation.Phase;
import org.ocpsoft.prettyfaces.annotation.RequestAction;
import org.ocpsoft.prettyfaces.annotation.PathPattern;

@ManagedBean
@RequestScoped
@PathPattern("/action")
@ForwardTo("/faces/action-phases.xhtml")
public class ActionPhasesBean
{

   private final List<String> log = new ArrayList<String>();

   @RequestAction
   public void actionDefaultPhase()
   {
      log.add("actionDefaultPhase:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   @RequestAction
   @BeforePhase(Phase.RENDER_RESPONSE)
   public void actionBeforeRenderResponse()
   {
      log.add("actionBeforeRenderResponse:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   @RequestAction
   @AfterPhase(Phase.INVOKE_APPLICATION)
   public void actionAfterInvokeApplication()
   {
      log.add("actionAfterInvokeApplication:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   public List<String> getLog()
   {
      return log;
   }

}