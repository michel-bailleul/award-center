package test;


import java.io.*;


public class MakeBig {

  public static void main(String args[]) throws IOException {
    if (args.length != 2) {
      System.err.println("Usage: filename length(K)");
      System.exit(1);
    }

    // open output file

    FileOutputStream fos = new FileOutputStream(args[0]);
    int len = Integer.parseInt(args[1]);
    byte[] vec = new byte[1024];

    // write out 1K chunks to the file

    for (int i = 0; i < vec.length; i++)
      vec[i] = (byte)'*';
    for (int i = 0; i < len; i++)
      fos.write(vec);

    fos.close();
  }

}