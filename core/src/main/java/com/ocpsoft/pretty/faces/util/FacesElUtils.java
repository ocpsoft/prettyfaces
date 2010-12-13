/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program. If not, see the file COPYING.LESSER
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.util;

import java.util.regex.Pattern;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class FacesElUtils
{
   private static final String EL_REGEX = "\\#\\{\\s*[^}]+\\s*\\}";

   /**
    * The pattern to match any EL expression in a String. Regex Group 0 contains
    * the entire expression, no more.
    */
   public static final Pattern elPattern = Pattern.compile(EL_REGEX);

   public Object coerceToType(final FacesContext context, final String expression, final Object value) throws ELException
   {
      ExpressionFactory ef = context.getApplication().getExpressionFactory();
      ValueExpression ve = ef.createValueExpression(context.getELContext(), expression, Object.class);
      return ef.coerceToType(value, ve.getType(context.getELContext()));
   }

   public Class<?> getExpectedType(final FacesContext context, final String expression) throws ELException
   {
      ExpressionFactory ef = context.getApplication().getExpressionFactory();
      ValueExpression ve = ef.createValueExpression(context.getELContext(), expression, Object.class);
      return ve.getType(context.getELContext());
   }

   public Object getValue(final FacesContext context, final String expression) throws ELException
   {
      ExpressionFactory ef = context.getApplication().getExpressionFactory();
      ValueExpression ve = ef.createValueExpression(context.getELContext(), expression, Object.class);
      return ve.getValue(context.getELContext());
   }

   public Object invokeMethod(final FacesContext context, final String expression) throws ELException
   {
      return invokeMethod(context, expression, new Class[] {}, null);
   }

   public Object invokeMethod(final FacesContext context, final String expression, Class<?>[] argumentTypes,
         Object[] argumentValues) throws ELException
   {
      ExpressionFactory ef = context.getApplication().getExpressionFactory();
      MethodExpression me = ef.createMethodExpression(context.getELContext(), expression, Object.class, argumentTypes);
      return me.invoke(context.getELContext(), argumentValues);
   }

   public boolean isEl(final String viewId)
   {
      if (viewId == null)
      {
         return false;
      }
      return elPattern.matcher(viewId).matches();
   }

   public void setValue(final FacesContext context, final String expression, final Object value) throws ELException
   {
      ExpressionFactory ef = context.getApplication().getExpressionFactory();
      ValueExpression ve = ef.createValueExpression(context.getELContext(), expression, Object.class);
      ve.setValue(context.getELContext(), ef.coerceToType(value, ve.getType(context.getELContext())));
   }

   public ValueExpression createValueExpression(final FacesContext context, final String expression) throws ELException
   {
      ExpressionFactory ef = context.getApplication().getExpressionFactory();
      ValueExpression ve = ef.createValueExpression(context.getELContext(), expression, Object.class);
      return ve;
   }
}
