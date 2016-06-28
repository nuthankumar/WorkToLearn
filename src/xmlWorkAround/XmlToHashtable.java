package xmlWorkAround;

import java.io.*;
import java.util.Stack;
import java.util.Hashtable;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;




/** 
 *
 * 
 *
 * This class parses an xml document and extracts the elements into a
 * Hashtable name/value pair.  Each elelment is extracted, and added to the
 * Hashtable with the xml tag element name as its name, and the xml value
 * as its value.
 *
 * The xml document is input as a String to the parse method.  The Hashtable is
 *  obtained from the getHashtable method.
 *
 * Example Usage:
 *
 * <CODE>
 * XmlToHashtable xh = new XmlToHashtable();
 * xh.parse(theXmlString);
 * Hashtable ht = xh.getHashtable();
 * </CODE>
 *
 */

public class XmlToHashtable extends DefaultHandler
{
    protected Stack elementValues;
    protected Hashtable nameValues; 
    /**
     * Constructor.
     *
     **/    
    public XmlToHashtable() 
    {
        elementValues = new Stack();
        nameValues = new Hashtable();
    }
    /** 
     * Parse the xml file.
     *
     * @param String input xml data document.  
     *
     */    
    public void parse(String xmlData) throws Exception {
        nameValues.clear();
        elementValues.clear();
        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        // Parse the input
        parser.parse(new InputSource(new StringReader(xmlData)), this);
    }
    
    /** 
     * Return Hashtable of the name/value pairs extracted from the xml document.
     *
     * @return Hashtable
     *
     */    
    public Hashtable getHashtable()
    {
        return nameValues;
    }
    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================
    /**
     * Start of an xml element.
     *
     * @param namespaceURI
     * @param localName
     * @param qName
     * @param attrs
     * @throws SAXException  
     */    
    public void startElement(String namespaceURI,
                             String localName, 
                             String qName, 
                             Attributes attrs) throws SAXException
    {
        // get element name
        //String elementName = localName; 
        //if (elementName.equals("")) {
        //    elementName = qName;
        //}
        
        // initialize empty buffer, push this element to top of stack
        StringBuffer elementBuff = new StringBuffer();
        elementValues.push(elementBuff);
    }
    /**
     * End of an eml element.
     *
     * @param namespaceURI
     * @param localName
     * @param qName
     * @throws SAXException  */    
    public void endElement(String namespaceURI,
                           String localName, 
                           String qName) throws SAXException 
    {

        // format element name    
        String elementName = localName; 
        if (elementName.equals("")) {
            elementName = qName;
        }   
        
        // pop off element value and store in Hashtable 
        StringBuffer elementBuff = (StringBuffer)elementValues.pop();
         nameValues.put(elementName, elementBuff.toString());
    }
    /**
     * Called as element is parsed.
     *
     */    
    public void characters(char buf[], int offset, int len) throws SAXException
    {

        String element = new String(buf, offset, len);
        // add ontents to accumulating element
        ((StringBuffer)elementValues.peek()).append(element);
    }

}