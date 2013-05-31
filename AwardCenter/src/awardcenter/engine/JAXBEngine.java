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
    setRoot("data/jaxb");
    JAXBContext jc = JAXBContext.newInstance(GameJAXB.class);
    marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    unmarshaller = jc.createUnmarshaller();
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private Marshaller marshaller;

  private Unmarshaller unmarshaller;


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected Game loadFromFile(File file) throws Exception {

    Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
    Game game = (Game) unmarshaller.unmarshal(new StreamSource(reader));
    reader.close();

    return game;

  }


  @Override
  protected void saveToFile(Game game, File file) throws Exception {

    Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), UTF_8));
    marshaller.marshal(new GameJAXB(game), writer);
    writer.close();

  }


}