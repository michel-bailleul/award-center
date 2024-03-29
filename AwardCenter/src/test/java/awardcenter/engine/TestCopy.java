package awardcenter.engine;


import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import awardcenter.model.Game;


public class TestCopy {


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void copyGames() throws JAXBException {

    IEngine<File> sourceEngine = new JAXBEngine();
//    IEngine targetEngine = new XMLCodecEngine();
    IEngine<?> targetEngine = new MongoDBEngine();

    targetEngine.forEach(targetEngine::removeGame);
//    for (Game game : targetEngine) {
//      targetEngine.
//    }

    for (Game game : sourceEngine) {
      System.out.printf("Copy [%s]%n", game.getName());
//      File targetFile = new File((File)targetEngine.getRoot(), sourceFile.getName());
//      game.setId(targetFile);
      game.setId(null);
      targetEngine.saveGame(game);
    }

  }


}