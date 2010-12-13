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
package com.ocpsoft.pretty.config;

import java.util.LinkedList;
import java.util.List;

/**
 * Pretty Faces configuration builder. Accepts configuration elements (
 * {@link #addMapping(PrettyUrlMapping)}) and builds the configuration (
 * {@link #build()}).
 * 
 * @author Aleksei Valikov
 */
public class PrettyConfigBuilder
{

    private final List<PrettyUrlMapping> mappings = new LinkedList<PrettyUrlMapping>();

    public void addMapping(final PrettyUrlMapping mapping)
    {
        if (mapping == null)
        {
            throw new IllegalArgumentException("Mapping must not be null.");
        }
        mappings.add(mapping);
    }

    public PrettyConfig build()
    {
        final PrettyConfig config = new PrettyConfig();
        for (PrettyUrlMapping mapping : mappings)
        {
            config.addMapping(mapping);
        }
        return config;
    }

}
