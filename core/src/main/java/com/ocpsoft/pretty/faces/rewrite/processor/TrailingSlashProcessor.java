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

package com.ocpsoft.pretty.faces.rewrite.processor;

import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.rewrite.Processor;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class TrailingSlashProcessor implements Processor
{

   public String process(final RewriteRule rule, final String url)
   {

      String result = url;
      switch (rule.getTrailingSlash())
      {
      case APPEND:
         if (!result.endsWith("/"))
         {
            result = result + "/";
         }
         break;
      case REMOVE:
         if (result.endsWith("/") && !"/".equals(result))
         {
            result = result.substring(0, result.length() - 1);
         }
         break;
      default:
         break;
      }
      return result;
   }

}
