package awardcenter.engine;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import awardcenter.model.Game;


public abstract class FileEngine implements IEngine {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String DEFAULT_EXT = ".xml";


  // ————————————————————————————————————————————————————————— Protected Methods


  protected String getExt() {
    return DEFAULT_EXT;
  }


  protected File getFile(Object id) {

    if (id instanceof File) {
      return (File) id;
    }
    else {
      throw new IllegalArgumentException("The Id of the game should be a file");
    }

  }


  protected File getFile(Game game) {
    return getFile(game.getId());
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public File[] getIds(Object root) {

    File dir = (File) ((root == null) ? getRoot() : root);

    File[] ids = dir.listFiles(
      new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(getExt());
        }
      }
    );

    return ids;

  }


  public List<Game> loadGames(Object root) {

    List<Game> games = new ArrayList<Game>();

    for (File file : getIds(root)) {
      Game game = loadGame(file);
      if (game != null) {
        game.setId(file);
        games.add(game);
        logger.info("Loading [{0}]", game.getName());
      }
    }

    return games;

  }


  public boolean removeGame(Game game) {

    boolean isOk;
    File file = (File) game.getId();

    if (isOk = (file != null && file.delete())) {
      logger.info("Deleting file [{0}]", file.getAbsolutePath());
    }

    return isOk;

  }


}