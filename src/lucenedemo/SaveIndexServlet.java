package lucenedemo;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SaveIndexServlet extends HttpServlet {
  
  private static final Logger log = Logger.getLogger(SaveIndexServlet.class.getName());
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");
    
    PrintWriter out = response.getWriter();
    
    MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
    
    BookFinder finder = BookFinderFactory.getInstance();
    
    
    
    //cache.put("lucene-index", finder);
    finder.indexSize();
    
    
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(finder);
    oos.close();
    
    int finderSize = baos.size();
    
    out.println("Cache size: " + finderSize);
       
  }


}