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

package com.ocpsoft.pretty.faces.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.faces.annotation.URLAction.PhaseId;
import com.ocpsoft.pretty.faces.config.convert.CaseConverter;
import com.ocpsoft.pretty.faces.config.convert.PhaseIdConverter;
import com.ocpsoft.pretty.faces.config.convert.RedirectConverter;
import com.ocpsoft.pretty.faces.config.convert.TrailingSlashConverter;
import com.ocpsoft.pretty.faces.config.mapping.PathValidator;
import com.ocpsoft.pretty.faces.config.mapping.QueryParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlAction;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.rewrite.Case;
import com.ocpsoft.pretty.faces.config.rewrite.Redirect;
import com.ocpsoft.pretty.faces.config.rewrite.RewriteRule;
import com.ocpsoft.pretty.faces.config.rewrite.TrailingSlash;

/**
 * Digester-based implementation of {@link PrettyConfigParser}.
 */
public class DigesterPrettyConfigParser implements PrettyConfigParser
{
   private final Converter caseConverter = new CaseConverter();
   private final Converter trailingSlashConverter = new TrailingSlashConverter();
   private final Converter phaseIdConverter = new PhaseIdConverter();
   private final Converter redirectConverter = new RedirectConverter();

   public void parse(final PrettyConfigBuilder builder, final InputStream resource) throws IOException, SAXException
   {
      if (builder == null)
      {
         throw new IllegalArgumentException("Builder must not be null.");
      }
      if (resource == null)
      {
         throw new IllegalArgumentException("Input stream must not be null.");
      }
      final Digester digester = configureDigester(new Digester());

      ConvertUtils.register(caseConverter, Case.class);
      ConvertUtils.register(trailingSlashConverter, TrailingSlash.class);
      ConvertUtils.register(phaseIdConverter, PhaseId.class);
      ConvertUtils.register(redirectConverter, Redirect.class);

      digester.push(builder);
      digester.parse(resource);
   }

   /**
    * Configure the digester. Assume that the builder object will be pushed
    * after this method is called.
    */
   private Digester configureDigester(final Digester digester)
   {

      /*
       * We use the context class loader to resolve classes. This fixes
       * ClassNotFoundExceptions on Geronimo.
       */
      digester.setUseContextClassLoader(true);

      /*
       * Parse RewriteRules
       */
      digester.addObjectCreate("pretty-config/rewrite", RewriteRule.class);
      digester.addSetProperties("pretty-config/rewrite");
      digester.addSetNext("pretty-config/rewrite", "addRewriteRule");

      /*
       * Create Mapping Object
       */
      digester.addObjectCreate("pretty-config/url-mapping", UrlMapping.class);
      digester.addSetProperties("pretty-config/url-mapping");
      digester.addCallMethod("pretty-config/url-mapping/pattern", "setPattern", 1);
      digester.addCallParam("pretty-config/url-mapping/pattern", 0, "value");
      digester.addCallMethod("pretty-config/url-mapping/pattern", "setPattern", 0);

      /*
       * Parse View Id
       */
      digester.addCallMethod("pretty-config/url-mapping/view-id", "setViewId", 1);
      digester.addCallParam("pretty-config/url-mapping/view-id", 0, "value");
      digester.addCallMethod("pretty-config/url-mapping/view-id", "setViewId", 0);

      /*
       * Parse Path Validators
       */
      digester.addObjectCreate("pretty-config/url-mapping/pattern/validate", PathValidator.class);
      digester.addSetProperties("pretty-config/url-mapping/pattern/validate");
      digester.addSetNext("pretty-config/url-mapping/pattern/validate", "addPathValidator");

      /*
       * Parse Query Params
       */
      digester.addObjectCreate("pretty-config/url-mapping/query-param", QueryParameter.class);
      digester.addSetProperties("pretty-config/url-mapping/query-param");
      digester.addCallMethod("pretty-config/url-mapping/query-param", "setExpression", 0);
      digester.addSetNext("pretty-config/url-mapping/query-param", "addQueryParam");

      /*
       * Parse Action Methods
       */
      digester.addObjectCreate("pretty-config/url-mapping/action", UrlAction.class);
      digester.addSetProperties("pretty-config/url-mapping/action");
      digester.addCallMethod("pretty-config/url-mapping/action", "setAction", 0);
      digester.addSetNext("pretty-config/url-mapping/action", "addAction");

      /*
       * Add Mapping to Builder
       */
      digester.addSetNext("pretty-config/url-mapping", "addMapping");
      return digester;
   }
}
