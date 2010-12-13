/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * Copyright (C) 2010 - Lincoln Baxter, III <lincoln@ocpsoft.com> This program
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

package com.ocpsoft.pretty.faces.config.convert;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.ocpsoft.pretty.faces.config.rewrite.TrailingSlash;

public class TrailingSlashConverter implements Converter
{
   @SuppressWarnings("rawtypes")
   public Object convert(final Class type, final Object value)
   {
      if (value instanceof String)
      {
         return Enum.valueOf(TrailingSlash.class, ((String) value).toUpperCase());
      }
      throw new ConversionException("Could not convert value: [" + value + "] to TrailingSlash type.");
   }
}