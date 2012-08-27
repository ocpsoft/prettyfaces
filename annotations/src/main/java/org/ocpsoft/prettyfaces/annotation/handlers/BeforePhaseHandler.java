package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.annotation.Annotation;

import javax.faces.event.PhaseId;

import org.ocpsoft.prettyfaces.annotation.AfterPhase;
import org.ocpsoft.prettyfaces.annotation.BeforePhase;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.faces.config.PhaseOperation;

public class BeforePhaseHandler extends PhaseHandlerBase<BeforePhase> implements AnnotationHandler<BeforePhase>
{

   @Override
   public Class<BeforePhase> handles()
   {
      return BeforePhase.class;
   }

   @Override
   protected void applyPhaseRestriction(PhaseOperation<?> deferredOperation, BeforePhase annotation)
   {
      PhaseId phaseId = ((BeforePhase) annotation).value().getPhaseId();
      deferredOperation.after(phaseId);
   }

   @Override
   protected Class<? extends Annotation> getForbiddenAnnotation()
   {
      return AfterPhase.class;
   }

}
