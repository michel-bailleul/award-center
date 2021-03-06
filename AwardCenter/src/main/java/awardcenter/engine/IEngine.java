package awardcenter.engine;


import static util.resource.Logger.getLogger;


import util.collection.ReadOnlyIterator;
import util.resource.Logger;

import awardcenter.model.Game;


/**
 * The {@code IEngine} interface should be implemented by any class
 * whose instances are intended to provide persistence services
 *
 * @author Michel BAILLEUL
 *
 * @version 1.4 [2o14-o6-26]
 * @since   1.0 [2o1o-o1-o1]
 */
public interface IEngine<R> extends Iterable<Game> {

  Logger logger = getLogger(IEngine.class);

  /**
   * <p>
   * Root of the data source
   * </p>
   *
   * @return The root of the data source
   */
  R getRoot();

  /**
   * <p>
   * Root of the data source
   * </p>
   *
   * @param root - The root of the data source
   */
  void setRoot(R root);

  /**
   * <p>
   * Save a game to the data source
   * </p>
   *
   * @param game - The game to save
   *
   * @return {@code true} if and only if the game is successfully saved
   */
  boolean saveGame(Game game);

  /**
   * <p>
   * Remove a game from the data source
   * </p>
   *
   * @param game - The game to remove
   *
   * @return {@code true} if and only if the game is successfully removed
   */
  boolean removeGame(Game game);

  /**
   * <p>
   * Stop the engine
   * </p>
   */
  void stop();


  interface GameIterator extends ReadOnlyIterator<Game> { }

  @Override
  GameIterator iterator();

}