package com.ocpsoft.pretty.faces.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ocpsoft.pretty.faces.util.URLDuplicatePathCanonicalizer;

public class URLDuplicatePathCanonicalizerTest
{
    URLDuplicatePathCanonicalizer c = new URLDuplicatePathCanonicalizer();

    @Test
    public void testRemovesDuplicatePaths() throws Exception
    {
        String url = "http://ocpsoft.com/prettyfaces/../prettyfaces/";

        String expected = "http://ocpsoft.com/prettyfaces/";

        assertEquals(expected, c.canonicalize(url));
    }

    @Test
    public void testIgnoresRelativePaths() throws Exception
    {
        String url = "http://ocpsoft.com/prettyfaces/../scrumshark/";

        String expected = "http://ocpsoft.com/prettyfaces/../scrumshark/";

        assertEquals(expected, c.canonicalize(url));
    }
}
