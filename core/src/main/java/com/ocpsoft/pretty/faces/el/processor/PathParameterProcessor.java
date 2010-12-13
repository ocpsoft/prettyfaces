package com.ocpsoft.pretty.faces.el.processor;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

public interface PathParameterProcessor
{
   /**
    * This method must call {@link PathParameter#setExpressionIsEL(boolean)} if
    * the parameter's expression is EL discovered by this processor.
    */
   public PathParameter process(final PathParameter param);

   public String getRegex();
}
