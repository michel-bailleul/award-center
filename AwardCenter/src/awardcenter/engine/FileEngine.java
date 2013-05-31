package awardcenter.engine;


import static util.io.FileUtil.clean;
import static util.misc.StringUtil.formatMessage;
import static util.misc.StringUtil.NBSP;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import awardcenter.model.Game;


public abstract class FileEngine implements IEngine {


  // ————————————————————————————————————————————————————————————— Inner Classes


  private class FileIterator extends ReadOnlyIterator {

    private int index = 0;
    private File[] files = null;

    private FileIterator() {
      if (root != null) {
        files = root.listFiles(
          new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
              return name.toLowerCase().endsWith(getExt());
            }
          }
        );
      }
    }

    @Override
    public boolean hasNext() {
      return (files != null) && (index < files.length);
    }

    @Override
    public Game next() {
      return hasNext() ? _loadGame(files[index++]) : null;
    }

  }


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

    if (id == null || id instanceof File) {
      return (File) id;
    }
    else {
      throw new IllegalArgumentException(formatMessage("The Id of the game should be a file [{0}]", id));
    }

  }


  private File _getFile(Game game) {
    return _getFile(game.getId());
  }


  private Game _loadGame(Object id) {

    Game game = null;
    File file = _getFile(id);

    try {
      game = loadFromFile(file);
      game.setId(file);
    }
    catch (IOException x) {
      logger.error("I/O Exception [{0}]", x, file);
    }
    catch (Exception x) {
      logger.error("Unexpected Exception [{0}]", x, id);
    }

    return game;

  }


  // ————————————————————————————————————————————————————————— Protected Methods


  protected String getExt() {
    return DEFAULT_EXT;
  }


  protected void setRoot(String root) {
    this.root = new File(root);
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public File getRoot() {
    return root;
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
  public void stop() {
  }


  @Override
  public ReadOnlyIterator iterator() {
    return new FileIterator();
  }


}