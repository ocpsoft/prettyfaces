/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
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
package com.ocpsoft.pretty.faces.config.mapping;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;
import com.ocpsoft.pretty.faces.el.ConstantExpression;
import com.ocpsoft.pretty.faces.el.PrettyExpression;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class UrlAction
{
   private PrettyExpression action;
   private PhaseId phaseId = PhaseId.RESTORE_VIEW;
   private boolean onPostback = true;

   /**
    * Create a new {@link UrlAction} with empty values
    */
   public UrlAction()
   {
   }

   /**
    * Creates a new {@link UrlAction} and creates a {@link ConstantExpression}
    * for the supplied EL method binding
    * 
    * @param action String representation of the EL action method
    */
   public UrlAction(final String action)
   {
      this.action = new ConstantExpression(action);
   }

   /**
    * Creates a new {@link UrlAction} and initialize it with the supplied
    * {@link PrettyExpression}
    * 
    * @param action The expression
    */
   public UrlAction(final PrettyExpression action)
   {
      this.action = action;
   }

   /**
    * Creates a new {@link UrlAction} and creates a {@link ConstantExpression}
    * for the supplied EL method binding
    * 
    * @param action String representation of the EL action method
    * @param phaseId Phase ID to set
    */
   public UrlAction(final String action, final PhaseId phaseId)
   {
      this.action = new ConstantExpression(action);
      this.phaseId = phaseId;
   }

   public PhaseId getPhaseId()
   {
      return phaseId;
   }

   public boolean onPostback()
   {
      return onPostback;
   }

   public void setOnPostback(final boolean onPostback)
   {
      this.onPostback = onPostback;
   }

   public void setPhaseId(final PhaseId phaseId)
   {
      this.phaseId = phaseId;
   }

   public PrettyExpression getAction()
   {
      return action;
   }

   public void setAction(final PrettyExpression action)
   {
      this.action = action;
   }

   /**
    * Extra setter method creating a {@link ConstantExpression}. Used only for
    * Digester only.
    * 
    * @param action String representation of the EL expression
    */
   public void setAction(final String action)
   {
      this.action = new ConstantExpression(action);
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (action == null ? 0 : action.hashCode());
      result = prime * result + (phaseId == null ? 0 : phaseId.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (!(obj instanceof UrlAction))
      {
         return false;
      }
      UrlAction other = (UrlAction) obj;
      if (action == null)
      {
         if (other.action != null)
         {
            return false;
         }
      }
      else if (!action.equals(other.action))
      {
         return false;
      }
      if (phaseId == null)
      {
         if (other.phaseId != null)
         {
            return false;
         }
      }
      else if (!phaseId.equals(other.phaseId))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "UrlAction [action=" + action + ", onPostback=" + onPostback + ", phaseId=" + phaseId + "]";
   }

}
