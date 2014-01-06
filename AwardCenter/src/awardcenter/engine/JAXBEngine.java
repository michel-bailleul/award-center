package awardcenter.engine;


import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.bind.JAXBContext.newInstance;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;


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
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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

    @XmlJavaTypeAdapter(TransientAdapter.class)
    private Object active;

    @XmlJavaTypeAdapter(TransientAdapter.class)
    private Object dirty;

  }


  private static class TransientAdapter extends XmlAdapter<Object, Object> {

    @Override
    public Object unmarshal(Object v) throws Exception {
      return null;
    }

    @Override
    public Object marshal(Object v) throws Exception {
      return null;
    }

  }


  // —————————————————————————————————————————————————————————————— Constructors


  public JAXBEngine() throws JAXBException {
    setRoot("data/jaxb");
    JAXBContext jc = newInstance(GameJAXB.class);
    marshaller = jc.createMarshaller();
    marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
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