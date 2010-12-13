package com.ocpsoft.pretty.faces.el;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.el.processor.RegexOverride;

public class RegexOverrideExpressionProcessorTest
{
   RegexOverride p = new RegexOverride();

   @Test
   public void testSpacing1() throws Exception
   {
      String expression = "#{/test/name}";
      PathParameter param = new PathParameter();
      param.setExpression(expression);
      param = p.process(param);

      assertEquals("#{name}", param.getExpression().getELExpression());
      assertEquals("test", param.getRegex());
   }

   @Test
   public void testSpacing2() throws Exception
   {
      String expression = "#{ /test/ name:point}";
      PathParameter param = new PathParameter();
      param.setExpression(expression);
      param = p.process(param);

      assertEquals("#{name:point}", param.getExpression().getELExpression());
      assertEquals("test", param.getRegex());
   }

   @Test
   public void testSpacing3() throws Exception
   {
      String expression = "#{/test/  name}";
      PathParameter param = new PathParameter();
      param.setExpression(expression);
      param = p.process(param);

      assertEquals("#{name}", param.getExpression().getELExpression());
      assertEquals("test", param.getRegex());
   }

   @Test
   public void testSpacing4() throws Exception
   {
      String expression = "#{  /test/name:point.p}";
      PathParameter param = new PathParameter();
      param.setExpression(expression);
      param = p.process(param);

      assertEquals("#{name:point.p}", param.getExpression().getELExpression());
      assertEquals("test", param.getRegex());
   }

   @Test
   public void testBoundaries() throws Exception
   {
      String expression = "#{  /te/st/name}";
      PathParameter param = new PathParameter();
      param.setExpression(expression);
      param = p.process(param);

      assertEquals("#{name}", param.getExpression().getELExpression());
      assertEquals("te/st", param.getRegex());
   }

   @Test
   public void testBoundaries2() throws Exception
   {
      String expression = "#{  /te[^/]+/st/name}";
      PathParameter param = new PathParameter();
      param.setExpression(expression);
      param = p.process(param);

      assertEquals("#{name}", param.getExpression().getELExpression());
      assertEquals("te[^/]+/st", param.getRegex());
   }
}
