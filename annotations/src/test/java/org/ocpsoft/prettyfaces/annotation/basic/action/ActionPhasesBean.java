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
import org.ocpsoft.prettyfaces.annotation.URLAction;
import org.ocpsoft.prettyfaces.annotation.URLPattern;

@ManagedBean
@RequestScoped
@URLPattern("/action")
@ForwardTo("/faces/action-phases.xhtml")
public class ActionPhasesBean
{

   private final List<String> log = new ArrayList<String>();

   @URLAction
   public void actionDefaultPhase()
   {
      log.add("actionDefaultPhase:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   @URLAction
   @BeforePhase(Phase.RENDER_RESPONSE)
   public void actionBeforeRenderResponse()
   {
      log.add("actionBeforeRenderResponse:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   @URLAction
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