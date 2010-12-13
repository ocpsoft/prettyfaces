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
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ocpsoft.pretty.PrettyFilter;

/**
 * @author Aleksei Valikov
 */
public class PrettyConfiguratorTest
{

    @Test
    public void configureDefault() throws SAXException, IOException
    {
        final ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

        EasyMock.expect(servletContext.getInitParameter(PrettyFilter.CONFIG_FILES_ATTR)).andReturn(null).anyTimes();
        EasyMock.expect(servletContext.getResourceAsStream(PrettyConfigurator.DEFAULT_PRETTY_FACES_CONFIG)).andReturn(
                null).anyTimes();

        final ClassLoader resourceLoader = new URLClassLoader(new URL[0], Thread.currentThread()
                .getContextClassLoader())
        {
            @Override
            public Enumeration<URL> getResources(final String name) throws IOException
            {
                if (PrettyConfigurator.PRETTY_CONFIG_RESOURCE.equals(name))
                {
                    return enumeration(mockPrettyConfigURL());
                }
                else
                {
                    return super.getResources(name);
                }
            }
        };

        final PrettyConfigurator configurator = new PrettyConfigurator(servletContext)
        {
            @Override
            protected ClassLoader getResourceLoader()
            {
                return resourceLoader;
            }
        };

        EasyMock.replay(servletContext);
        final PrettyConfig config = configurator.configure();
        Assert.assertEquals(8, config.getMappings().size());
    }

    @Test
    public void configureSpecial() throws SAXException, IOException
    {
        final ServletContext servletContext = EasyMock.createNiceMock(ServletContext.class);

        EasyMock.expect(servletContext.getInitParameter(PrettyFilter.CONFIG_FILES_ATTR)).andReturn("car.xml, cdr.xml")
                .anyTimes();
        EasyMock.expect(servletContext.getResourceAsStream("car.xml")).andReturn(mockPrettyConfigInputStream())
                .anyTimes();
        EasyMock.expect(servletContext.getResourceAsStream("cdr.xml")).andReturn(mockPrettyConfigInputStream())
                .anyTimes();
        EasyMock.expect(servletContext.getResourceAsStream(PrettyConfigurator.DEFAULT_PRETTY_FACES_CONFIG)).andReturn(
                mockPrettyConfigInputStream()).anyTimes();

        final PrettyConfigurator configurator = new PrettyConfigurator(servletContext);
        EasyMock.replay(servletContext);
        final PrettyConfig config = configurator.configure();
        Assert.assertEquals(24, config.getMappings().size());

    }

    private InputStream mockPrettyConfigInputStream()
    {
        return getClass().getClassLoader().getResourceAsStream("mock-pretty-config.xml");
    }

    private URL mockPrettyConfigURL()
    {
        return getClass().getClassLoader().getResource("mock-pretty-config.xml");
    }

    private <T> Enumeration<T> enumeration(final T... elements)
    {
        return new Enumeration<T>()
        {
            private int index = 0;

            public boolean hasMoreElements()
            {
                return index < elements.length;
            }

            public T nextElement()
            {
                if (!hasMoreElements())
                {
                    throw new NoSuchElementException("No more elements exist.");
                }
                return elements[index++];
            }
        };
    }

}
