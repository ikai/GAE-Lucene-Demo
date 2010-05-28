package lucenedemo;

import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lucenedemo.BookFinder.IndexNotBuiltException;

public class SearchServlet extends HttpServlet {
  
  // private static final Logger log = Logger.getLogger(SearchServlet.class.getName());
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String term = request.getParameter("term");
    
    response.setContentType("text/html");
    
    PrintWriter out = response.getWriter();
    out.println("<h1>Search for term: " + term + "</h1>");
    BookFinder finder = BookFinderFactory.getInstance();
    
    try {
      BookResults results = finder.search(term); 
      out.println("<h2>Results</h2>");
      out.println("<ol>");
      for(Book book : results.getBooks()) {
        out.println("<li>" + book + "</li>");
      }
      out.println("</ol>");
    } catch (ParseException e) {
      out.println("Unable to parse query");
    } catch (IndexNotBuiltException e) {
      out.println("Index not yet built");

    } 
    
    
  }
  
}
