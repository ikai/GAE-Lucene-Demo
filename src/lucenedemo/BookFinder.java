package lucenedemo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MergeScheduler;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

public class BookFinder implements Serializable {

  private static final Logger log = Logger.getLogger(BookFinder.class.getName());

  private final Directory index;

  private IndexWriter writer;

  public BookFinder(Directory index) {
    this.index = index;
  }
  
  public int size() throws IOException {
    return this.index.listAll().length;
  }

  public void openIndexForWriting() throws IOException, IndexNotWritableException {
    log.info("Opened Index for writing");
    try {
      writer =
          new IndexWriter(index, new StandardAnalyzer(Version.LUCENE_30),
              IndexWriter.MaxFieldLength.LIMITED);
      
      // writer.setMaxBufferedDocs(50);
      writer.setRAMBufferSizeMB(0.3);
      MergeScheduler mergeScheduler = new SerialMergeScheduler();
      this.writer.setMergeScheduler(mergeScheduler);
      
    } catch (CorruptIndexException e) {
      throw new IndexNotWritableException("Corrupted Index");
    } catch (LockObtainFailedException e) {
      throw new IndexNotWritableException("Failed to obtain lock on index");
    }

  }

  public void close() throws IOException, IndexNotWritableException {
    try {
      
      this.writer.close();
    } catch (CorruptIndexException e) {
      throw new IndexNotWritableException("Could not close IndexWriter");
    } finally {
      this.writer = null;
    }
  }
  
  public void indexSize() throws IOException {
    String[] files = this.index.listAll();
    
    for(String file : files) {
      long length = this.index.fileLength(file);
      log.info("File size: " + this.index.fileLength(file));
      
    }
  }

  public void addBook(Book book) throws IndexNotWritableException, IOException {

    if (this.writer == null) {
      throw new IndexNotWritableException("IndexWriter closed. Must call openIndexForWriting()");
    } else {
      writer.addDocument(book.toDocument());
    }

  }


  /*
   * Code taken from here
   * http://ikaisays.com/2010/04/24/lucene-in-memory-search-
   * example-now-updated-for-lucene-3-0-1/
   */
  public BookResults search(String queryString) throws CorruptIndexException, IOException,
      ParseException, IndexNotBuiltException {

    QueryParser parser =
        new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));

    Query query = parser.parse(queryString);

    int hitsPerPage = 10;

    TopScoreDocCollector collector = TopScoreDocCollector.create(5 * hitsPerPage, false);
    
    BookResults results;

    try {
      Searcher searcher = new IndexSearcher(index);

      searcher.search(query, collector);

      ScoreDoc[] hits = collector.topDocs().scoreDocs;

      int hitCount = collector.getTotalHits();
      log.info(hitCount + " total matching documents for query: " + queryString);

      // Examine the Hits object to see if there were any matches
      results = new BookResults(hitCount);
      if (hitCount == 0) {
        log.info("No matches were found for \"" + queryString + "\"");
        
      } else {
        
        // Iterate over the Documents in the Hits object
        for (int i = 0; i < hits.length; i++) {
          ScoreDoc scoreDoc = hits[i];
          int docId = scoreDoc.doc;
          float docScore = scoreDoc.score;
          log.info("docId: " + docId + "\t" + "docScore: " + docScore);

          Document doc = searcher.doc(docId);

          Book book = Book.buildFromDocument(doc);
          
          // Print the value that we stored in the "title" field. Note
          // that this Field was not indexed, but (unlike the
          // "contents" field) was stored verbatim and can be
          // retrieved.
          log.info("  " + (i + 1) + ". " + book);
          results.addBook(book);
          
        }

      }

    } catch (FileNotFoundException e) {
      throw new IndexNotBuiltException(e.getMessage());
    }


    return results;
  }

  class IndexNotWritableException extends Exception {

    public IndexNotWritableException(String message) {
      super(message);
    }

  }

  class IndexNotBuiltException extends Exception {

    public IndexNotBuiltException(String message) {
      super(message);
    }

  }


}
