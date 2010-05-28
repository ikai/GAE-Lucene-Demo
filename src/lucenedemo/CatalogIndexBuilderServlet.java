package lucenedemo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

@SuppressWarnings("serial")
public class CatalogIndexBuilderServlet extends HttpServlet {
  
  private static final Logger log = Logger.getLogger(CatalogIndexBuilderServlet.class.getName());
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");
    
    PrintWriter out = response.getWriter();
    out.println("Building catalog index ... ");
    long indexingStartTime = System.currentTimeMillis();
    
    BookFinderFactory.reset();
    
    File dir = new File("WEB-INF/catalog");
    File[] files = dir.listFiles();
    
    for(File file : files) {
      if(file.isFile() && file.getName().endsWith(".xml")) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        
        try {
          log.info("started parsing file " + file.getName());
          out.println("parsing file " + file.getName()); 
          SAXParser sp = spf.newSAXParser();
          CatalogHandler ch = new CatalogHandler();
          sp.parse(file, ch);

          log.info("finished parsing file " + file.getName()); 
          
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
          
        }
    
      }
    }
    
    long totalIndexingTime = System.currentTimeMillis() - indexingStartTime;
    out.println("Indexed in " + totalIndexingTime + "ms");
    
    
  }
  
}
