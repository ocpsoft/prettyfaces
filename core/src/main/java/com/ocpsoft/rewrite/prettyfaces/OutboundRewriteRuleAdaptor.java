/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ocpsoft.rewrite.prettyfaces;

import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.rewrite.RewriteEngine;
import com.ocpsoft.rewrite.config.Condition;
import com.ocpsoft.rewrite.config.Operation;
import com.ocpsoft.rewrite.config.Rule;
import com.ocpsoft.rewrite.servlet.config.HttpCondition;
import com.ocpsoft.rewrite.servlet.config.HttpOperation;
import com.ocpsoft.rewrite.servlet.http.event.HttpOutboundServletRewrite;
import com.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class OutboundRewriteRuleAdaptor implements Rule
{
   private final RewriteRule rule;

   public OutboundRewriteRuleAdaptor(final RewriteRule rule)
   {
      this.rule = rule;
   }

   public Condition getCondition()
   {
      return new HttpCondition() {

         @Override
         public boolean evaluateHttp(final HttpServletRewrite event)
         {
            if ((event instanceof HttpOutboundServletRewrite)
                     && rule.isOutbound()
                     && rule.matches(((HttpOutboundServletRewrite) event).getOutboundURL()))
            {
               return true;
            }
            return false;
         }
      };
   }

   public Operation getOperation()
   {
      return new HttpOperation() {

         @Override
         public void performHttp(final HttpServletRewrite event)
         {
            RewriteEngine engine = new RewriteEngine();
            HttpOutboundServletRewrite outbound = (HttpOutboundServletRewrite) event;
            String url = outbound.getOutboundURL();
            String strippedUrl = stripContextPath(outbound.getContextPath(), url);

            String result = "";
            if (!strippedUrl.equals(url))
            {
               result = outbound.getContextPath();
            }
            strippedUrl = engine.processOutbound(event.getRequest(), event.getResponse(), rule, strippedUrl);
            result += strippedUrl;

            outbound.setOutboundURL(result);
         }
      };
   }

   /**
    * If the given URL is prefixed with this request's context-path, return the URI without the context path. Otherwise
    * return the URI unchanged.
    */
   private String stripContextPath(final String contextPath, String uri)
   {
      if (uri.startsWith(contextPath))
      {
         uri = uri.substring(contextPath.length());
      }
      return uri;
   }

}
