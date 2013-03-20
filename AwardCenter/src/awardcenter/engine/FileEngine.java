package awardcenter.engine;


import static util.io.FileUtil.clean;
import static util.misc.StringUtil.formatMessage;
import static util.misc.StringUtil.NBSP;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import awardcenter.model.Game;


public abstract class FileEngine implements IEngine {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String DEFAULT_EXT = ".xml";


  // ———————————————————————————————————————————————————————— Instance Variables


  private File root;


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


  private File _getFile(Object id) {

    if (id instanceof File) {
      return (File) id;
    }
    else {
      throw new IllegalArgumentException(formatMessage("The Id of the game should be a file [{0}]", id));
    }

  }


  private File _getFile(Game game) {
    return _getFile(game.getId());
  }


  // ————————————————————————————————————————————————————————— Protected Methods


  protected String getExt() {
    return DEFAULT_EXT;
  }


  protected void setRoot(String root) {
    this.root = new File(root);
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public File getRoot() {
    return root;
  }


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
    File file = _getFile(id);

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

    File trash = null;
    File file = _getFile(game);
    String fileName = clean(game.getName().replace(NBSP, '-')) + getExt();

    if (file == null || !file.getName().equals(fileName) || !file.getParentFile().equals(getRoot())) {
      trash = file;
      file = new File(getRoot(), fileName);
      game.setId(file);
    }

    try {
      saveToFile(game, file);
      if (trash != null) {
        trash.delete();
        logger.info("Deleting [{0}]", trash.getAbsolutePath());
      }
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


  public void stop() { }


}