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

package com.ocpsoft.pretty.faces.config.rewrite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ocpsoft.pretty.faces.config.types.RewriteElement;

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
     * Creates an empty {@link RewriteRule}
     */
    public RewriteRule()
    {
        // nothing
    }

    /**
     * Creates an rewrite rule from the supplied JAXB object
     * 
     * @param element
     *            The JAXB object
     */
    public RewriteRule(RewriteElement element)
    {
        if (element.getMatch() != null)
        {
            match = element.getMatch().trim();
            pattern = Pattern.compile(match);
        }
        if (element.getSubstitute() != null)
        {
            substitute = element.getSubstitute().trim();
        }
        if (element.getProcessor() != null)
        {
            processor = element.getProcessor().trim();
        }
        if (element.getUrl() != null)
        {
            url = element.getUrl().trim();
        }
        if (element.isInbound() != null)
        {
            inbound = element.isInbound();
        }
        if (element.isOutbound() != null)
        {
            outbound = element.isOutbound();
        }
        if (element.getRedirect() != null)
        {
            if ("301".equals(element.getRedirect().trim()))
            {
                redirect = Redirect.PERMANENT;
            }
            else if ("302".equals(element.getRedirect().trim()))
            {
                redirect = Redirect.TEMPORARY;
            }
            else
            {
                redirect = Redirect.valueOf(element.getRedirect().trim().toUpperCase());
            }
        }
        if (element.getToCase() != null)
        {
            toCase = Case.valueOf(element.getToCase().name().toUpperCase());
        }
        if (element.getTrailingSlash() != null)
        {
            trailingSlash = TrailingSlash.valueOf(element.getTrailingSlash().name().toUpperCase());
        }
    }
    
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
