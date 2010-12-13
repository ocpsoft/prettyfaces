package com.ocpsoft.pretty.faces.el.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.el.ConstantExpression;

public class RegexOverride implements PathParameterProcessor
{
   public static final String REGEX = "(\\#\\{)" + "(\\s*/(.*?)/)" + "\\s*([^}/]*\\})";

   public static final Pattern pattern = Pattern.compile(REGEX);

   public PathParameter process(final PathParameter param)
   {
      PathParameter result = param.copy();

      Matcher matcher = pattern.matcher(param.getExpression().getELExpression());
      if (matcher.matches())
      {
         String regex = matcher.group(3);
         result.setRegex(regex);
         result.setExpression(new ConstantExpression(matcher.group(1) + matcher.group(4)));
         result.setExpressionIsPlainText(false);
      }

      return result;
   }

   public String getRegex()
   {
      return REGEX;
   }

}
