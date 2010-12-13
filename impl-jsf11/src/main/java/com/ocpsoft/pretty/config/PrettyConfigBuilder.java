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
