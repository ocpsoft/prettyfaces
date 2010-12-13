package com.ocpsoft.pretty.component.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.ocpsoft.pretty.component.Link;
import com.ocpsoft.pretty.component.PrettyUrlBuilder;

public class LinkRenderer extends Renderer
{
    public static final String RENDERER_TYPE = "javax.faces.Link";
    private final PrettyUrlBuilder urlBuilder = new PrettyUrlBuilder();

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

        writer.startElement("a", link);

        String href = context.getExternalContext().getRequestContextPath() + urlBuilder.buildMappedUrl(component);
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

        super.encodeEnd(context, component);

        if (!component.isRendered())
        {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("a");
    }
}