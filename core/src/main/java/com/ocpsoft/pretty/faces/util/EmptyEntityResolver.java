package com.ocpsoft.pretty.faces.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>
 * Empty {@link EntityResolver} to disable downloading of external entities.
 * Will work without problems for the parts of the <code>faces-config.xml</code>
 * we are parsing.
 * </p>
 * 
 * @see http://wiki.apache.org/commons/Digester/FAQ
 */
public class EmptyEntityResolver implements EntityResolver
{

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
   {
      return new InputSource(new ByteArrayInputStream(new byte[0]));
   }

}