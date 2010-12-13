package com.ocpsoft.pretty.faces.el;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConstantExpressionTest
{

   @Test
   public void testSimpleConstantExpression()
   {

      PrettyExpression expr = new ConstantExpression("#{someBean.someProperty}");
      assertEquals("#{someBean.someProperty}", expr.getELExpression());

   }

   @Test
   public void testConstantExpressionEqualsAndHashCode()
   {

      PrettyExpression expr1 = new ConstantExpression("#{someBean.someProperty}");
      PrettyExpression expr2 = new ConstantExpression("#{someBean.someProperty}");

      assertTrue(expr1.equals(expr2));
      assertEquals(expr1.hashCode(), expr2.hashCode());

   }

}
