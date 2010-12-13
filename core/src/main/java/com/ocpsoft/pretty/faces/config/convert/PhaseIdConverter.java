/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2010 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */

package com.ocpsoft.pretty.faces.config.convert;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;

public class PhaseIdConverter implements Converter
{
   @SuppressWarnings("rawtypes")
   public Object convert(final Class type, final Object value)
   {
      PhaseId result = null;
      if ("ANY_PHASE".equals(value))
      {
         result = PhaseId.ANY_PHASE;
      }
      else if ("APPLY_REQUEST_VALUES".equals(value))
      {
         result = PhaseId.APPLY_REQUEST_VALUES;
      }
      else if ("PROCESS_VALIDATIONS".equals(value))
      {
         result = PhaseId.PROCESS_VALIDATIONS;
      }
      else if ("UPDATE_MODEL_VALUES".equals(value))
      {
         result = PhaseId.UPDATE_MODEL_VALUES;
      }
      else if ("INVOKE_APPLICATION".equals(value))
      {
         result = PhaseId.INVOKE_APPLICATION;
      }
      else if ("RENDER_RESPONSE".equals(value))
      {
         result = PhaseId.RENDER_RESPONSE;
      }
      else
      {
         throw new ConversionException("Could not convert value: [" + value + "] to FacesPhaseId type.");
      }
      return result;
   }
}