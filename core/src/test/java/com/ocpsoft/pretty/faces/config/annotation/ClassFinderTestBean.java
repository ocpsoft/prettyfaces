package com.ocpsoft.pretty.faces.config.annotation;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

/**
 * Simple test bean used by {@link WebClassesFinderTest} and
 * {@link WebLibFinderTest}.
 * 
 * @author Christian Kaltepoth
 */
@URLMapping(id="test", pattern="/test", viewId="/test.jsf")
public class ClassFinderTestBean
{
   // Nothing
}
