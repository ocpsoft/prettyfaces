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
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.component.Link;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.PrettyURLBuilder;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class LinkRenderer extends Renderer
{
   public static final String RENDERER_TYPE = "javax.faces.Link";
   private final PrettyURLBuilder urlBuilder = new PrettyURLBuilder();

   @Override
   public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException
   {
      super.encodeBegin(context, component);
      if (!component.isRendered())
      {
         return;
      }

      Link link = (Link) component;
      ResponseWriter writer = context.getResponseWriter();

      if (link.isDisabled())
      {
         writer.startElement("span", link);
      }
      else
      {
         writer.startElement("a", link);
      }

      String mappingId = (String) component.getAttributes().get("mappingId");
      if (mappingId == null)
      {
         throw new PrettyException("Mapping id was null when attempting to build URL for component: " + component.toString() + " <" + component.getClientId(context) + ">");
      }

      PrettyContext prettyContext = PrettyContext.getCurrentInstance();
      PrettyConfig prettyConfig = prettyContext.getConfig();
      UrlMapping urlMapping = prettyConfig.getMappingById(mappingId);

      String href = context.getExternalContext().getRequestContextPath() + urlBuilder.build(urlMapping, urlBuilder.extractParameters(component));

      if ((link.getAnchor() != null) && link.getAnchor().length() > 0)
      {
         href += "#" + link.getAnchor();
      }

      writer.writeURIAttribute("href", context.getExternalContext().encodeResourceURL(href), "href");

      writeAttr(writer, "id", link.getClientId(context));
      writeAttr(writer, "accesskey", link.getAccesskey());
      writeAttr(writer, "charset", link.getCharset());
      writeAttr(writer, "coords", link.getCoords());
      writeAttr(writer, "dir", link.getDir());
      writeAttr(writer, "hreflang", link.getHreflang());
      writeAttr(writer, "lang", link.getLang());
      writeAttr(writer, "onblur", link.getOnblur());
      writeAttr(writer, "onclick", link.getOnclick());
      writeAttr(writer, "ondblclick", link.getOndblclick());
      writeAttr(writer, "onfocus", link.getOnfocus());
      writeAttr(writer, "onkeydown", link.getOnkeydown());
      writeAttr(writer, "onkeypress", link.getOnkeypress());
      writeAttr(writer, "onkeyup", link.getOnkeyup());
      writeAttr(writer, "onmousedown", link.getOnmousedown());
      writeAttr(writer, "onmousemove", link.getOnmousemove());
      writeAttr(writer, "onmouseout", link.getOnmouseout());
      writeAttr(writer, "onmouseover", link.getOnmouseover());
      writeAttr(writer, "onmouseup", link.getOnmouseup());
      writeAttr(writer, "rel", link.getRel());
      writeAttr(writer, "rev", link.getRev());
      writeAttr(writer, "shape", link.getShape());
      writeAttr(writer, "style", link.getStyle());
      writeAttr(writer, "class", link.getStyleClass());
      writeAttr(writer, "tabindex", link.getTabindex());
      writeAttr(writer, "target", link.getTarget());
      writeAttr(writer, "title", link.getTitle());
      writeAttr(writer, "type", link.getType());

   }

   private void writeAttr(final ResponseWriter writer, final String name, final String value) throws IOException
   {
      if (value != null)
      {
         writer.writeAttribute(name, value, name);
      }
   }

   @Override
   public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException
   {
      Link link = (Link) component;
      super.encodeEnd(context, link);

      if (!link.isRendered())
      {
         return;
      }

      ResponseWriter writer = context.getResponseWriter();

      if (link.isDisabled())
      {
         writer.endElement("span");
      }
      else
      {
         writer.endElement("a");
      }
   }
}