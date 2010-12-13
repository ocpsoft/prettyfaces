package com.ocpsoft.pretty.faces.config.dynaview;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynaviewEngine
{

    public static final String DYNAVIEW = "com.ocpsoft.dynaView";

    /**
     * Given the string value of the Faces Servlet mapping, return a string that
     * is guaranteed to match when a servlet forward is issued. It doesn't
     * matter which FacesServlet we get to, as long as we get to one.
     */
    public String buildDynaViewId(final String facesServletMapping)
    {
        StringBuffer result = new StringBuffer();

        Map<Pattern, String> patterns = new HashMap<Pattern, String>();

        Pattern pathMapping = Pattern.compile("^(/.*/)\\*$");
        Pattern extensionMapping = Pattern.compile("^\\*(\\..*)$");
        Pattern defaultMapping = Pattern.compile("^/$");

        patterns.put(pathMapping, "$1" + DYNAVIEW + ".jsf");
        patterns.put(extensionMapping, "/" + DYNAVIEW + "$1");
        patterns.put(defaultMapping, "/" + DYNAVIEW + ".jsf");

        boolean matched = false;
        Iterator<Pattern> iterator = patterns.keySet().iterator();
        while (matched == false && iterator.hasNext())
        {
            Pattern p = iterator.next();
            Matcher m = p.matcher(facesServletMapping);
            if (m.matches())
            {
                String replacement = patterns.get(p);
                m.appendReplacement(result, replacement);
                matched = true;
            }
        }

        if (matched == false)
        {
            // This is an exact url-mapping, use it.
            result.append(facesServletMapping);
        }

        return result.toString();
    }
}
