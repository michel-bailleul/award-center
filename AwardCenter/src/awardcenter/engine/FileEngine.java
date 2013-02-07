package awardcenter.engine;


import static util.misc.StringUtil.formatMessage;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import awardcenter.model.Game;


public abstract class FileEngine implements IEngine {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String DEFAULT_EXT = ".xml";


  // —————————————————————————————————————————————————————————— Abstract Methods


  /**
   * <p>
   * Load a game from a file
   * </p>
   *
   * @param file - The file of the game
   * @return The loaded game
   */
  protected abstract Game loadFromFile(File file) throws Exception;


  /**
   * <p>
   * Save a game to a file
   * </p>
   *
   * @param game - The game to save
   * @param file - The file
   */
  protected abstract void saveToFile(Game game, File file) throws Exception;


  // ——————————————————————————————————————————————————————————— Private Methods


  private File getFile(Object id) {

    if (id instanceof File) {
      return (File) id;
    }
    else {
      throw new IllegalArgumentException(formatMessage("The Id of the game should be a file [{0}]", id));
    }

  }


  private File getFile(Game game) {
    return getFile(game.getId());
  }


  // ————————————————————————————————————————————————————————— Protected Methods


  protected String getExt() {
    return DEFAULT_EXT;
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
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


  @Override
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


  @Override
  public boolean removeGame(Game game) {

    boolean isOk;
    File file = (File) game.getId();

    if (isOk = (file != null && file.delete())) {
      logger.info("Deleting file [{0}]", file.getAbsolutePath());
    }

    return isOk;

  }


  @Override
  public Game loadGame(Object id) {

    Game game = null;
    File file = getFile(id);

    try {
      game = loadFromFile(file);
    }
    catch (IOException x) {
      logger.error("I/O Exception [{0}]", x, file);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception [{0}]", x, id);
    }

    return game;

  }


  @Override
  public boolean saveGame(Game game) {

    File file = null;

    try {
      file = getFile(game);
      saveToFile(game, file);
      return true;
    }
    catch (IOException x) {
      logger.error("I/O Exception [{0}]", x, file);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception [{0}]", x, game);
    }

    return false;

  }


}