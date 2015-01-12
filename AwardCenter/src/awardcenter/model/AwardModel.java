package awardcenter.model;


import static java.lang.System.currentTimeMillis;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import static util.misc.StringUtil.isEmpty;
import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getMsg;

import static awardcenter.resources.Key.AWARD_NEW;
import static awardcenter.resources.Key.AWARD_NEW_SEPARATOR;
import static awardcenter.resources.Key.GAME_NEW;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.resource.Logger;
import util.swing.app.ListModel;

import awardcenter.engine.IEngine;


public class AwardModel {


  // ————————————————————————————————————————————————————————————————————— Enums


  private static enum Mode {

    SAVE   ("Saving", "saved"),
    IMPORT ("Importing", "imported"),
    EXPORT ("Exporting", "exported");

    private int i;
    private String[] txt;

    private Mode(String... txt) {
      this.txt = txt;
    }

    public Mode setIndex(int i) {
      this.i = i;
      return this;
    }

    @Override
    public String toString() {
      String s = txt[i];
      i = 0;
      return s;
    }

  }


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(AwardModel.class);


  // —————————————————————————————————————————————————————————————— Constructors


  public AwardModel(IEngine engine) {
    games = new ArrayList<Game>();
    this.engine = engine;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private IEngine engine;

  private List<Game> games;


  // ——————————————————————————————————————————————————————————— Private Methods


  /* version 3.0 - Iterator */
  private List<Game> _loadGames(Object root, ListModel<Game> model) {

    long time = currentTimeMillis();
    long lastUpdate = time;

    int loaded = 0;
    List<Game> games = new ArrayList<Game>();

    if (model != null) {
      model.setAutoSort(false);
      model.setList(games);
    }

    if (root != null) {
      engine.setRoot(root);
    }

    for (Game game : engine) {
      loaded++;
      logger.info("Loading [{0}]", game.getName());
      // Game image
      if (!isEmpty(game.getImage())) {
        game.setBytes(_getBytes(game));
      }
      // Award images
      for (Award award : game.getAwards()) {
        if (!isEmpty(award.getImage())) {
          award.setActive(false);
          award.setBytes(_getBytes(award));
          award.setImage(null);
        }
        award.setDirty(false);
        award.setActive(true);
      }
      game.setImage(null);
      game.setDirty(false);
      game.setActive(true);
      games.add(game);
      // update list & model
      if (model != null && (currentTimeMillis() - lastUpdate) > 500) {
        model.update();
        lastUpdate = currentTimeMillis();
      }
    }

    if (model != null) {
      model.setAutoSort(true);
      model.update();
    }

    if (root == null) {
      setGames(games);
    }

    logger.info("{0} game{0, choice, |1<s} loaded in {1,number,0} ms", loaded, currentTimeMillis() - time);

    return games;

  }


  /* version 2.0
  private List<Game> _loadGames(Object root, ListModel<Game> model) {

    long time = currentTimeMillis();

    int loaded = 0;
    List<Game> games = engine.loadGames(root);

    for (Game game : games) {
      loaded++;
      // Game image
      if (!isEmpty(game.getImage())) {
        game.setBytes(_getBytes(game));
      }
      // Award images
      for (Award award : game.getAwards()) {
        if (!isEmpty(award.getImage())) {
          award.setActive(false);
          award.setBytes(_getBytes(award));
          award.setImage(null);
        }
        award.setDirty(false);
        award.setActive(true);
      }
      game.setImage(null);
      game.setDirty(false);
      game.setActive(true);
    }

    logger.info("{0} game{0, choice, |1<s} loaded in {1,number,0} ms", loaded, currentTimeMillis() - time);

    return games;

  }
  */


  /* version 1.0 : older but faster [10%]
  private List<Game> _loadGames(Object root) {

    long time = currentTimeMillis();

    int loaded = 0;
    Game game = null;
    List<Game> games = new ArrayList<Game>();
    Object[] ids = engine.getIds(root);

    if (!ArrayUtil.isEmpty(ids)) {
      for (Object id : ids) {
        if ((game = engine.loadGame(id)) == null) {
          continue;
        }
        games.add(game);
        loaded++;
        logger.info("Loading [{0}]", game.getName());
        game.setId(id);
        // Game image
        if (!isEmpty(game.getImage())) {
          game.setBytes(_getBytes(game));
        }
        // Award images
        for (Award award : game.getAwards()) {
          if (!isEmpty(award.getImage())) {
            award.setActive(false);
            award.setBytes(_getBytes(award));
            award.setImage(null);
          }
          award.setDirty(false);
          award.setActive(true);
        }
        game.setImage(null);
        game.setDirty(false);
        game.setActive(true);
      }
    }

    logger.info("{0} game{0, choice, |1<s} loaded in {1,number,0} ms", loaded, currentTimeMillis() - time);

    return games;

  }
  */


  private byte[] _getBytes(Object o) {

    try {
//      return decodeBase64ToByte((o instanceof Game) ? ((Game)o).getImage() : ((Award)o).getImage());
      return parseBase64Binary((o instanceof Game) ? ((Game)o).getImage() : ((Award)o).getImage());
    }
    catch (Exception x) {
      logger.error("Image corrompue", x); //TODO: terminer le message avec un 'choice', boolean ?
      return null;
    }

  }


  private String _getString(Object o) {

    try {
      return printBase64Binary((o instanceof Game) ? ((Game)o).getBytes() : ((Award)o).getBytes());
    }
    catch (Exception x) {
      logger.error("Image corrompue", x); //TODO: terminer le message avec un 'choice', boolean ?
      return null;
    }

  }


  private void _saveGames(List<Game> games, Object root, Mode mode) {

    long time = currentTimeMillis();
    int saved = 0;

    for (Game game : games) {
      if (_saveGame(game, root, mode)) {
        saved++;
      }
    }

    if (saved > 0) {
      logger.info("{0} game{0, choice, |1<s} {1} in {2,number,0} ms", saved, mode.setIndex(1), currentTimeMillis() - time);
    }

  }


  private boolean _saveGame(Game game, Object root, Mode mode) {

    boolean isDirty = false;
    boolean isSaved = false;

    if (game != null) {
      isDirty = (mode != Mode.SAVE) || game.isDirty();
      if (!isDirty) {
        for (Award award : game.getAwards()) {
          isDirty |= award.isDirty();
        }
      }
    }

    if (isDirty) {
      game.setActive(false);
//      game.setDirty(false);

      // Game image
      byte[] bytes = game.getBytes();
//      game.setImage(encodeBase64ToString(bytes));
      game.setImage(_getString(game));
      game.setBytes(null);

      // Award images
      Map<Award, byte[]> awardImages = new HashMap<Award, byte[]>();
      for (Award award : game.getAwards()) {
        award.setActive(false);
//        award.setDirty(false);
//        award.setImage(encodeBase64ToString(award.getBytes()));
        award.setImage(_getString(award));
        awardImages.put(award, award.getBytes());
        award.setBytes(null);
      }

      Object rt = engine.getRoot();
      Object id = game.getId();
      if (root != null) {
        engine.setRoot(root);
      }

      if (isSaved = engine.saveGame(game)) {
        logger.info("{0} [{1}]", mode, game.getName());
      }

      if (mode == Mode.EXPORT) { // en cas d'export...
        engine.setRoot(rt);      // ...on revient dans le repertoire racine...
        game.setId(id);          // ...et au fichier source
      }
      else {
        // Game image
        game.setImage(null);
        game.setBytes(bytes);
        game.setDirty(!isSaved && game.isDirty());
        game.setActive(true);

        // Award images
        for (Award award : game.getAwards()) {
          award.setImage(null);
          award.setBytes(awardImages.get(award));
          award.setDirty(!isSaved && award.isDirty());
          award.setActive(true);
        }
//        if (mode == Mode.SAVE && trash != null) {
//          trash.delete();
//          logger.info("Deleting [{0}]", trash.getAbsolutePath());
//        }
      }
    }

    return isSaved;

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }


  public Game createGame() {

    Game game = new Game();
    game.setName(getMsg(GAME_NEW));
    game.setActive(true);
    games.add(game);

    return game;

  }


  public void deleteGame(Game game) {

    if (game != null) {
      games.remove(game);
      engine.removeGame(game);
      logger.info("Removing [{0}]", game.getName());
    }

  }


  public Award createAward(Game game) {

    Award award = null;

    if (game != null) {
      award = new Award();
      game.addAward(award);
      award.setLabel(getMsg(AWARD_NEW));
      award.setDirty(false);
    }

    return award;

  }


  public Award createSeparator(Game game) {

    Award award = null;

    if (game != null) {
      award = new Award();
      award.setSeparator(true);
      game.addAward(award);
      award.setLabel(getMsg(AWARD_NEW_SEPARATOR));
      award.setDirty(false);
    }

    return award;

  }


  public void deleteAward(Game game, Award award) {

    if (game != null && award != null) {
      game.removeAward(award);
    }

  }


  public List<Game> loadGames(Object dir) {
    return _loadGames(dir, null);
  }


  public List<Game> loadGames(ListModel<Game> model) {
    return _loadGames(null, model);
  }


  public void saveGames() {
    _saveGames(games, null, Mode.SAVE);
  }


  public void importGames(List<Game> games) {
    _saveGames(games, null, Mode.IMPORT);
    this.games.addAll(games);
  }


  public void exportGames(List<Game> games, Object root) {
    _saveGames(games, root, Mode.EXPORT);
  }


}