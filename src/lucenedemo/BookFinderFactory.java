package lucenedemo;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class BookFinderFactory {
  
  private static Directory index;
  
  /*
   * Build the index
   * 
   * Don't know if it makes sense to write it like this
   */
  public static void init() {
    index = new RAMDirectory();
    init(index);
  }
  
  public static void init(Directory index) {
    File dir = new File("WEB-INF/texts");
    File[] files = dir.listFiles();
    
    for(File file : files) {
      System.out.println("Reading : " + file.getName());
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
