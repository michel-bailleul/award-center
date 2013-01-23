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
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

import awardcenter.model.Award;
import awardcenter.model.Game;


public final class JAXBEngine extends FileEngine {


  // ————————————————————————————————————————————————————————————— Inner Classes


  @XmlRootElement(name="game")
  private static class GameJAXB extends Game {

    /** default constructor */
    private GameJAXB() { }

    private GameJAXB(Game game) {
      setId(game.getId());
      setName(game.getName());
      setDeveloper(game.getDeveloper());
      setPublisher(game.getPublisher());
      setRating(game.getRating());
      setScore(game.getScore());
      setScoreMax(game.getScoreMax());
      setImage(game.getImage());
      setBytes(game.getBytes());
      setAwards(game.getAwards());
    }

    @Override
    @XmlElement(name="award")
    @XmlElementWrapper(name="award-list")
    public List<Award> getAwards() {
      return super.getAwards();
    }

  }


  // —————————————————————————————————————————————————————————————— Constructors


  public JAXBEngine() throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(GameJAXB.class);
    marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    unmarshaller = jc.createUnmarshaller();
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private Marshaller marshaller;

  private Unmarshaller unmarshaller;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public File getRoot() {
    return new File("data/jaxb");
  }


  @Override
  public Game loadGame(Object id) {

    Game game = null;
    File file = getFile(id);

    try {
      InputStream is = new FileInputStream(file);
      Reader reader = new BufferedReader(new InputStreamReader(is, UTF_8));
      game = (Game) unmarshaller.unmarshal(new StreamSource(reader));
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
  public boolean saveGame(Game game) {

    File file = getFile(game);

    try {
      OutputStream os = new FileOutputStream(file);
      Writer writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
      marshaller.marshal(new GameJAXB(game), writer);
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