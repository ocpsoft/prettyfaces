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
 * Public License along with this program. If not, see the file COPYING.LESSER3
 * or visit the GNU website at <http://www.gnu.org/licenses/>.
 */
package com.ocpsoft.pretty.faces.config.mapping;

import com.ocpsoft.pretty.faces.el.PrettyExpression;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class PathParameter extends RequestParameter
{
   private static final String PATH_PARAM_NAME_PREFIX = "com.ocpsoft.vP_";
   private static final String DEFAULT_PATH_REGEX = "[^/]+";

   private int position;
   private String regex = DEFAULT_PATH_REGEX;
   private boolean expressionIsPlainText = true;

   public PathParameter()
   {
      super();
   }

   public PathParameter(final String name, final String value, final PrettyExpression expression)
   {
      super(name, value, expression);
   }

   public PathParameter(final String name, final String value)
   {
      super(name, value);
   }

   public PathParameter copy()
   {
      PathParameter result = new PathParameter();
      if (isNamed())
      {
         result.setName(getName());
      }
      result.setName(getName());
      result.setValue(getValue());
      result.setPosition(getPosition());
      result.setExpression(getExpression());
      result.setRegex(getRegex());
      result.setExpressionIsPlainText(expressionIsPlainText());
      return result;
   }

   public int getPosition()
   {
      return position;
   }

   public void setPosition(final int param)
   {
      position = param;
   }

   public boolean isNamed()
   {
      return (null != super.getName()) && !"".equals(super.getName().trim());
   }

   @Override
   public String getName()
   {
      if (!isNamed())
      {
         return PATH_PARAM_NAME_PREFIX + getPosition();
      }
      return super.getName();
   }

   public String getRegex()
   {
      return regex;
   }

   public void setRegex(final String regex)
   {
      this.regex = regex;
   }

   public void setExpressionIsPlainText(final boolean value)
   {
      this.expressionIsPlainText = value;
   }

   public boolean expressionIsPlainText()
   {
      return expressionIsPlainText;
   }

   @Override
   public String toString()
   {
      return "PathParameter [position=" + position + ", regex=" + regex + ", name=" + getName() + ", expression=" + getExpression() + ", value=" + getValue() + "]";
   }
}
