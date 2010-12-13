package com.ocpsoft.pretty.config;

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
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.config.mapping.UrlAction;

/**
 * Digester-based implementation of {@link PrettyConfigParser}.
 */
public class DigesterPrettyConfigParser implements PrettyConfigParser
{

    public void parse(final PrettyConfigBuilder builder, final InputStream resource) throws IOException, SAXException
    {
        if (builder == null)
        {
            throw new IllegalArgumentException("Builder must not be null.");
        }
        if (resource == null)
        {
            throw new IllegalArgumentException("Input stream must not be null.");
        }
        final Digester digester = configureDigester(new Digester());
        digester.push(builder);
        digester.parse(resource);
    }

    private Digester configureDigester(final Digester digester)
    {
        // Assume that target builder will be pushed into digester stack
        // externally

        // Create Mapping Object
        digester.addObjectCreate("pretty-config/url-mapping", PrettyUrlMapping.class);
        digester.addSetProperties("pretty-config/url-mapping");
        digester.addCallMethod("pretty-config/url-mapping/pattern", "setPattern", 0);

        // Parse Query Params
        digester.addObjectCreate("pretty-config/url-mapping/query-param", QueryParameter.class);
        digester.addSetProperties("pretty-config/url-mapping/query-param");
        digester.addCallMethod("pretty-config/url-mapping/query-param", "setExpression", 0);
        digester.addSetNext("pretty-config/url-mapping/query-param", "addQueryParam");

        // Parse Action Methods
        digester.addObjectCreate("pretty-config/url-mapping/action", UrlAction.class);
        digester.addSetProperties("pretty-config/url-mapping/action");
        digester.addCallMethod("pretty-config/url-mapping/action", "setPhaseId", 1);
        digester.addCallParam("pretty-config/url-mapping/action", 0, "phaseId");
        digester.addCallMethod("pretty-config/url-mapping/action", "setAction", 0);
        digester.addSetNext("pretty-config/url-mapping/action", "addAction");

        // Parse View Id
        digester.addCallMethod("pretty-config/url-mapping/view-id", "setViewId", 0);

        // Add Mappings to Config
        digester.addSetNext("pretty-config/url-mapping", "addMapping");
        return digester;
    }
}
