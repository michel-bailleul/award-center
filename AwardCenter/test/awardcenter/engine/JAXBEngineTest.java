package awardcenter.engine;


import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import awardcenter.model.Award;
import awardcenter.model.Game;


public class JAXBEngineTest {


  // —————————————————————————————————————————————————————————— Static Constants


  private static File DIR = new File("trash");


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() { }


  @After
  public void after() { }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testSaveGame() throws JAXBException {

    File file = new File(DIR, "jaxb-dummy-test.xml");

    Game game = new Game();
    game.setName("jaxb dummy test");
    
    Award award = new Award();
    award.setLabel("award 1");
    game.addAward(award);
    award = new Award();
    award.setLabel("award 2");
    game.addAward(award);

    IEngine engine = new JAXBEngine();
    engine.saveGame(game, file);

  }


//  @Test
  public void testLoadGame() throws JAXBException {

    File file = new File(DIR, "jaxb-ikaruga.xml");

    IEngine engine = new JAXBEngine();
    Game game = engine.loadGame(file);

  }


}
