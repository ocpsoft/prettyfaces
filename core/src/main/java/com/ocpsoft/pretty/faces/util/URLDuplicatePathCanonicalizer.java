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

package com.ocpsoft.pretty.faces.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class URLDuplicatePathCanonicalizer
{
    private final Pattern pattern = Pattern.compile("(/[^/]+/)\\.\\.(/[^/]+/)");

    /**
     * Canonicalize a given URL, replacing relative path structures with the
     * condensed path. (Eg. /xxxx/../xxxx/ => /xxxx/)
     * 
     * @return Return the canonicalized URL. If the URL requires no
     *         canonicalization, return the original unmodified URL.
     */
    public String canonicalize(final String url)
    {
        if (url != null && url.contains("/../"))
        {
            StringBuffer result = new StringBuffer();
            Matcher m = pattern.matcher(url);
            while (m.find())
            {
                if (m.group(1).equals(m.group(2)))
                {
                    m.appendReplacement(result, m.group(1));
                }
                m.appendTail(result);
            }
            return result.toString();
        }
        return url;
    }

}
