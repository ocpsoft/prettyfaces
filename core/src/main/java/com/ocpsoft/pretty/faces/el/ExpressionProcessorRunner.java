package com.ocpsoft.pretty.faces.el;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.el.processor.Injected;
import com.ocpsoft.pretty.faces.el.processor.Named;
import com.ocpsoft.pretty.faces.el.processor.NamedInjected;
import com.ocpsoft.pretty.faces.el.processor.PathParameterProcessor;
import com.ocpsoft.pretty.faces.el.processor.PlainText;
import com.ocpsoft.pretty.faces.el.processor.RegexOverride;

public abstract class ExpressionProcessorRunner
{
   public static final List<PathParameterProcessor> processors;
   public static final List<PathParameterProcessor> preProcessors;

   static
   {
      List<PathParameterProcessor> temp = new ArrayList<PathParameterProcessor>();

      temp.add(new Named());
      temp.add(new NamedInjected());
      temp.add(new Injected());
      temp.add(new PlainText());

      processors = Collections.unmodifiableList(temp);

      temp = new ArrayList<PathParameterProcessor>();
      temp.add(new RegexOverride());

      preProcessors = Collections.unmodifiableList(temp);
   }

   public static PathParameter process(final String expression)
   {
      PathParameter result = new PathParameter();
      result.setExpression(new ConstantExpression(expression));

      for (PathParameterProcessor p : preProcessors)
      {
         result = p.process(result);
      }

      for (PathParameterProcessor p : processors)
      {
         result = p.process(result);
      }

      if (result == null)
      {
         throw new PrettyException("Malformed EL expression: " + expression + ", discovered.");
      }

      return result;
   }

   // public static PathParameter preprocess(final String expression)
   // {
   // PathParameter result = new PathParameter();
   // result.setExpression(expression);
   //
   // for (PathParameterProcessor p : preProcessors)
   // {
   // result = p.process(result);
   // }
   //
   // if (result == null)
   // {
   // throw new PrettyException("Malformed EL expression: " + expression +
   // ", discovered.");
   // }
   //
   // return result;
   // }

}
