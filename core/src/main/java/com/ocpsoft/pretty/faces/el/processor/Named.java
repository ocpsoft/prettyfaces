package com.ocpsoft.pretty.faces.el.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;

public class Named implements PathParameterProcessor
{

   private static final String REGEX = "\\#\\{\\s*(\\w+)\\s*\\}";
   public static final Pattern pattern = Pattern.compile(REGEX);

   public PathParameter process(final PathParameter param)
   {
      PathParameter result = param.copy();

      Matcher matcher = pattern.matcher(param.getExpression().getELExpression());
      if (matcher.matches())
      {
         String name = matcher.group(1);
         result.setName(name);
         result.setExpressionIsPlainText(false);
      }
      return result;
   }

   public String getRegex()
   {
      return REGEX;
   }
}
