package org.ocpsoft.prettyfaces.annotation.basic.binding;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.ocpsoft.prettyfaces.annotation.AfterPhase;
import org.ocpsoft.prettyfaces.annotation.BeforePhase;
import org.ocpsoft.prettyfaces.annotation.Join;
import org.ocpsoft.prettyfaces.annotation.ParameterBinding;
import org.ocpsoft.prettyfaces.annotation.Phase;

@ManagedBean
@RequestScoped
@Join(path = "/binding/{value}/", to = "/faces/binding-phases.xhtml")
public class BindingPhasesBean
{

   private final List<String> log = new ArrayList<String>();

   @ParameterBinding("value")
   private String defaultPhase;

   @ParameterBinding("value")
   @AfterPhase(Phase.INVOKE_APPLICATION)
   private String afterInvokeApplication;

   @ParameterBinding("value")
   @BeforePhase(Phase.RENDER_RESPONSE)
   private String beforeRenderResponse;

   public String getDefaultPhase()
   {
      return defaultPhase;
   }

   public void setDefaultPhase(String defaultPhase)
   {
      this.defaultPhase = defaultPhase;
      log.add("defaultPhase:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   public String getAfterInvokeApplication()
   {
      return afterInvokeApplication;
   }

   public void setAfterInvokeApplication(String afterInvokeApplication)
   {
      this.afterInvokeApplication = afterInvokeApplication;
      log.add("afterInvokeApplication:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   public String getBeforeRenderResponse()
   {
      return beforeRenderResponse;
   }

   public void setBeforeRenderResponse(String beforeRenderResponse)
   {
      this.beforeRenderResponse = beforeRenderResponse;
      log.add("beforeRenderResponse:" + FacesContext.getCurrentInstance().getCurrentPhaseId().toString());
   }

   public List<String> getLog()
   {
      return log;
   }

}
