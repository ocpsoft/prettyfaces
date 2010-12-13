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