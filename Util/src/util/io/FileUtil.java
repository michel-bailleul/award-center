package util.io;


import static util.resource.Logger.getLogger;
import static util.resources.LogKey.FILE_UTIL_COPY;
import static util.resources.LogKey.FILE_UTIL_IOX;
import static util.resources.LogKey.FILE_UTIL_NOT_FOUND;
import static util.resources.LogKey.FILE_UTIL_READ;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import util.resource.Logger;


public final class FileUtil {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(FileUtil.class);


  private static final int BUFFER_SIZE = 65536; // 64k


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Methode utilitaire de copie d'un fichier
   *
   * @param in  - Fichier source
   * @param out - Fichier cible
   */
  public static void copy(File in, File out) {

    if (in == null || out == null) {
      return;
    }

    try (FileChannel inChannel = new FileInputStream(in).getChannel();
         FileChannel outChannel = new FileOutputStream(out).getChannel())
    {
      inChannel.transferTo(0, inChannel.size(), outChannel);
      logger.info(FILE_UTIL_COPY, in.getAbsolutePath(), out.getAbsolutePath());
    }
    catch (IOException x) {
      logger.error(FILE_UTIL_IOX, x);
    }


  }


  /**
   * Lit et retourne le contenu d'un fichier binaire
   *
   * @param file - Fichier en entree
   * @return Le contenu du fichier sous forme binaire
   */
  public static byte[] getBytes(File file) {

    if (file == null || !file.isFile()) {
      return null;
    }

    byte[] bytes = null;

    try (FileInputStream fis = new FileInputStream(file);
         BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
         ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
         BufferedOutputStream bos = new BufferedOutputStream(baos, BUFFER_SIZE))
    {
      logger.debug(FILE_UTIL_READ, file.getPath());
      final byte[] buffer = new byte[BUFFER_SIZE];
      while (bis.read(buffer) != -1) {
        bos.write(buffer);
      };
      bos.flush();
      bytes = baos.toByteArray();
    }
    catch (FileNotFoundException x) {
      logger.error(FILE_UTIL_NOT_FOUND, x, file);
    }
    catch (IOException x) {
      logger.error(FILE_UTIL_IOX, x);
    }

    return bytes;

  }


  /**
   *
   * @param fileName - nom du fichier
   * @return le nom de fichier apres
   */
  public static String clean(String fileName) {
    return fileName.replace(':', '=');
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private FileUtil() { }


}