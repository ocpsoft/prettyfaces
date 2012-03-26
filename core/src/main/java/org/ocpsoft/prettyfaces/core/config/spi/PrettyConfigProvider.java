package org.ocpsoft.prettyfaces.core.config.spi;

import java.util.Collection;

import javax.servlet.ServletContext;

import com.ocpsoft.rewrite.config.Rule;

public interface PrettyConfigProvider
{

   Collection<Rule> getRules(ServletContext servletContext);

}
