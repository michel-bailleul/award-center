package awardcenter.engine;


import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import awardcenter.model.Game;


public final class XMLCodecEngine implements IEngine {


  @Override
  public File getDir() {
    return new File("data");
  }


  @Override
  public Game loadGame(File file) {

    Game game = null;
    XMLDecoder decoder = null;
    ExceptionListener el = new ExceptionListener() {
      @Override
      public void exceptionThrown(Exception e) {
        throw new RuntimeException(e);
      }
    };

    try {
      FileInputStream fis = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(fis);
      decoder = new XMLDecoder(bis, null, el);
      game = (Game)decoder.readObject();
    }
    catch (FileNotFoundException x) {
      logger.error("File Not Found", x);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception: {0}", x, file.getName());
    }
    finally {
      if (decoder != null) {
        decoder.close();
      }
    }

    return game;

  }


  @Override
  public boolean saveGame(Game game, File file) {

    XMLEncoder encoder = null;

    try {
      FileOutputStream fos = new FileOutputStream(file);
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      encoder = new XMLEncoder(bos);
      encoder.writeObject(game);
      return true;
    }
    catch (FileNotFoundException x) {
      logger.error("File Not Found", x);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception: {0}", x, file.getName());
    }
    finally {
      if (encoder != null) {
        encoder.close();
      }
    }

    return false;

  }


}
