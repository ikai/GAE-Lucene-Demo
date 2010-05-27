package lucenedemo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class BookFinder {
  
  private static final String BOOK_DIRECTORY = "WEB-INF/texts";

  private static final Logger log = Logger.getLogger(BookFinder.class.getName());
  
  private final Directory index;
  
  public BookFinder(Directory index) {
    this.index = index;
  }

  /*
   * Build the index. Iterates through each file and adds it to the index
   */
  public void buildIndex() throws IOException {
    log.info("Building Index ...");
    
    long indexingStartTime = System.currentTimeMillis();

    IndexWriter writer = new IndexWriter(index, 
         new StandardAnalyzer(Version.LUCENE_30), 
         IndexWriter.MaxFieldLength.LIMITED);
    
    File dir = new File(BOOK_DIRECTORY);
    File[] files = dir.listFiles();
    
    for(File file : files) {
      addToIndex(writer, file);
    }
    
    // Optimize causes thread errors, GRRR
    // writer.optimize();
    writer.close();

    long totalIndexingTime = System.currentTimeMillis() - indexingStartTime;
    log.info("Indexed " + files.length + " files in " + totalIndexingTime + "ms");

  }
  
  /*
   * Add a file to an IndexWriter 
   */
  private void addToIndex(IndexWriter writer, File file) throws CorruptIndexException, IOException {
    if(file.isFile()) {
      try {
        Document doc = new Document();
        doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.ANALYZED));          
        doc.add(new Field("filename", new FileReader(file)));
        
        writer.addDocument(doc);
        long fileSize = file.length();          
        log.info("Indexed " + file.getName() + " File size: " + fileSize);
      } catch (FileNotFoundException e) {
        // This would be a good place for a log.wtf(), since we you can't delete
        // files from GAE after uploading it to appspot.com
        e.printStackTrace();
      } 
    }
  }
  
  
  
  
  
  
}
