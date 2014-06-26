package awardcenter.engine;


import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.schema.ISqlJetSchema;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import awardcenter.model.Award;
import awardcenter.model.Game;


public class SqlJetEngine implements IEngine {


  // ————————————————————————————————————————————————————————————— Inner Classes


  private class SqlJetIterator extends ReadOnlyIterator {

    private boolean hasNext = false;

    private ISqlJetTable table = null;
    private ISqlJetCursor cursorGame = null;
    private ISqlJetCursor cursorAward = null;

    private SqlJetIterator() {

      try {
        db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        table = db.getTable(TABLE_GAME);
        cursorGame = table.open();
        hasNext = !cursorGame.eof();
      }
      catch (SqlJetException x) {
        logger.error("todo", x); // TODO: externalize
      }

    }

    @Override
    public boolean hasNext() {
      return hasNext;
    }

    @Override
    public Game next() {

      Game game = null;

      try {
        if (hasNext) {
          Object[] values = cursorGame.getRowValues();
          game = new Game();
          game.setId(values[0]);
          game.setName((String)values[1]);
          game.setDeveloper((String)values[2]);
          game.setPublisher((String)values[3]);
          game.setRating(((Long)values[4]).intValue());
          game.setScore(((Long)values[5]).intValue());
          game.setScoreMax(((Long)values[6]).intValue());
          game.setImage((String)values[7]);

          // awards
          List<Award> awards = new ArrayList<Award>();
          game.setAwards(awards);
          table = db.getTable(TABLE_AWARD);
          cursorAward = table.lookup(INDEX_AWARD, game.getId());

          if (!cursorAward.eof()) {
            do {
              values = cursorAward.getRowValues();
              Award award = new Award();
              award.setId(values[0]);
              award.setValue(((Long)values[2]).intValue());
              award.setIndex(((Long)values[3]).intValue());
              award.setLabel((String)values[5]);
              award.setDescription((String)values[6]);
              award.setTipsAndTricks((String)values[7]);
              award.setImage((String)values[8]);
              _setFlags(award, ((Long)values[4]).intValue());
              awards.add(award);
            }
            while (cursorAward.next());
            cursorAward.close();
          }
          hasNext = cursorGame.next();
        }
        if (!hasNext) {
          cursorGame.close();
          db.commit();
        }
      }
      catch (SqlJetException x) {
        logger.error("todo", x); // TODO: externalize
      }
      finally {
        try {
          if (cursorAward != null) {
            cursorAward.close();
          }
        }
        catch (SqlJetException x) {
          logger.error("todo", x); // TODO: externalize
        }
      }

      return game;

    }

  }


  // —————————————————————————————————————————————————————————— Static Constants


  // database ------------------------------------------------------------------

  private static final String TABLE_GAME = "GAME";

  private static final String TABLE_AWARD = "AWARD";

  private static final String CREATE_TABLE_GAME =
    "CREATE TABLE GAME ("
  +   "ID        INTEGER PRIMARY KEY AUTOINCREMENT,"
  +   "NAME      VARCHAR(255),"
  +   "DEVELOPER VARCHAR(255),"
  +   "PUBLISHER VARCHAR(255),"
  +   "RATING    INTEGER,"
  +   "SCORE     INTEGER,"
  +   "SCORE_MAX INTEGER,"
  +   "IMAGE     TEXT"
  + ")";

  private static final String CREATE_TABLE_AWARD =
    "CREATE TABLE AWARD ("
  +   "ID          INTEGER PRIMARY KEY AUTOINCREMENT,"
  +   "ID_GAME     INTEGER NOT NULL,"
  +   "VALUE       INTEGER,"
  +   "INDX        INTEGER,"
  +   "FLAGS       INTEGER,"
  +   "LABEL       VARCHAR(255),"
  +   "DESCRIPTION TEXT,"
  +   "TIPS        TEXT,"
  +   "IMAGE       TEXT"
  + ")";

  private static final String INDEX_AWARD = "INDEX_AWARD";

  private static final String CREATE_INDEX_AWARD =
    "CREATE INDEX INDEX_AWARD ON AWARD(ID_GAME)";


  // flags ---------------------------------------------------------------------

  private static final int ACHIEVED  = 0b00000001;
  private static final int ADDED     = 0b00000010;
  private static final int MULTI     = 0b00000100;
  private static final int SECRET    = 0b00001000;
  private static final int SEPARATOR = 0b00010000;


  // ———————————————————————————————————————————————————————————— Static Methods


  private static int _getFlags(Award award) {
    int flags = 0;
    flags += award.isAchieved()  ? ACHIEVED  : 0;
    flags += award.isAdded()     ? ADDED     : 0;
    flags += award.isMulti()     ? MULTI     : 0;
    flags += award.isSecret()    ? SECRET    : 0;
    flags += award.isSeparator() ? SEPARATOR : 0;
    return flags;
  }


  private static void _setFlags(Award award, int flags) {
    award.setAchieved( (flags & ACHIEVED)  > 0);
    award.setAdded(    (flags & ADDED)     > 0);
    award.setMulti(    (flags & MULTI)     > 0);
    award.setSecret(   (flags & SECRET)    > 0);
    award.setSeparator((flags & SEPARATOR) > 0);
  }


