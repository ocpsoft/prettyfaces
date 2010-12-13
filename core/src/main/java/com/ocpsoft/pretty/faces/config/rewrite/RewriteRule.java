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

package com.ocpsoft.pretty.faces.config.rewrite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class RewriteRule
{
    private String match = "";
    private String substitute = "";
    private String processor = "";
    private String url = "";
    private Redirect redirect = Redirect.PERMANENT;
    private boolean inbound = true;
    private boolean outbound = true;
    private Case toCase = Case.IGNORE;
    private TrailingSlash trailingSlash = TrailingSlash.IGNORE;

    private Pattern pattern = Pattern.compile("");

    /**
     * Return true if the <b>match</b> field contains a regex that matches some
     * or all of the given URL, false if no match is found.
     */
    public boolean matches(final String url)
    {
        if (url != null)
        {
            if (getMatch().length() == 0)
            {
                return true;
            }
            else
            {
                Matcher m = pattern.matcher(url).useAnchoringBounds(outbound);
                while (m.find())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public String getMatch()
    {
        return match == null ? "" : match;
    }

    public void setMatch(final String match)
    {
        pattern = Pattern.compile(match);
        this.match = match;
    }

    public String getSubstitute()
    {
        return substitute == null ? "" : substitute;
    }

    public void setSubstitute(final String substitute)
    {
        this.substitute = substitute;
    }

    public boolean isInbound()
    {
        return inbound;
    }

    public void setInbound(final boolean inbound)
    {
        this.inbound = inbound;
    }

    public boolean isOutbound()
    {
        return outbound;
    }

    public void setOutbound(final boolean outbound)
    {
        this.outbound = outbound;
    }

    public Case getToCase()
    {
        return toCase == null ? Case.IGNORE : toCase;
    }

    public void setToCase(final Case toCase)
    {
        this.toCase = toCase;
    }

    public TrailingSlash getTrailingSlash()
    {
        return trailingSlash == null ? TrailingSlash.IGNORE : trailingSlash;
    }

    public void setTrailingSlash(final TrailingSlash trailingSlash)
    {
        this.trailingSlash = trailingSlash;
    }

    public Pattern getPattern()
    {
        return pattern;
    }

    public String getProcessor()
    {
        return processor == null ? "" : processor;
    }

    public void setProcessor(final String processor)
    {
        this.processor = processor;
    }

    public Redirect getRedirect()
    {
        return redirect == null ? Redirect.PERMANENT : redirect;
    }

    public void setRedirect(final Redirect redirect)
    {
        this.redirect = redirect;
    }

    public String getUrl()
    {
        return url == null ? "" : url;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "RewriteRule [inbound=" + inbound + ",  match=" + match + ", outbound=" + outbound + ", pattern="
                + pattern + ", processor=" + processor + ", redirect=" + redirect + ", substitute=" + substitute
                + ", toCase=" + toCase + ", trailingSlash=" + trailingSlash + ", url=" + url + "]";
    }
}
