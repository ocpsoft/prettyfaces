package com.ocpsoft.pretty.application;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.beans.MappingUrlBuilder;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;

public class PrettyRedirector
{
    private static final Log log = LogFactory.getLog(PrettyNavigationHandler.class);
    private final MappingUrlBuilder builder = new MappingUrlBuilder();

    public static PrettyRedirector getInstance()
    {
        return new PrettyRedirector();
    }

    public boolean redirect(final FacesContext context, final String action)
    {
        try
        {
            PrettyContext prettyContext = PrettyContext.getCurrentInstance();
            PrettyConfig config = prettyContext.getConfig();
            ExternalContext externalContext = context.getExternalContext();
            if (PrettyContext.PRETTY_PREFIX.equals(action) && prettyContext.isPrettyRequest())
            {
                String url = prettyContext.getOriginalRequestUrl();
                log.info("Refreshing requested page [" + url + "]");
                externalContext.redirect(externalContext.encodeActionURL(url));
                return true;
            }
            else if (isPrettyNavigationCase(action))
            {
                PrettyUrlMapping mapping = config.getMappingById(action);
                if (mapping != null)
                {
                    String url = builder.getURL(mapping);
                    log.info("Redirecting to mappingId [" + mapping.getId() + "], [" + url + "]");
                    externalContext.redirect(externalContext.encodeActionURL(url));
                }
                else
                {
                    throw new PrettyException("PrettyFaces: Invalid mapping id supplied to navigation handler: "
                            + action);
                }
                return true;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("PrettyFaces: redirect failed for target: " + action, e);
        }
        return false;
    }

    private boolean isPrettyNavigationCase(final String action)
    {
        PrettyContext prettyContext = PrettyContext.getCurrentInstance();
        PrettyConfig config = prettyContext.getConfig();
        return action != null && config.isMappingId(action) && action.trim().startsWith(PrettyContext.PRETTY_PREFIX);
    }
}
