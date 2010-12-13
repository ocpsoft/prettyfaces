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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.PrettyFilter;

/**
 * Implements the Pretty Faces configuration procedure.
 * <p>
 * At application startup time, before any requests are processed, Pretty Faces
 * processes zero or more configuration resources, located according to the
 * following algorithm:
 * </p>
 * <ul>
 * <li>Search for classpath resources named
 * <code>META-INF/pretty-config.xml</code> in the ServletContext resource paths
 * for this web application, and load each as a configuration resource.</li>
 * <li>Check for the existence of a context initialization parameter named
 * <code>com.ocpsoft.pretty.CONFIG_FILES</code>. If it exists, treat it as a
 * comma-delimited list of context relative resource paths (starting with a
 * <code>/</code>), and load each of the specfied resources.</li>
 * <li>Check for the existence of a web application configuration resource named
 * <code>/WEB-INF/pretty-config.xml</code>, and load it if the resource exists.</li>
 * </ul>
 * 
 * @author Aleksei Valikov
 */
public class PrettyConfigurator
{

    private static final Log log = LogFactory.getLog(PrettyConfigurator.class);

    /**
     * Name of the classpath configuration resource for Pretty Faces.
     */
    public static final String PRETTY_CONFIG_RESOURCE = "META-INF/pretty-config.xml";

    /**
     * Default path of the Pretty Faces configuration.
     */
    public static final String DEFAULT_PRETTY_FACES_CONFIG = "/WEB-INF/pretty-config.xml";

    private final ServletContext servletContext;

    private final PrettyConfigParser configParser;

    /**
     * Constructs a new configurator.
     * 
     * @param servletContext
     *            servlet context, must not be <code>null</code>.
     */
    public PrettyConfigurator(final ServletContext servletContext)
    {
        if (servletContext == null)
        {
            throw new IllegalArgumentException("Servlet context must not be null.");
        }
        this.servletContext = servletContext;
        configParser = new DigesterPrettyConfigParser();
    }

    /**
     * Loads the Pretty Faces configuration and returns the configuration
     * object.
     * 
     * @return Loaded configuration.
     * @throws IOException
     *             If configuration could not be read.
     * @throws SAXException
     *             If configuraion could not be parsed.
     */
    public PrettyConfig configure() throws IOException, SAXException
    {
        final PrettyConfigBuilder builder = new PrettyConfigBuilder();
        // Load configurations from the class path
        feedClassLoaderConfigs(builder);
        // Load configurations from the config files configured in the servlet
        // context
        feedContextSpecifiedConfig(builder);
        // Load configuration from the default path
        feedWebAppConfig(builder);
        return builder.build();
    }

    private void feedClassLoaderConfigs(final PrettyConfigBuilder builder) throws IOException, SAXException
    {
        final Enumeration<URL> urls = getResourceLoader().getResources(PRETTY_CONFIG_RESOURCE);
        if (urls != null)
        {
            while (urls.hasMoreElements())
            {
                final URL url = urls.nextElement();
                if (url != null)
                {
                    InputStream is = null;
                    try
                    {
                        is = openStream(url);
                        configParser.parse(builder, is);
                    }
                    finally
                    {
                        if (is != null)
                        {
                            try
                            {
                                is.close();
                            }
                            catch (IOException ignored)
                            {

                            }
                        }

                    }
                }
            }
        }
    }

    private void feedContextSpecifiedConfig(final PrettyConfigBuilder builder) throws IOException, SAXException
    {
        final List<String> configFilesList = getConfigFilesList();
        for (final String systemId : configFilesList)
        {
            final InputStream is = servletContext.getResourceAsStream(systemId);
            if (is == null)
            {
                log.error("Pretty Faces config resource [" + systemId + "] not found.");
                continue;
            }

            if (log.isInfoEnabled())
            {
                log.info("Reading config [" + systemId + "].");
            }

            try
            {
                configParser.parse(builder, is);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException ignored)
                {

                }
            }
        }
    }

    private List<String> getConfigFilesList()
    {
        final String configFiles = servletContext.getInitParameter(PrettyFilter.CONFIG_FILES_ATTR);
        final List<String> configFilesList = new ArrayList<String>();
        if (configFiles != null)
        {
            final StringTokenizer st = new StringTokenizer(configFiles, ",", false);
            while (st.hasMoreTokens())
            {
                final String systemId = st.nextToken().trim();

                if (DEFAULT_PRETTY_FACES_CONFIG.equals(systemId))
                {
                    if (log.isWarnEnabled())
                    {
                        log.warn("The file [" + DEFAULT_PRETTY_FACES_CONFIG + "] has been specified in the ["
                                + PrettyFilter.CONFIG_FILES_ATTR + "] context parameter of "
                                + "the deployment descriptor. This will automatically be removed, "
                                + "if we wouldn't do this, it would be loaded twice.");
                    }
                }
                else
                {
                    configFilesList.add(systemId);
                }
            }
        }
        return configFilesList;
    }

    private void feedWebAppConfig(final PrettyConfigBuilder builder) throws IOException, SAXException
    {

        final InputStream is = servletContext.getResourceAsStream(DEFAULT_PRETTY_FACES_CONFIG);
        if (is != null)
        {
            if (log.isInfoEnabled())
            {
                log.info("Reading config [" + DEFAULT_PRETTY_FACES_CONFIG + "].");
            }

            try
            {
                configParser.parse(builder, is);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException ignored)
                {

                }
            }
        }
    }

    /**
     * Return class loader to be used to resolve resources.
     * 
     * @return Class loader to be used to resolve resources
     */
    protected ClassLoader getResourceLoader()
    {
        final ClassLoader resourceLoader = Thread.currentThread().getContextClassLoader();
        return resourceLoader;
    }

    /**
     * Opens an input stream for the given URL.
     * 
     * @param url
     *            target URL.
     * @return Opened input stream.
     * @throws IOException
     *             If connection could not be opened.
     */
    protected InputStream openStream(final URL url) throws IOException
    {
        final URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }

}
