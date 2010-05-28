package lucenedemo;

import org.apache.lucene.queryParser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import lucenedemo.BookFinder.IndexNotBuiltException;

public class SearchServlet extends HttpServlet {
  
  private static final Logger log = Logger.getLogger(SearchServlet.class.getName());
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String term = request.getParameter("term");
    
    response.setContentType("text/html");
    
    PrintWriter out = response.getWriter();
    out.println("<h1>Search for term: " + term + "</h1>");
    BookFinder finder = BookFinderFactory.getInstance();
    
    try {
      finder.search(term);
      
      
    } catch (ParseException e) {
      out.println("Unable to parse query");
    } catch (IndexNotBuiltException e) {
      out.println("Index not yet built");

    } 
    
    
  }
  
}
