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

package com.ocpsoft.pretty.faces.component.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.component.UrlBuffer;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.PrettyURLBuilder;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class UrlBufferRenderer extends Renderer
{
   public static final String RENDERER_TYPE = "com.ocpsoft.pretty.Url";
   private final PrettyURLBuilder urlBuilder = new PrettyURLBuilder();

   @Override
   public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException
   {
      super.encodeBegin(context, component);
      if (!component.isRendered())
      {
         return;
      }

      UrlBuffer urlBuffer = (UrlBuffer) component;
      String var = (String) urlBuffer.getAttributes().get("var");

      String mappingId = (String) component.getAttributes().get("mappingId");
      if (mappingId == null)
      {
         throw new PrettyException("Mapping id was null when attempting to build URL for component: " + component.toString() + " <" + component.getClientId(context) + ">");
      }

      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      PrettyConfig prettyConfig = prettyContext.getConfig();
      UrlMapping urlMapping = prettyConfig.getMappingById(mappingId);

      String href = context.getExternalContext().getRequestContextPath() + urlBuilder.build(urlMapping, urlBuilder.extractParameters(component));

      context.getExternalContext().getRequestMap().put(var, href);
   }
}