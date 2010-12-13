/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.ocpsoft.pretty.faces.util;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class PhaseIdComparator
{

   /**
    * @param phaseId
    * @param currentPhaseId
    * @return
    */
   public static boolean equals(PhaseId phaseId, javax.faces.event.PhaseId currentPhaseId)
   {
      switch (phaseId)
      {
      case ANY_PHASE:
         return javax.faces.event.PhaseId.ANY_PHASE.equals(currentPhaseId);
      case RESTORE_VIEW:
         return javax.faces.event.PhaseId.RESTORE_VIEW.equals(currentPhaseId);
      case APPLY_REQUEST_VALUES:
         return javax.faces.event.PhaseId.APPLY_REQUEST_VALUES.equals(currentPhaseId);
      case PROCESS_VALIDATIONS:
         return javax.faces.event.PhaseId.PROCESS_VALIDATIONS.equals(currentPhaseId);
      case UPDATE_MODEL_VALUES:
         return javax.faces.event.PhaseId.UPDATE_MODEL_VALUES.equals(currentPhaseId);
      case INVOKE_APPLICATION:
         return javax.faces.event.PhaseId.INVOKE_APPLICATION.equals(currentPhaseId);
      case RENDER_RESPONSE:
         return javax.faces.event.PhaseId.RENDER_RESPONSE.equals(currentPhaseId);
      }
      return false;
   }
}
