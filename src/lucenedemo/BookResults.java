package lucenedemo;

import java.util.LinkedList;
import java.util.List;

public class BookResults {
  private int totalHits;
  private List<Book> books = new LinkedList<Book>();
  
  
  public BookResults(int totalHits) {
    super();
    this.totalHits = totalHits;
  }
  
  public int getTotalHits() {
    return totalHits;
  }
  
  public void setTotalHits(int totalHits) {
    this.totalHits = totalHits;
  }
  
  public void addBook(Book book) {
    books.add(book);
  }
  
  
}
