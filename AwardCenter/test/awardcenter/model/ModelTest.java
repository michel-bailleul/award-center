package awardcenter.model;


import static java.nio.charset.StandardCharsets.UTF_8;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ModelTest {


  // —————————————————————————————————————————————————————————— Static Constants


  private static File DIR = new File("trash");


  // ———————————————————————————————————————————————————————————— Static Methods


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }


  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private JAXBContext jc;

  private File file;


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    jc = JAXBContext.newInstance(GameJAXB.class);
    file = new File(DIR, "jaxb-wrapper-test.xml");
  }


  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testWrapperMarshal() throws JAXBException, IOException {

    Game game = new GameJAXB();
    game.setName("The Game");
    
    // awards
    Award award;

    // award 1
    award = new Award();
    award.setLabel("Award I");
    game.addAward(award);

    // award 2
    award = new Award();
    award.setLabel("Award II");
    game.addAward(award);

    Marshaller marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    OutputStream os = new FileOutputStream(file);
    Writer writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
    marshaller.marshal(game, writer);
    writer.close();

  }


  @Test
  public void testWrapperUnmarshal() throws JAXBException, IOException {

    Unmarshaller unmarshaller = jc.createUnmarshaller();
    InputStream is = new FileInputStream(file);
    Reader reader = new BufferedReader(new InputStreamReader(is, UTF_8));
    Game game = (Game) unmarshaller.unmarshal(new StreamSource(reader));
    reader.close();
    System.out.printf("%s : %s", game, game.getAwards());

  }


}