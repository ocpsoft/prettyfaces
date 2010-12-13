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
package com.ocpsoft.pretty.component;

import javax.faces.component.UIComponentBase;

/**
 * @author lb3
 */
public class UrlBuffer extends UIComponentBase
{
    public static final String COMPONENT_TYPE = "com.ocpsoft.pretty.UrlBuffer";
    public static final String RENDERER_TYPE = "com.ocpsoft.pretty.UrlBuffer";
    private static final String PRETTY_FACES_FAMILY = "PRETTY_FACES_FAMILY";

    public UrlBuffer()
    {
        super();
    }

    @Override
    public String getFamily()
    {
        return PRETTY_FACES_FAMILY;
    }
}
