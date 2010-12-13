package com.ocpsoft.pretty.faces.rewrite.processor;

import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.rewrite.Processor;

public class MockCustomClassProcessor implements Processor
{
    public static final String RESULT = "I_PROCESSED";

    public String process(final RewriteRule rule, final String url)
    {
        return RESULT;
    }
}
