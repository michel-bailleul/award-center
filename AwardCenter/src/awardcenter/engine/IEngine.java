package awardcenter.engine;


import java.io.File;

import util.resource.Logger;


import awardcenter.model.Game;


public interface IEngine {

  Logger logger = Logger.getLogger(IEngine.class);

  File getDir();

  Game loadGame(File file);

  boolean saveGame(Game game, File file);

}
