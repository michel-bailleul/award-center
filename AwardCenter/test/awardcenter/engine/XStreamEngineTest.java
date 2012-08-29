package awardcenter.engine;


import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import awardcenter.model.Game;


public class XStreamEngineTest {


  // —————————————————————————————————————————————————————————— Static Constants


  private static File DIR = new File("trash");


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() { }


  @After
  public void after() { }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testSaveGame() {
    
    File file = new File(DIR, "xstream-dummy-test.xml");

    Game game = new Game();
    game.setName("xstream dummy test");

    IEngine engine = new XStreamEngine();
    engine.saveGame(game, file);

  }


  @Test
  public void testLoadGame() {

    File file = new File(DIR, "xstream-ikaruga.xml");

    IEngine engine = new XStreamEngine();
    Game game = engine.loadGame(file);

  }


}
