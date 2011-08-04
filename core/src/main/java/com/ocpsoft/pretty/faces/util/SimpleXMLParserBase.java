package com.ocpsoft.pretty.faces.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Base class for implementing XML parsers based on the SAX API.
 * 
 * @author Christian Kaltepoth <christian@kaltepoth.de>
 */
public abstract class SimpleXMLParserBase extends DefaultHandler
{

   /**
    * Used to maintain a list of parents up to the root of the XML tree
    */
   private final Stack<String> elements = new Stack<String>();

   /**
    * Parses the supplied {@link InputStream}.
    * 
    * @param stream
    *           The stream to parse
    * @throws IOException
    *            for any errors during the parsing process
    */
   public final void parse(InputStream stream) throws IOException
   {

      // all XML exceptions are wrapped by IOExceptions
      try
      {

         // setup a namespace aware SAXParserFactory
         SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
         saxParserFactory.setNamespaceAware(true);

         // parse the input stream
         SAXParser parser = saxParserFactory.newSAXParser();
         parser.parse(stream, new SimpleHandler());

      }
      catch (ParserConfigurationException e)
      {
         throw new IOException(e);
      }
      catch (SAXException e)
      {
         throw new IOException(e);
      }

   }

   /**
    * Called for every element start event
    * 
    * @param name
    *           The local name of the element
    */
   public abstract void processStartElement(String name);

   /**
    * Called for every element end event
    * 
    * @param name
    *           The local name of the element
    */
   public abstract void processEndElement(String name);

   /**
    * Called for every characters event
    * 
    * @param text
    *           The characters
    */
   public abstract void processCharacters(String text);

   /**
    * This class maintaince the parent element stack and forwards all events to
    * the abstract methods of {@link SimpleXMLParserBase}.
    */
   private class SimpleHandler extends DefaultHandler
   {

      @Override
      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
      {

         // add the element's local name to the parent stack
         elements.add(localName);

         // forward event
         processStartElement(localName);

      }

      @Override
      public void characters(char[] ch, int start, int length) throws SAXException
      {
         // forward event
         processCharacters(String.valueOf(ch, start, length));
      }

      @Override
      public void endElement(String uri, String localName, String qName) throws SAXException
      {
         // forward event
         processEndElement(localName);

         // remove the name on top of the stack
         String removedName = elements.pop();

         // check if the removed name is the expected one
         if (!removedName.equals(localName))
         {
            throw new IllegalStateException("Found '" + removedName + "' but expected '" + localName
                  + "' on the stack!");
         }

      }

   }

   /**
    * Checks whether the parent stack currently contains exactly the elements
    * supplied by the caller. This method can be used to find the current
    * position in the document. The first argument is always the root element of
    * the document, the second is a child of the root element, and so on. The
    * method uses the local names of the elements only. Namespaces are ignored.
    * 
    * @param name
    *           The names of the parent elements
    * @return <code>true</code> if the parent element stack contains exactly
    *         these elements
    */
   protected boolean elements(String... name)
   {
      if (name.length != elements.size())
      {
         return false;
      }
      for (int i = 0; i < name.length; i++)
      {
         if (!name[i].equals(elements.get(i)))
         {
            return false;
         }
      }
      return true;
   }

}
