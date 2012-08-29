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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import awardcenter.model.Game;


public final class JAXBEngine implements IEngine {


  // —————————————————————————————————————————————————————————————— Constructors


  public JAXBEngine() throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(Game.class);
    marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    unmarshaller = jc.createUnmarshaller();
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private Marshaller marshaller;
  private Unmarshaller unmarshaller;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public File getDir() {
    return new File("jaxb");
  }


  @Override
  public Game loadGame(File file) {

    Game game = null;

    try {
      InputStream is = new FileInputStream(file);
      Reader in = new BufferedReader(new InputStreamReader(is, UTF_8));
      game = unmarshaller.unmarshal(new StreamSource(in), Game.class).getValue();
      in.close();
    }
    catch (FileNotFoundException x) {
      logger.error("File Not Found", x);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception: {0}", x, file.getName());
    }

    return game;

  }


  @Override
  public boolean saveGame(Game game, File file) {

    try {
      OutputStream os = new FileOutputStream(file);
      Writer out = new OutputStreamWriter(os, UTF_8);
      out = new BufferedWriter(out);
      JAXBElement<Game> jaxbElement = new JAXBElement<Game>(new QName("game"), Game.class, game);
      marshaller.marshal(jaxbElement, out);
      out.close();
      return true;
    }
    catch (FileNotFoundException x) {
      logger.error("File Not Found", x);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception: {0}", x, file.getName());
    }

    return false;

  }


}
