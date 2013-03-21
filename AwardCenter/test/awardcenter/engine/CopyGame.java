package awardcenter.engine;


import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import awardcenter.model.Game;


public class CopyGame {


  // —————————————————————————————————————————————————————————————— Test Methods


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