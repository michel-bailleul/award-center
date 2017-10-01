package misc;


import java.util.HashMap;
import java.lang.ref.SoftReference;
import java.io.*;


public class FileCache {


  // hash table that maps pathnames -> SoftReference objects
  private HashMap map = new HashMap();


  // read a file into a byte vector and put it in the table
  private byte[] readin(String fn) throws IOException {

    // open the file

    FileInputStream fis = new FileInputStream(fn);

    // read all its bytes

    long filelen = new File(fn).length();
    byte[] vec = new byte[(int)filelen];
    fis.read(vec);
    fis.close();

    // put the (pathname, vector) entry into the hash table

    map.put(fn, new SoftReference(vec));

    return vec;

  }


  // get the byte vector for a file
  public byte[] getFile(String fn) throws IOException {

    // look up the pathname in the hash table

    SoftReference ref = (SoftReference)map.get(fn);
    byte[] vec;

    // if the name is not there, or the referent has been cleared,
    // then read from disk

    if (ref == null || (vec = (byte[])ref.get()) == null) {
      System.err.println("read " + fn + " from disk");
      return readin(fn);
    }
    else {
      System.err.println("read " + fn + " from cache");
      return vec;
    }

  }


  public static void main(String args[]) throws IOException {

    // set up the cache for files

    FileCache cache = new FileCache();
    byte[] vec;

    // read in big files the first time

    vec = cache.getFile("big1");
    vec = cache.getFile("big2");
    //vec = cache.getFile("big3");

    // read the second time from cache or from disk

    vec = cache.getFile("big1");
    vec = cache.getFile("big2");

  }


}