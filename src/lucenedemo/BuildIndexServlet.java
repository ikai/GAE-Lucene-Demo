package lucenedemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BuildIndexServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/plain");
    
    PrintWriter out = response.getWriter();
    out.println("Building Index ...");
    
    
    // BookFinderFactory.init();
    
    File dir = new File("WEB-INF/texts");
    File[] files = dir.listFiles();
    
    for(File file : files) {
      out.println("Reading : " + file.getName());
      if(file.isFile()) {
        try {
          FileReader reader = new FileReader(file);
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } 
      }
    }
    
  }
  
}
