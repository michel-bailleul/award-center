package awardcenter.engine;


import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import awardcenter.model.Award;

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


//  @Test
  public void testSaveGame() {

    File file = new File(DIR, "xstream-dummy-test.xml");

    Game game = new Game();
    game.setId(file);
    game.setName("xstream dummy test");

    Award award = new Award();
    award.setLabel("award 1");
    game.addAward(award);
    award = new Award();
    award.setLabel("award 2");
    game.addAward(award);

    IEngine engine = new XStreamEngine();
    engine.saveGame(game);

  }


//  @Test
  public void testLoadGame() {

    File file = new File(DIR, "xstream-ikaruga.xml");

    IEngine engine = new XStreamEngine();
    Game game = engine.loadGame(file);
    System.out.printf("%s : %s", game, game.getAwards());

  }


  @Test
  public void copyGames() throws JAXBException {

    IEngine sourceEngine = new JAXBEngine();
//    IEngine targetEngine = new XMLCodecEngine();
    IEngine targetEngine = new SqlJetEngine();

    for (File sourceFile : ((File)sourceEngine.getRoot()).listFiles()) {
      System.out.printf("Load [%s]%n", sourceFile.getName());
      Game game = sourceEngine.loadGame(sourceFile);
      System.out.printf("Copy [%s]%n", game.getName());
//      File targetFile = new File((File)targetEngine.getRoot(), sourceFile.getName());
//      game.setId(targetFile);
      game.setId(null);
      targetEngine.saveGame(game);
    }

  }


}