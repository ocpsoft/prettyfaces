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
 * along with this program. If not, see the file COPYING.LESSER or visit the GNU
 * website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.component.tag;

import javax.faces.component.UIComponent;

import com.ocpsoft.pretty.faces.component.Link;
import com.ocpsoft.pretty.faces.component.PrettyTagBase;
import com.ocpsoft.pretty.faces.component.renderer.LinkRenderer;

/**
 * @author Derek Hollis <derek@ocpsoft.com>
 */
public class LinkTag extends PrettyTagBase
{
    private String mappingId;
    private String accesskey;
    private String anchor;
    private String charset;
    private String coords;
    private String dir;
    private String hreflang;
    private String lang;
    private String onblur;
    private String onclick;
    private String ondblclick;
    private String onfocus;
    private String onkeydown;
    private String onkeypress;
    private String onkeyup;
    private String onmousedown;
    private String onmousemove;
    private String onmouseout;
    private String onmouseover;
    private String onmouseup;
    private String rel;
    private String rev;
    private String shape;
    private String style;
    private String styleClass;
    private String tabindex;
    private String target;
    private String title;
    private String type;

    @Override
    public String getComponentType()
    {
        return Link.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType()
    {
        return LinkRenderer.RENDERER_TYPE;
    }

    @Override
    public void release()
    {
        super.release();

        mappingId = null;
        accesskey = null;
        anchor = null;
        charset = null;
        coords = null;
        dir = null;
        hreflang = null;
        lang = null;
        onblur = null;
        onclick = null;
        ondblclick = null;
        onfocus = null;
        onkeydown = null;
        onkeypress = null;
        onkeyup = null;
        onmousedown = null;
        onmousemove = null;
        onmouseout = null;
        onmouseover = null;
        onmouseup = null;
        rel = null;
        rev = null;
        shape = null;
        style = null;
        styleClass = null;
        tabindex = null;
        target = null;
        title = null;
        type = null;
    }

    @Override
    protected void setProperties(final UIComponent component)
    {
        super.setProperties(component);

        setAttributeProperites(component, "mappingId", mappingId);

        setAttributeProperites(component, "accesskey", accesskey);

        setAttributeProperites(component, "anchor", anchor);

        setAttributeProperites(component, "charset", charset);

        setAttributeProperites(component, "coords", coords);

        setAttributeProperites(component, "dir", dir);

        setAttributeProperites(component, "hreflang", hreflang);

        setAttributeProperites(component, "lang", lang);

        setAttributeProperites(component, "onblur", onblur);

        setAttributeProperites(component, "onclick", onclick);

        setAttributeProperites(component, "ondblclick", ondblclick);

        setAttributeProperites(component, "onfocus", onfocus);

        setAttributeProperites(component, "onkeydown", onkeydown);

        setAttributeProperites(component, "onkeypress", onkeypress);

        setAttributeProperites(component, "onkeyup", onkeyup);

        setAttributeProperites(component, "onmousedown", onmousedown);

        setAttributeProperites(component, "onmousemove", onmousemove);

        setAttributeProperites(component, "onmouseout", onmouseout);

        setAttributeProperites(component, "onmouseover", onmouseover);

        setAttributeProperites(component, "onmouseup", onmouseup);

        setAttributeProperites(component, "rel", rel);

        setAttributeProperites(component, "rev", rev);

        setAttributeProperites(component, "shape", shape);

        setAttributeProperites(component, "style", style);

        setAttributeProperites(component, "styleClass", styleClass);

        setAttributeProperites(component, "tabindex", tabindex);

        setAttributeProperites(component, "target", target);

        setAttributeProperites(component, "title", title);

        setAttributeProperites(component, "type", type);

    }

    public String getMappingId()
    {
        return mappingId;
    }

    public void setMappingId(final String mappingId)
    {
        this.mappingId = mappingId;
    }

    public String getAccesskey()
    {
        return accesskey;
    }

    public String getAnchor()
    {
        return anchor;
    }

    public String getCharset()
    {
        return charset;
    }

    public String getCoords()
    {
        return coords;
    }

    public String getDir()
    {
        return dir;
    }

    public String getHreflang()
    {
        return hreflang;
    }

    public String getLang()
    {
        return lang;
    }

    public String getOnblur()
    {
        return onblur;
    }

    public String getOnclick()
    {
        return onclick;
    }

    public String getOndblclick()
    {
        return ondblclick;
    }

    public String getOnfocus()
    {
        return onfocus;
    }

    public String getOnkeydown()
    {
        return onkeydown;
    }

    public String getOnkeypress()
    {
        return onkeypress;
    }

    public String getOnkeyup()
    {
        return onkeyup;
    }

    public String getOnmousedown()
    {
        return onmousedown;
    }

    public String getOnmousemove()
    {
        return onmousemove;
    }

    public String getOnmouseout()
    {
        return onmouseout;
    }

    public String getOnmouseover()
    {
        return onmouseover;
    }

    public String getOnmouseup()
    {
        return onmouseup;
    }

    public String getRel()
    {
        return rel;
    }

    public String getRev()
    {
        return rev;
    }

    public String getShape()
    {
        return shape;
    }

    public String getStyle()
    {
        return style;
    }

    public String getStyleClass()
    {
        return styleClass;
    }

    public String getTabindex()
    {
        return tabindex;
    }

    public String getTarget()
    {
        return target;
    }

    public String getTitle()
    {
        return title;
    }

    public String getType()
    {
        return type;
    }

    public void setAccesskey(final String accesskey)
    {
        this.accesskey = accesskey;
    }

    public void setAnchor(final String anchor)
    {
        this.anchor = anchor;
    }

    public void setCharset(final String charset)
    {
        this.charset = charset;
    }

    public void setCoords(final String coords)
    {
        this.coords = coords;
    }

    public void setDir(final String dir)
    {
        this.dir = dir;
    }

    public void setHreflang(final String hreflang)
    {
        this.hreflang = hreflang;
    }

    public void setLang(final String lang)
    {
        this.lang = lang;
    }

    public void setOnblur(final String onblur)
    {
        this.onblur = onblur;
    }

    public void setOnclick(final String onclick)
    {
        this.onclick = onclick;
    }

    public void setOndblclick(final String ondblclick)
    {
        this.ondblclick = ondblclick;
    }

    public void setOnfocus(final String onfocus)
    {
        this.onfocus = onfocus;
    }

    public void setOnkeydown(final String onkeydown)
    {
        this.onkeydown = onkeydown;
    }

    public void setOnkeypress(final String onkeypress)
    {
        this.onkeypress = onkeypress;
    }

    public void setOnkeyup(final String onkeyup)
    {
        this.onkeyup = onkeyup;
    }

    public void setOnmousedown(final String onmousedown)
    {
        this.onmousedown = onmousedown;
    }

    public void setOnmousemove(final String onmousemove)
    {
        this.onmousemove = onmousemove;
    }

    public void setOnmouseout(final String onmouseout)
    {
        this.onmouseout = onmouseout;
    }

    public void setOnmouseover(final String onmouseover)
    {
        this.onmouseover = onmouseover;
    }

    public void setOnmouseup(final String onmouseup)
    {
        this.onmouseup = onmouseup;
    }

    public void setRel(final String rel)
    {
        this.rel = rel;
    }

    public void setRev(final String rev)
    {
        this.rev = rev;
    }

    public void setShape(final String shape)
    {
        this.shape = shape;
    }

    public void setStyle(final String style)
    {
        this.style = style;
    }

    public void setStyleClass(final String styleClass)
    {
        this.styleClass = styleClass;
    }

    public void setTabindex(final String tabindex)
    {
        this.tabindex = tabindex;
    }

    public void setTarget(final String target)
    {
        this.target = target;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public void setType(final String type)
    {
        this.type = type;
    }
}
