package lucenedemo;

import org.apache.lucene.store.RAMDirectory;

public class BookFinderFactory {
  
  private static BookFinder finder;
  
  private BookFinderFactory() {};
  
  public static synchronized BookFinder getInstance() {
    
   if(finder == null) {
     finder = new BookFinder(new RAMDirectory());
   }
   return finder;
    
  }
  
  public static synchronized void reset() {
    finder = null;
  }

}
