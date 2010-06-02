// Copyright 2010 Google Inc. All Rights Reserved.

package lucenedemo;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ikai@google.com (Your Name Here)
 *
 *  First attempt at creating a Lucene Directory that can be layered on top
 *  of App Engine's datastore as a virtual filesystem.
 *  
 *  The virtual filesystem makes use of an Entity "File" with the following
 *  properties:
 *  key: the filename
 *  lastModified: long timestamp of when the file was last modified
 *  content: Blob containing the contents of the file
 */
public class DatastoreDirectory extends Directory {
  

  private static final String FILE_ENTITY_KIND = "File";
  DatastoreService datastore;

  public DatastoreDirectory() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }
  
  /* 
   * Doesn't do anything since we don't have to close anything
   */
  @Override
  public void close() throws IOException {
    // Not sure we have to do anything here
  }

  /* (non-Javadoc)
   * @see org.apache.lucene.store.Directory#createOutput(java.lang.String)
   */
  @Override
  public IndexOutput createOutput(String arg0) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* 
   * Deletes the file with the given name from the virtual filesystem
   * 
   * @param name The name of the file to delete
   */
  @Override
  public void deleteFile(String name) {
    Key key = filenameToKey(name);
    datastore.delete(key);    
  }

  /* Check to see if the file exists in the virtual filesystem
   * @param name The name of the file to check for existence
   * @return A boolean indicating whether the file exists or not
   */
  @Override
  public boolean fileExists(String name) throws IOException {
    Key key = filenameToKey(name);
    
    try {
      datastore.get(key);
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
    
  }

  /* Return the length of the virtual file
   * @param name Name of the file to check for length
   */
  @Override
  public long fileLength(String name) throws IOException {
    Key key = filenameToKey(name);
    
    try {
      Entity fileEntity = datastore.get(key);
      Blob fileContents = (Blob) fileEntity.getProperty("content");
      
      // TODO: If this method gets called a lot, may make sense to refactor
      // this to be a cached value generated on write rather than something
      // we count each time
      return fileContents.getBytes().length;
    } catch (EntityNotFoundException e) {
      throw new FileNotFoundException(name);
    }
    
  }

  /* Return the last modified time of the file
   * @param name The name of the file check for timestamp
   */
  @Override
  public long fileModified(String name) throws IOException {
    Key key = filenameToKey(name);
    try {
      Entity fileEntity = datastore.get(key);
      return ((Long) fileEntity.getProperty("lastModified")).longValue();
    } catch (EntityNotFoundException e) {
      throw new FileNotFoundException(name);
    }
  }

  /* 
   * Goes through all the File type entities in the datastore returning
   * the virtual filenames
   * 
   * @return An array of Strings representing the virtual filenames
   */
  @Override
  public String[] listAll() {

    Query query = new Query(FILE_ENTITY_KIND);
    FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
    
    List<String> filenames = new LinkedList<String>();
    
    
    for(Entity fileEntity : datastore.prepare(query).asIterable()) {
      // TODO: This may not return what we expect, may need KeyBuilder
      filenames.add(fileEntity.getKey().toString());
    }
    
    return (String[]) filenames.toArray();
  }

  /* (non-Javadoc)
   * @see org.apache.lucene.store.Directory#openInput(java.lang.String)
   */
  @Override
  public IndexInput openInput(String arg0) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* Update the timestamp on the file to reflect the last write
   * @name Filename to update timestamp
   */
  @Override
  public void touchFile(String name) throws IOException {
    Key key = filenameToKey(name);
    try {
      Entity fileEntity = datastore.get(key);
      long timestamp = System.currentTimeMillis();
      fileEntity.setProperty("lastModified", timestamp);
      datastore.put(fileEntity);
    } catch (EntityNotFoundException e) {
      throw new FileNotFoundException(name);
    }

  }

  protected Key filenameToKey(String name) {
    Key key = (new KeyFactory.Builder(FILE_ENTITY_KIND, name)).getKey();
    return key;
  }
}
