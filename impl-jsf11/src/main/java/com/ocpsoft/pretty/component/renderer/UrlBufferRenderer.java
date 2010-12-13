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
package com.ocpsoft.pretty.component.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import com.ocpsoft.pretty.component.PrettyUrlBuilder;
import com.ocpsoft.pretty.component.UrlBuffer;

public class UrlBufferRenderer extends Renderer
{
    public static final String RENDERER_TYPE = "com.ocpsoft.pretty.Url";
    private final PrettyUrlBuilder urlBuilder = new PrettyUrlBuilder();

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

        String href = context.getExternalContext().getRequestContextPath() + urlBuilder.buildMappedUrl(component);
        context.getExternalContext().getRequestMap().put(var, href);
    }
}