  private static Object[] _toValues(Game game) {
    return new Object[] {
      game.getId(),
      game.getName(),
      game.getDeveloper(),
      game.getPublisher(),
      game.getRating(),
      game.getScore(),
      game.getScoreMax(),
      game.getImage()
    };
  }


  private static Object[] _toValues(Game game, Award award) {
    return new Object[] {
      award.getId(),
      game.getId(),
      award.getValue(),
      award.getIndex(),
      _getFlags(award),
      award.getLabel(),
      award.getDescription(),
      award.getTipsAndTricks(),
      award.getImage()
    };
  }


  // —————————————————————————————————————————————————————————————— Constructors


  public SqlJetEngine() {
    setRoot("data/sqljet/game.db");
    _init();
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private String root;

  private File file;

  private SqlJetDb db;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _init() {

    file = new File(root);

    // create database
    try {
      db = SqlJetDb.open(file, true);

      ISqlJetSchema schema = db.getSchema();
      Set<String> tables = schema.getTableNames();

      if (tables == null || !(tables.contains(TABLE_GAME) && tables.contains(TABLE_AWARD))) {
        file.delete();
        db = SqlJetDb.open(file, true);
        // set DB option that have to be set before running any transactions:
        db.getOptions().setAutovacuum(true);
        // set DB option that have to be set in a transaction:
        db.runTransaction(
          new ISqlJetTransaction() {
            public Object run(SqlJetDb db) throws SqlJetException {
              db.getOptions().setUserVersion(1);
              return true;
            }
          },
          SqlJetTransactionMode.WRITE
        );
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        db.createTable(CREATE_TABLE_GAME);
        db.createTable(CREATE_TABLE_AWARD);
        db.createIndex(CREATE_INDEX_AWARD);
        db.commit();
      }
    }
    catch (SqlJetException x) {
      logger.error("todo", x); // TODO: externalize
    }

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public Object getRoot() {
    return root;
  }


  @Override
  public void setRoot(Object root) {
    this.root = (String) root;
  }


  @Override
  public boolean saveGame(Game game) {

    ISqlJetTable table = null;
    ISqlJetCursor cursor = null;

    try {
      db.beginTransaction(SqlJetTransactionMode.WRITE);

      // game ------------------------------------------------------------------

      table = db.getTable(TABLE_GAME);

      if (game.getId() == null) {
        // insert
        long id = table.insert(_toValues(game));
        game.setId(id);
      }
      else if (game.isDirty()) {
        // update
        cursor = table.lookup(null, game.getId());
        if (!cursor.eof()) {
          cursor.update(_toValues(game));
        }
        cursor.close();
      }

      // awards ----------------------------------------------------------------

      table = db.getTable(TABLE_AWARD);

      Set<Object> ids = new HashSet<Object>();
      cursor = table.lookup(INDEX_AWARD, game.getId());

      if (!cursor.eof()) {
        do {
          ids.add(cursor.getRowId());
        }
        while (cursor.next());
      }
      cursor.close();

      for (Award award : game.getAwards()) {
        if (award.getId() == null) {
          // INSERT
          long id = table.insert(_toValues(game, award));
          award.setId(id);
        }
        else if (award.isDirty()) {
          // UPDATE
          cursor = table.lookup(null, award.getId());
          if (!cursor.eof()) {
            cursor.update(_toValues(game, award));
          }
          cursor.close();
        }
        ids.remove(award.getId());
      }

      // DELETE
      for (Object id : ids) {
        cursor = table.lookup(null, id);
        if (!cursor.eof()) {
          cursor.delete();
        }
        cursor.close();
      }

      return true;
    }
    catch (SqlJetException x) {
      logger.error("todo", x); // TODO: externalize
    }
    finally {
      try {
        if (cursor != null) {
          cursor.close();
        }
        db.commit();
      }
      catch (SqlJetException x) {
        logger.error("todo", x); // TODO: externalize
      }
    }

    return false;

  }


  @Override
  public boolean removeGame(Game game) {

    ISqlJetTable table = null;
    ISqlJetCursor cursor = null;

    try {
      db.beginTransaction(SqlJetTransactionMode.WRITE);

      // awards ----------------------------------------------------------------

      table = db.getTable(TABLE_AWARD);

      cursor = table.lookup(INDEX_AWARD, game.getId());
      while (!cursor.eof()) {
        cursor.delete();
      }
      cursor.close();

      // game ------------------------------------------------------------------

      table = db.getTable(TABLE_GAME);

      cursor = table.lookup(null, game.getId());
      if (!cursor.eof()) {
        cursor.delete();
      }
      cursor.close();

      return true;
    }
    catch (SqlJetException x) {
      logger.error("todo", x); // TODO: externalize
    }
    finally {
      try {
        if (cursor != null) {
          cursor.close();
        }
        db.commit();
      }
      catch (SqlJetException x) {
        logger.error("todo", x); // TODO: externalize
      }
    }

    return false;

  }


  @Override
  public void stop() {
    if (db != null) {
      try {
        db.close();
        logger.info("Stop SqlJet Engine");
      }
      catch (SqlJetException x) {
        logger.error("todo", x); // TODO: externalize
      }
    }
  }


  @Override
  public ReadOnlyIterator iterator() {
    return new SqlJetIterator();
  }


}