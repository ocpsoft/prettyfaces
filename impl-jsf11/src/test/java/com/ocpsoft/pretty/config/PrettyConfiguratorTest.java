/*
 * PrettyFaces is an OpenSource JSF library to create bookmarkable URLs.
 * 
 * Copyright (C) 2009 - Lincoln Baxter, III <lincoln@ocpsoft.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see the file COPYING.LESSER3 or visit the
 * GNU website at <http://www.gnu.org/licenses/>.
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
