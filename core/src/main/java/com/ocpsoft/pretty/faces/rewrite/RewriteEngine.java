/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2010 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */

package com.ocpsoft.pretty.faces.rewrite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.rewrite.processor.CaseProcessor;
import com.ocpsoft.pretty.faces.rewrite.processor.CustomClassProcessor;
import com.ocpsoft.pretty.faces.rewrite.processor.RegexProcessor;
import com.ocpsoft.pretty.faces.rewrite.processor.TrailingSlashProcessor;
import com.ocpsoft.pretty.faces.rewrite.processor.UrlProcessor;

/**
 * Process URL rewrites based on configuration
 * 
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class RewriteEngine
{
    private static List<Processor> processors;

    static
    {
        List<Processor> list = new ArrayList<Processor>();
        list.add(new RegexProcessor());
        list.add(new CaseProcessor());
        list.add(new TrailingSlashProcessor());
        list.add(new CustomClassProcessor());
        list.add(new UrlProcessor());
        processors = Collections.unmodifiableList(list);
    }

    /**
     * Rewrite the given URL using the provided {@link RewriteRule} object
     * as a
     * set of rules.
     * 
     * @return The rewritten URL, or the unchanged URL if no action was
     *         taken.
     */
    public String processInbound(final RewriteRule rule, final String url)
    {
        String result = url;
        if (rule != null && rule.isInbound() && rule.matches(url))
        {
            for (Processor p : processors)
            {
                result = p.process(rule, result);
            }
        }
        return result;
    }

    /**
     * Rewrite the given URL using the provided {@link RewriteRule} object.
     * Process the URL only if the rule is set to outbound="true"
     * 
     * @return The rewritten URL, or the unchanged URL if no action was
     *         taken.
     */
    public String processOutbound(final RewriteRule rule, final String url)
    {
        String result = url;
        if (rule != null && rule.isOutbound() && rule.matches(url))
        {
            for (Processor p : processors)
            {
                result = p.process(rule, result);
            }
        }
        return result;
    }

}
