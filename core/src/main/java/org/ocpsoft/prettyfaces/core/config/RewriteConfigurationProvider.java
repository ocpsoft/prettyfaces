package org.ocpsoft.prettyfaces.core.config;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.ocpsoft.prettyfaces.core.config.spi.PrettyConfigProvider;

import com.ocpsoft.common.services.ServiceLoader;
import com.ocpsoft.logging.Logger;
import com.ocpsoft.rewrite.config.Configuration;
import com.ocpsoft.rewrite.config.ConfigurationBuilder;
import com.ocpsoft.rewrite.config.Rule;
import com.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;

public class RewriteConfigurationProvider extends HttpConfigurationProvider
{

   private final Logger log = Logger.getLogger(RewriteConfigurationProvider.class);

   @Override
   public int priority()
   {
      return 0;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Configuration getConfiguration(ServletContext context)
   {

      ConfigurationBuilder builder = ConfigurationBuilder.begin();

      // add rules provided by the SPI implementations
      Iterator<PrettyConfigProvider> providers = ServiceLoader.load(PrettyConfigProvider.class).iterator();
      while (providers.hasNext()) {

         PrettyConfigProvider provider = providers.next();
         Collection<Rule> rules = provider.getRules(context);

         if (log.isDebugEnabled()) {
            log.debug("Got {} rules from provider {}", rules.size(), provider.getClass().getSimpleName());
         }

         for (Rule rule : rules) {
            builder.addRule(rule);
         }

      }

      return builder;

   }
}
