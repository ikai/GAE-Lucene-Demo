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
    String title = request.getParameter("title");
    String author = request.getParameter("author");
    String language = request.getParameter("lang");
    
    StringBuilder queryBuilder = new StringBuilder();
        
    if(language != null && !language.equals("all")) {
      queryBuilder.append(" language:" + language);
    }

    if(title != null && !title.isEmpty()) {
      queryBuilder.append(" title:" + title);
    }

    if(author != null && !author.isEmpty()) {
      queryBuilder.append(" author:" + author);
    }

    
    response.setContentType("text/html");
    
    String query = queryBuilder.toString();
    
    PrintWriter out = response.getWriter();
    out.println("<h1>Search with query: " + query + "</h1>");
    BookFinder finder = BookFinderFactory.getInstance();
    
    try {
      BookResults results = finder.search(query); 
      out.println("<h2>Results</h2>");
      out.println("<ol>");
      for(Book book : results.getBooks()) {
        out.println("<li>" + book + "</li>");
      }
      out.println("</ol>");
    } catch (ParseException e) {
      out.println("Unable to parse query: " + query);
    } catch (IndexNotBuiltException e) {
      out.println("Index not yet built");

    } 
    
    
  }
  
}
