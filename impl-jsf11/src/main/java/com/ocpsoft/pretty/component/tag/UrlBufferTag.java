/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see the file COPYING.LESSER or visit the GNU website at
 * <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.component.tag;

import javax.faces.component.UIComponent;

import com.ocpsoft.pretty.component.PrettyTagBase;
import com.ocpsoft.pretty.component.UrlBuffer;

/**
 * @author lb3
 */
public class UrlBufferTag extends PrettyTagBase
{
    private String var;
    private String mappingId;

    @Override
    public String getComponentType()
    {
        return UrlBuffer.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType()
    {
        return UrlBuffer.RENDERER_TYPE;
    }

    @Override
    public void release()
    {
        super.release();
        var = null;
    }

    @Override
    protected void setProperties(final UIComponent component)
    {
        super.setProperties(component);
        setAttributeProperites(component, "var", var);
        setAttributeProperites(component, "mappingId", mappingId);

    }

    public String getVar()
    {
        return var;
    }

    public void setVar(final String var)
    {
        this.var = var;
    }

    public String getMappingId()
    {
        return mappingId;
    }

    public void setMappingId(final String mappingId)
    {
        this.mappingId = mappingId;
    }

}
