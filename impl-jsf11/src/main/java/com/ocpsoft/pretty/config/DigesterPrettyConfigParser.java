/*
 * Copyright 2010 Lincoln Baxter, III
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.pretty.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.config.mapping.QueryParameter;
import com.ocpsoft.pretty.config.mapping.UrlAction;

/**
 * Digester-based implementation of {@link PrettyConfigParser}.
 */
public class DigesterPrettyConfigParser implements PrettyConfigParser
{

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
      digester.push(builder);
      digester.parse(resource);
   }

   private Digester configureDigester(final Digester digester)
   {
      // Assume that target builder will be pushed into digester stack
      // externally

      // Create Mapping Object
      digester.addObjectCreate("pretty-config/url-mapping", PrettyUrlMapping.class);
      digester.addSetProperties("pretty-config/url-mapping");
      digester.addCallMethod("pretty-config/url-mapping/pattern", "setPattern", 0);

      // Parse Query Params
      digester.addObjectCreate("pretty-config/url-mapping/query-param", QueryParameter.class);
      digester.addSetProperties("pretty-config/url-mapping/query-param");
      digester.addCallMethod("pretty-config/url-mapping/query-param", "setExpression", 0);
      digester.addSetNext("pretty-config/url-mapping/query-param", "addQueryParam");

      // Parse Action Methods
      digester.addObjectCreate("pretty-config/url-mapping/action", UrlAction.class);
      digester.addSetProperties("pretty-config/url-mapping/action");
      digester.addCallMethod("pretty-config/url-mapping/action", "setPhaseId", 1);
      digester.addCallParam("pretty-config/url-mapping/action", 0, "phaseId");
      digester.addCallMethod("pretty-config/url-mapping/action", "setAction", 0);
      digester.addSetNext("pretty-config/url-mapping/action", "addAction");

      // Parse View Id
      digester.addCallMethod("pretty-config/url-mapping/view-id", "setViewId", 0);

      // Add Mappings to Config
      digester.addSetNext("pretty-config/url-mapping", "addMapping");
      return digester;
   }
}
