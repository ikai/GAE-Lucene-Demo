package lucenedemo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

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
    
    long indexingStartTime = System.currentTimeMillis();
    
    // BookFinderFactory.init();
    
    Directory index = new RAMDirectory();
    IndexWriter writer = new IndexWriter(index, 
         new StandardAnalyzer(Version.LUCENE_30), 
         IndexWriter.MaxFieldLength.LIMITED);
    
    File dir = new File("WEB-INF/texts");
    File[] files = dir.listFiles();
    
    for(File file : files) {
      if(file.isFile()) {
        try {
          Document doc = new Document();
          doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
          
          FileReader reader = new FileReader(file);      
          
          doc.add(new Field("filename", reader));
          
          long fileSize = file.length();
          
          out.println("Indexed " + file.getName() + " File size: " + fileSize);
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } 
      }
    }
    long totalIndexingTime = System.currentTimeMillis() - indexingStartTime;
    
    
    out.println("Indexed " + files.length + " files in " + totalIndexingTime + "ms");
    
    
    
  }
  
}
