package awardcenter.engine;


import java.util.List;

import util.resource.Logger;

import awardcenter.model.Game;


/**
 * The {@code IEngine} interface should be implemented by any class
 * whose instances are intended to provide persistence services
 *
 * @author Michel BAILLEUL
 *
 * @version 1.1 [2o13-o1-23]
 * @since   1.0 [2o1o-o1-o1]
 */
public interface IEngine {

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
   * List of Ids
   * </p>
   *
   * @param root - The root of the data source
   * @return The list of Ids
   */
  Object[] getIds(Object root);

  /**
   * <p>
   * Load a game from the data source
   * </p>
   *
   * @param id - The id of the game
   * @return The loaded game
   */
  Game loadGame(Object id);

  /**
   * <p>
   * Load all games from the data source
   * </p>
   *
   * @param root - The root of the data source
   * @return The list of all games
   */
  List<Game> loadGames(Object root);

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

}
