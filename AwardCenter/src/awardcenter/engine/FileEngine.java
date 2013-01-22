package awardcenter.engine;


import java.io.File;

import awardcenter.model.Game;


public abstract class FileEngine implements IEngine {


  protected File getFile(Object id) {

    if (id instanceof File) {
      return (File) id;
    }
    else {
      //TODO: error message
      throw new IllegalArgumentException("TODO");
    }

  }


  protected File getFile(Game game) {
    return getFile(game.getId());
  }


}
