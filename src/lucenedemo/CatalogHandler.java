package lucenedemo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.logging.Logger;

/*
 * SAX handler. Lots of help from http://www.totheriver.com/learn/xml/xmltutorial.html
 */
public class CatalogHandler extends DefaultHandler {
  
  private static final Logger log = Logger.getLogger(CatalogHandler.class.getName());
  private int elementCounter = 0;
  
  private Book currentBook;
  private String tempVal;
  
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) {
    elementCounter++;
    
    if(qName.equals("pgterms:etext")) {
      String id = attributes.getValue("rdf:ID");
      currentBook = new Book();
      currentBook.setId(id);
    }
    
  }


  @Override
  public void characters(char[] ch, int start, int length) {
    tempVal = new String(ch, start, length);
  }

  @Override
  public void endElement(String uri, String localName,
      String qName) {
        if(qName.equalsIgnoreCase("pgterms:etext")) {
        // Okay, now we index it
          log.info("Indexing book: " + currentBook);
        } else if (qName.equals("dc:title")) {
          currentBook.setTitle(tempVal);
        } else if (qName.equals("dc:creator")) {
          currentBook.setAuthor(tempVal);
        } else if (qName.equals("dc:language")) {
          currentBook.setLanguage(tempVal);
        } 

  }
  
  @Override
  public void endDocument() {
    log.info("Parsed " + elementCounter + " elements.");
  }
}
