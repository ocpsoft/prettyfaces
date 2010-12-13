/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER3 or visit the
 * GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.util;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

/**
 * @author lb3
 */
public class FacesStateUtils
{
   /**
    * Determine if the current request/FacesContext is in PostBack state
    */
   public static boolean isPostback(final FacesContext context)
   {
      RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
      RenderKit renderKit = factory.getRenderKit(context, RenderKitFactory.HTML_BASIC_RENDER_KIT);
      return renderKit.getResponseStateManager().isPostback(context);
   }
}
