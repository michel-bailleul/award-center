package awardcenter.engine;


import static java.nio.charset.StandardCharsets.UTF_8;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

import awardcenter.model.Award;
import awardcenter.model.Game;


public final class XStreamEngine extends FileEngine {


  // —————————————————————————————————————————————————————————————— Constructors


  public XStreamEngine() {
    setRoot("data/xstream");
    xstream = new XStream(new PureJavaReflectionProvider());
    xstream.alias("game", Game.class);
    xstream.alias("award", Award.class);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private XStream xstream;


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected Game loadFromFile(File file) throws Exception {

    Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
    Game game = (Game) xstream.fromXML(reader);
    reader.close();

    return game;

  }


  @Override
  protected void saveToFile(Game game, File file) throws Exception {

    Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), UTF_8));
    xstream.toXML(game, writer);
    writer.close();

  }


}