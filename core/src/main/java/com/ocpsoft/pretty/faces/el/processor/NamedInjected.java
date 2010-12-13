package com.ocpsoft.pretty.faces.el.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.el.ConstantExpression;

public class NamedInjected implements PathParameterProcessor
{
   private static final String REGEX = "\\#\\{\\s*(\\w+)\\s*:\\s*([\\w\\.]{1,})\\s*\\}";
   public static final Pattern pattern = Pattern.compile(REGEX);

   public PathParameter process(final PathParameter param)
   {
      PathParameter result = param.copy();

      Matcher matcher = pattern.matcher(param.getExpression().getELExpression());
      if (matcher.matches())
      {
         String name = matcher.group(1);
         String el = matcher.group(2);

         result.setExpression(new ConstantExpression("#{" + el + "}"));
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
