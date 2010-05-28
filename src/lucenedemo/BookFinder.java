package lucenedemo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class BookFinder {
  
  private static final Logger log = Logger.getLogger(BookFinder.class.getName());
  
  private final Directory index;
  
  private IndexWriter writer;
  
  public BookFinder(Directory index) {
    this.index = index;
  }
  
  public void openIndexForWriting() throws IOException, IndexNotWritableException {
    log.info("Opened Index for writing");
    try {
      writer = new IndexWriter(index, 
          new StandardAnalyzer(Version.LUCENE_30), 
          IndexWriter.MaxFieldLength.LIMITED);
    } catch (CorruptIndexException e) {
      throw new IndexNotWritableException("Corrupted Index");
    } catch (LockObtainFailedException e) {
      throw new IndexNotWritableException("Failed to obtain lock on index");
    } 
    
  }
  
  public void close() throws IOException, IndexNotWritableException {
    try {
      
      this.writer.close();
    }
    catch (CorruptIndexException e){
     throw new IndexNotWritableException("Could not close IndexWriter");
    } finally {
      this.writer = null;
    }
  }
  
  public void addBook(Book book) throws IndexNotWritableException, IOException {
    
    if (this.writer == null) {
      throw new IndexNotWritableException("IndexWriter closed. Must call openIndexForWriting()");
    } else {
      Document doc = new Document();
      
      // Books ALWAYS have IDs

      doc.add(new Field("id", book.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));

      if(book.getTitle() != null) {
        doc.add(new Field("title", book.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
      }
      
      if(book.getAuthor() != null) { // Prevent "value cannot be null" exception
        doc.add(new Field("author", book.getAuthor(), Field.Store.YES, Field.Index.ANALYZED));
      }
      
      if(book.getLanguage() != null) {
        // The Human Genome, for instance, has no language
        doc.add(new Field("language", book.getLanguage(), Field.Store.YES, Field.Index.NOT_ANALYZED));
      }
      
      
      writer.addDocument(doc);
    }
    
  }


  public void search(String queryString) throws CorruptIndexException, IOException, ParseException {
    Searcher searcher = new IndexSearcher(index);
    
    QueryParser parser = new QueryParser(Version.LUCENE_30, 
        "contents", 
        new StandardAnalyzer(Version.LUCENE_30));
   
    Query query = parser.parse(queryString);
    
    // TODO fill me in
    
  }
  
  class IndexNotWritableException extends Exception {

    public IndexNotWritableException(String message) {
      super(message);
    }
    
  }
  
 
}
