package com.ocpsoft.pretty.faces.el.processor;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

public class PlainText implements PathParameterProcessor
{

   public PathParameter process(final PathParameter param)
   {
      PathParameter result = param.copy();
      if (result.expressionIsPlainText())
      {
         result.setRegex(result.getExpression().getELExpression());
      }
      return result;
   }

   public String getRegex()
   {
      return null;
   }

}
