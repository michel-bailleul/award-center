package awardcenter.engine;


import static com.mongodb.MongoClient.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.pojo.PojoCodecProvider.builder;


import java.util.Iterator;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import awardcenter.model.Game;


/**
 * <b>MongoDB engine</b><br/><br/>
 *
 * <pre>
 * ┌───────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
 * │                                                    HISTORIQUE                                                     │
 * ├────────┬────────────┬──────────┬──────────────────────────────────────────────────────────────────────────────────┤
 * │ Auteur │ Date       │ Version  │ Description                                                                      │
 * ├────────┼────────────┼──────────┼──────────────────────────────────────────────────────────────────────────────────┤
 * │ MBL    │ 2ø17-ø9-3ø │ 01.00.00 │ Creation                                                                         │
 * └────────┴────────────┴──────────┴──────────────────────────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * @author Michel BAILLEUL
 *
 * @version 1.00 [2ø17-ø9-3ø]
 * @since   1.00 [2ø17-ø9-3ø]
 */
public class MongoDBEngine implements IEngine<MongoClient> {


  // ————————————————————————————————————————————————————————————————————————————————————————————————————— Inner Classes


  public static class ゲーム extends Game { // katakana, yes we can !

    /** default constructor */
    public ゲーム() { }

    private ゲーム(Game game) {
      setObjectId((ObjectId) game.getId());
      setName(game.getName());
      setDeveloper(game.getDeveloper());
      setPublisher(game.getPublisher());
      setRating(game.getRating());
      setScore(game.getScore());
      setScoreMax(game.getScoreMax());
      setImage(game.getImage());
      setBytes(game.getBytes());
      setAwards(game.getAwards());
    }

    @BsonId
    private ObjectId objectId;

    public ObjectId getObjectId() {
      return objectId;
    }

    public void setObjectId(ObjectId objectId) {
      this.objectId = objectId;
    }

  }


  private class MongoDBIterator implements GameIterator {

    Iterator<ゲーム> it = collection.find().iterator();

    @Override
    public boolean hasNext() {
      return it.hasNext();
    }

    @Override
    public Game next() {
      ゲーム game = it.next();
      game.setId(game.getObjectId());
      return game;
    }

  }


  // —————————————————————————————————————————————————————————————————————————————————————————————————————— Constructors


  public MongoDBEngine() {
    setRoot(new MongoClient("localhost", 27017));
    CodecRegistry cr = fromRegistries(getDefaultCodecRegistry(), fromProviders(builder().automatic(true).build()));
    database = root.getDatabase("game");
    collection = database.getCollection("game", ゲーム.class).withCodecRegistry(cr);
  }


  // ———————————————————————————————————————————————————————————————————————————————————————————————— Instance Variables


  private MongoClient root;

  private MongoDatabase database;

  private MongoCollection<ゲーム> collection;


  // ———————————————————————————————————————————————————————————————————————————————————————————————————— Public Methods


  @Override
  public MongoClient getRoot() {
    return root;
  }


  @Override
  public void setRoot(MongoClient root) {
    this.root = root;
  }


  @Override
  public boolean saveGame(Game game) {

    ゲーム 私のゲーム = new ゲーム(game);

    try {
      if (game.getId() == null) {
        collection.insertOne(私のゲーム);
      }
      else {
        collection.replaceOne(eq("_id", game.getId()), 私のゲーム);
      }
    }
    catch (MongoException x) {
      logger.error("MongoDB failed to insert", x);
      return false;
    }

    return true;

  }


  @Override
  public boolean removeGame(Game game) {

    DeleteResult result = null;

    if (game.getId() != null) {
      result = collection.deleteOne(eq("_id", game.getId()));
    }

    return result != null && result.getDeletedCount() > 0;

  }


  @Override
  public void stop() {
    if (root != null) {
      root.close();
    }
  }


  @Override
  public GameIterator iterator() {
    return new MongoDBIterator();
  }


}
