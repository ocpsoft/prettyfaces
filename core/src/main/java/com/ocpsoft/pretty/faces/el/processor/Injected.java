package com.ocpsoft.pretty.faces.el.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.el.ConstantExpression;

public class Injected implements PathParameterProcessor
{
   private static final String REGEX = "\\#\\{\\s*:?\\s*([\\w\\.]+)\\s*\\}";
   public static final Pattern pattern = Pattern.compile(REGEX);

   public PathParameter process(final PathParameter param)
   {
      PathParameter result = param.copy();

      Matcher matcher = pattern.matcher(param.getExpression().getELExpression());
      if (matcher.matches())
      {
         String el = matcher.group(1);
         result.setExpression(new ConstantExpression("#{" + el + "}"));
         result.setExpressionIsPlainText(false);
      }
      return result;
   }

   public String getRegex()
   {
      return REGEX;
   }

}
