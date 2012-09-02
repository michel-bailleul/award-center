package awardcenter.engine;


import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import awardcenter.model.Award;

import awardcenter.model.Game;


public class XMLCodecEngineTest {


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

    File file = new File(DIR, "xmlcodec-dummy-test.xml");

    Game game = new Game();
    game.setName("xmlcodec dummy test");

    Award award = new Award();
    award.setLabel("award 1");
    game.addAward(award);
    award = new Award();
    award.setLabel("award 2");
    game.addAward(award);

    IEngine engine = new XMLCodecEngine();
    engine.saveGame(game, file);

  }


  @Test
  public void testLoadGame() {

    File file = new File(DIR, "xmlcodec-ikaruga.xml");

    IEngine engine = new XMLCodecEngine();
    Game game = engine.loadGame(file);
    System.out.printf("%s : %s", game, game.getAwards());

  }


}