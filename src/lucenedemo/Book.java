package lucenedemo;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class Book {

  private String id;

  private String title;

  private String author;

  private String language;

  public static Book buildFromDocument(Document doc) {
    Book book = new Book();

    book.setAuthor(doc.get("author"));
    book.setLanguage(doc.get("language"));
    book.setTitle(doc.get("title"));
    book.setId(doc.get("id"));

    return book;
  }

  public Document toDocument() {

    Document doc = new Document();

    doc.add(new Field("id", this.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));

    if (this.getTitle() != null) {
      doc.add(new Field("title", this.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
    }

    if (this.getAuthor() != null) { // Prevent "value cannot be null"
      // exception
      doc.add(new Field("author", this.getAuthor(), Field.Store.YES, Field.Index.ANALYZED));
    }

    if (this.getLanguage() != null) {
      // The Human Genome, for instance, has no language
      doc.add(new Field("language", this.getLanguage(), Field.Store.YES, Field.Index.NOT_ANALYZED));
    }
    return doc;

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public String toString() {
    return "Book [author=" + author + ", id=" + id + ", language=" + language + ", title=" + title
        + "]";
  }



}
