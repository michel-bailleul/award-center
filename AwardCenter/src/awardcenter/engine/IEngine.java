package awardcenter.engine;


import util.resource.Logger;

import awardcenter.model.Game;


public interface IEngine {

  Logger logger = Logger.getLogger(IEngine.class);

  Object getRoot();

  Game loadGame(Object id);

  boolean saveGame(Game game);

}
