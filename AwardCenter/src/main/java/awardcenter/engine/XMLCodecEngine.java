package awardcenter.engine;


import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import awardcenter.model.Game;


public final class XMLCodecEngine extends FileEngine {


  // —————————————————————————————————————————————————————————————— Constructors


  public XMLCodecEngine() {
    setRoot("data/xmlcodec");
  }


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected Game loadFromFile(File file) throws Exception {

    ExceptionListener el = new ExceptionListener() {
      @Override
      public void exceptionThrown(Exception e) {
        throw new RuntimeException(e);
      }
    };

    XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)), null, el);
    Game game = (Game)decoder.readObject();
    decoder.close();

    return game;

  }


  @Override
  protected void saveToFile(Game game, File file) throws Exception {

    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
    encoder.writeObject(game);
    encoder.close();

  }


}