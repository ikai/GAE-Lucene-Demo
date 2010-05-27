package lucenedemo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.logging.Logger;

public class CatalogHandler extends DefaultHandler {
  
  private static final Logger log = Logger.getLogger(CatalogHandler.class.getName());
  private int elementCounter = 0;
  
  @Override
  public void startElement(String uri, String localName, String qName,
      Attributes attributes) {
    elementCounter++;
  }


  @Override
  public void characters(char[] ch, int start, int length) {

  }

  @Override
  public void endElement(String uri, String localName,
      String qName) {


  }
  
  @Override
  public void endDocument() {
    log.info("Parsed " + elementCounter + " elements.");
  }
}
