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
package com.ocpsoft.pretty.faces.event;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.application.PrettyRedirector;
import com.ocpsoft.pretty.faces.beans.ActionExecutor;
import com.ocpsoft.pretty.faces.beans.ExtractedValuesURLBuilder;
import com.ocpsoft.pretty.faces.beans.ParameterInjector;
import com.ocpsoft.pretty.faces.beans.ParameterValidator;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.FacesElUtils;
import com.ocpsoft.pretty.faces.util.FacesMessagesUtils;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PrettyPhaseListener implements PhaseListener
{
   private static final long serialVersionUID = 2345410822999587673L;
   private static final Log log = LogFactory.getLog(PrettyPhaseListener.class);
   private static FacesElUtils elUtils = new FacesElUtils();
   private final FacesMessagesUtils messagesUtils = new FacesMessagesUtils();
   private final ActionExecutor executor = new ActionExecutor();
   private final ParameterInjector injector = new ParameterInjector();
   private final ParameterValidator validator = new ParameterValidator();

   public PhaseId getPhaseId()
   {
      return PhaseId.ANY_PHASE;
   }

   public void beforePhase(final PhaseEvent event)
   {
      FacesContext facesContext = event.getFacesContext();
      if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId()))
      {
         PrettyContext prettyContext = PrettyContext.getCurrentInstance();
         if (prettyContext.shouldProcessDynaview())
         {
            // We are only using this lifecycle to access the EL-Context.
            // End the faces lifecycle and finish processing after the Phase
            UIViewRoot viewRoot = facesContext.getViewRoot();
            if (viewRoot == null)
            {
               viewRoot = new UIViewRoot();
               viewRoot.setViewId("/com.ocpsoft.Dynaview.xhtml");
               facesContext.setViewRoot(viewRoot);
            }
            facesContext.responseComplete();
         }
      }
      else if (!facesContext.getResponseComplete())
      {
         FacesContext context = facesContext;
         messagesUtils.restoreMessages(context, context.getExternalContext().getRequestMap());
         processEvent(event);
      }
   }

   public void afterPhase(final PhaseEvent event)
   {
      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId()))
      {
         // TODO Test that validation occurs before injection
         /*
          * Parameter validation and injection must occur after RESTORE_VIEW in
          * order to participate in faces-navigation.
          */
         validator.validateParameters(event.getFacesContext());
         injector.injectParameters(event.getFacesContext());

         if (prettyContext.shouldProcessDynaview())
         {
            processDynaView(prettyContext, event.getFacesContext());
         }
         else if (!event.getFacesContext().getResponseComplete())
         {
            FacesContext context = event.getFacesContext();
            messagesUtils.restoreMessages(context, context.getExternalContext().getRequestMap());
            processEvent(event);
         }
      }
   }

   /**
    * Handle DynaView processing. This method will end the Faces life-cycle.
    */
   private void processDynaView(final PrettyContext prettyContext, final FacesContext facesContext)
   {
      log.trace("Requesting DynaView processing for: " + prettyContext.getRequestURL());
      String viewId = "";
      try
      {
         viewId = prettyContext.getCurrentViewId();
         log.trace("Invoking DynaView method: " + viewId);
         Object result = computeDynaViewId();
         if (result instanceof String)
         {
            viewId = (String) result;
            log.trace("Forwarding to DynaView: " + viewId);
            prettyContext.setDynaviewProcessed(true);
            facesContext.getExternalContext().dispatch(viewId);
            facesContext.responseComplete();
         }
      }
      catch (Exception e)
      {
         PrettyRedirector prettyRedirector = new PrettyRedirector();
         prettyRedirector.send404(facesContext);
         throw new PrettyException("Could not forward to view: " + viewId + "", e);
      }
   }

   /**
    * Calculate the Faces ViewId to which this request URI resolves. This method
    * will recursively call any dynamic mapping viewId functions as needed until
    * a String viewId is returned, or supplied by a static mapping.
    * <p>
    * This phase does not support FacesNavigation or PrettyRedirecting. Its SOLE
    * purpose is to resolve a viewId.
    * <p>
    * <i><b>Note:</b> Precondition - parameter injection must take place before
    * this</i>
    * <p>
    * <i>Postcondition - currentViewId is set to computed View Id</i>
    * 
    * @return JSF viewID to which this request resolves.
    */
   private String computeDynaViewId()
   {
      String result = "";

      PrettyContext context = PrettyContext.getCurrentInstance();
      FacesContext facesContext = FacesContext.getCurrentInstance();

      UrlMapping urlMapping = context.getCurrentMapping();
      if (urlMapping != null)
      {
         String viewId = urlMapping.getViewId();
         if (viewId == null)
         {
            viewId = "";
         }
         while (elUtils.isEl(viewId))
         {
            Object viewResult = elUtils.invokeMethod(facesContext, viewId);
            if (viewResult == null)
            {
               viewId = "";
               break;
            }
            else
            {
               viewId = viewResult.toString();
            }

            if (context.getConfig().isMappingId(viewId))
            {
               urlMapping = context.getConfig().getMappingById(viewId);
               viewId = urlMapping.getViewId();
               ExtractedValuesURLBuilder builder = new ExtractedValuesURLBuilder();
               result = context.getContextPath() + builder.buildURL(urlMapping).encode()
                        + builder.buildQueryString(urlMapping);
            }
            else
            {
               result = viewId;
            }
         }
         if ("".equals(viewId))
         {
            log.debug("ViewId for mapping with id <" + urlMapping.getId() + "> was blank");
         }
      }
      return context.stripContextPath(result);
   }

   private void processEvent(final PhaseEvent event)
   {
      FacesContext context = event.getFacesContext();

      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      UrlMapping mapping = prettyContext.getCurrentMapping();
      if (mapping != null)
      {
         executor.executeActions(context, event.getPhaseId(), mapping);
      }
   }
}
