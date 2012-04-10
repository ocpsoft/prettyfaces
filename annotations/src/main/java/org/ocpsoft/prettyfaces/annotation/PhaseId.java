package org.ocpsoft.prettyfaces.annotation;

public enum PhaseId
{

   RESTORE_VIEW(javax.faces.event.PhaseId.RESTORE_VIEW),
   APPLY_REQUEST_VALUES(javax.faces.event.PhaseId.APPLY_REQUEST_VALUES),
   PROCESS_VALIDATIONS(javax.faces.event.PhaseId.PROCESS_VALIDATIONS),
   UPDATE_MODEL_VALUES(javax.faces.event.PhaseId.UPDATE_MODEL_VALUES),
   INVOKE_APPLICATION(javax.faces.event.PhaseId.INVOKE_APPLICATION),
   RENDER_RESPONSE(javax.faces.event.PhaseId.RENDER_RESPONSE);

   private final javax.faces.event.PhaseId phaseId;

   private PhaseId(javax.faces.event.PhaseId phaseId)
   {
      this.phaseId = phaseId;
   }

   public javax.faces.event.PhaseId getPhaseId()
   {
      return phaseId;
   }

}
