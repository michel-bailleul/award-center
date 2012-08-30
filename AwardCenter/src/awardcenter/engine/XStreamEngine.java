package awardcenter.engine;


import static java.nio.charset.StandardCharsets.UTF_8;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

import awardcenter.model.Award;
import awardcenter.model.Game;


public final class XStreamEngine implements IEngine {


  // —————————————————————————————————————————————————————————————— Constructors


  public XStreamEngine() {
    xstream = new XStream(new PureJavaReflectionProvider());
    xstream.alias("game", Game.class);
    xstream.alias("award", Award.class);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private XStream xstream;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public File getDir() {
    return new File("data/xstream");
  }


  @Override
  public Game loadGame(File file) {

    Game game = null;

    try {
      InputStream is = new FileInputStream(file);
      Reader reader = new BufferedReader(new InputStreamReader(is, UTF_8));
      game = (Game) xstream.fromXML(reader);
      reader.close();
    }
    catch (FileNotFoundException x) {
      logger.error("File Not Found [{0}]", x, file.getName());
    }
    catch (Exception x) {
      logger.error("Unexpected Exception [{0}]", x, file.getName());
    }

    return game;

  }


  @Override
  public boolean saveGame(Game game, File file) {

    try {
      OutputStream os = new FileOutputStream(file);
      Writer writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
      xstream.toXML(game, writer);
      writer.close();
      return true;
    }
    catch (FileNotFoundException x) {
      logger.error("File Not Found [{0}]", x, file.getName());
    }
    catch (Exception x) {
      logger.error("Unexpected Exception [{0}]", x, file.getName());
    }

    return false;

  }


}
