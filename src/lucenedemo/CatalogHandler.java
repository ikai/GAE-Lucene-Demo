package lucenedemo;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.logging.Logger;

import lucenedemo.BookFinder.IndexNotWritableException;

/*
 * SAX handler. Lots of help from http://www.totheriver.com/learn/xml/xmltutorial.html
 */
public class CatalogHandler extends DefaultHandler {
  
  private static final Logger log = Logger.getLogger(CatalogHandler.class.getName());
  private int elementCounter = 0;
  
  private Book currentBook;
  private String tempVal;
  private BookFinder finder;
  

  @Override
  public void startDocument() {
    log.info("Starting to parse XML");
    finder = BookFinderFactory.getInstance();
    try {
      finder.openIndexForWriting();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IndexNotWritableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
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
          try {
            log.fine("Indexing book: " + currentBook);
            finder.addBook(currentBook);
            
          } catch (IndexNotWritableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
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
    try {
      finder.close();
      log.info("Parsed " + elementCounter + " elements.");
      // log.info("Indexed" + finder.);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IndexNotWritableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
}
