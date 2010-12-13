/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.application;

import java.io.IOException;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.PrettyContext;

public class PrettyViewHandler extends ViewHandler
{
   protected ViewHandler parent;

   public PrettyViewHandler(final ViewHandler viewHandler)
   {
      super();
      parent = viewHandler;
   }

   @Override
   public Locale calculateLocale(final FacesContext facesContext)
   {
      return parent.calculateLocale(facesContext);
   }

   @Override
   public String calculateRenderKitId(final FacesContext facesContext)
   {
      return parent.calculateRenderKitId(facesContext);
   }

   @Override
   public UIViewRoot createView(final FacesContext context, final String arg1)
   {
      return parent.createView(context, arg1);
   }

   @Override
   public String getActionURL(final FacesContext context, final String viewId)
   {
      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      if (prettyContext.isPrettyRequest() && !prettyContext.isInNavigation() && (viewId != null)
                && viewId.equals(context.getViewRoot().getViewId()))
      {
         return context.getExternalContext().encodeActionURL(prettyContext.getCalculatedActionUri());
      }
      else
      {
         return parent.getActionURL(context, viewId);
      }
   }

   @Override
   public String getResourceURL(final FacesContext facesContext, final String arg1)
   {
      return parent.getResourceURL(facesContext, arg1);
   }

   @Override
   public void renderView(final FacesContext facesContext, final UIViewRoot arg1) throws IOException, FacesException
   {
      parent.renderView(facesContext, arg1);
   }

   @Override
   public UIViewRoot restoreView(final FacesContext facesContext, final String arg1)
   {
      return parent.restoreView(facesContext, arg1);
   }

   @Override
   public void writeState(final FacesContext facesContext) throws IOException
   {
      parent.writeState(facesContext);
   }

}