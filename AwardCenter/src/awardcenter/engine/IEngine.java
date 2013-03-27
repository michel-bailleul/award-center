package awardcenter.engine;


import util.resource.Logger;

import awardcenter.model.Game;


/**
 * The {@code IEngine} interface should be implemented by any class
 * whose instances are intended to provide persistence services
 *
 * @author Michel BAILLEUL
 *
 * @version 1.3 [2o13-o3-27]
 * @since   1.0 [2o1o-o1-o1]
 */
public interface IEngine extends Iterable<Game> {

  Logger logger = Logger.getLogger(IEngine.class);

  /**
   * <p>
   * Root of the data source
   * </p>
   *
   * @return The root of the data source
   */
  Object getRoot();

  /**
   * <p>
   * Save a game to the data source
   * </p>
   *
   * @param game - The game to save
   * @return {@code true} if and only if the game is successfully saved
   */
  boolean saveGame(Game game);

  /**
   * <p>
   * Remove a game from the data source
   * </p>
   *
   * @param game - The game to remove
   * @return {@code true} if and only if the game is successfully removed
   */
  boolean removeGame(Game game);

  /**
   * <p>
   * Stop the engine
   * </p>
   */
  void stop();

  @Override
  ReadOnlyIterator iterator();

}