package org.ocpsoft.prettyfaces.annotation.handlers;

import java.lang.annotation.Annotation;

import javax.faces.event.PhaseId;

import org.ocpsoft.prettyfaces.annotation.AfterPhase;
import org.ocpsoft.prettyfaces.annotation.BeforePhase;
import org.ocpsoft.rewrite.annotation.spi.AnnotationHandler;
import org.ocpsoft.rewrite.faces.config.PhaseOperation;

public class AfterPhaseHandler extends PhaseHandlerBase<AfterPhase> implements AnnotationHandler<AfterPhase>
{

   @Override
   public Class<AfterPhase> handles()
   {
      return AfterPhase.class;
   }

   @Override
   protected void applyPhaseRestriction(PhaseOperation<?> deferredOperation, AfterPhase annotation)
   {
      PhaseId phaseId = ((AfterPhase) annotation).value().getPhaseId();
      deferredOperation.after(phaseId);
   }

   @Override
   protected Class<? extends Annotation> getForbiddenAnnotation()
   {
      return BeforePhase.class;
   }

}
