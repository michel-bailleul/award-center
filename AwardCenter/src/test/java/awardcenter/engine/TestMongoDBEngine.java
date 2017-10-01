package awardcenter.engine;


import static com.mongodb.MongoClient.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.pojo.PojoCodecProvider.builder;


import java.io.File;
import java.util.function.Consumer;

import javax.xml.bind.JAXBException;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import awardcenter.model.Award;
import awardcenter.model.Game;


public class TestMongoDBEngine {


  // —————————————————————————————————————————————————————————— Static Constants


  private static File DIR = new File("trash");


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() { }


  @After
  public void after() { }


  // —————————————————————————————————————————————————————————————— Test Methods

  public static class ゲーム extends Game {
    @BsonId
    private ObjectId objectId;
    public ObjectId getObjectId() { return objectId; }
    public void setObjectId(ObjectId objectId) { this.objectId = objectId; }
  }

  @Test
  public void testSaveGame() throws Exception {


    Consumer<Object> print = System.out::println;
    File file = new File(DIR, "jaxb-ikaruga.xml");

    FileEngine engine = new JAXBEngine();
    Game g = engine.loadFromFile(file);
    ゲーム game = new ゲーム();
    game.setName(g.getName());
    game.setDeveloper(g.getDeveloper());
    game.setPublisher(g.getPublisher());
    game.setRating(g.getRating());
    game.setScore(g.getScore());
    game.setScoreMax(g.getScoreMax());
    game.setImage(g.getImage());
    game.setBytes(g.getBytes());
    game.setAwards(g.getAwards());
//    game.setObjectId(new ObjectId());
    /*
    Game game = new Game();
    game.setId(file);
    game.setName("jaxb dummy test");

    Award award = new Award();
    award.setLabel("award 1");
    game.addAward(award);
    award = new Award();
    award.setLabel("award 2");
    game.addAward(award);
    */

    try (MongoClient mongoClient = new MongoClient("192.168.1.30", 27017)) {
      CodecRegistry cr = fromRegistries(getDefaultCodecRegistry(), fromProviders(builder().automatic(true).build()));
      MongoDatabase database = mongoClient.getDatabase("test");
      MongoCollection<ゲーム> collection = database.getCollection("game", ゲーム.class).withCodecRegistry(cr);
      collection.drop();
      collection.insertOne(game);
      System.out.println("total # of games : " + collection.count());
//      collection.find().forEach(print);
      System.out.println("objectId: " + game.objectId);
      game = collection.find().first();
      System.out.println("objectId: " + game.objectId);
      System.out.println(game);
    }

  }


//  @Test
  public void testLoadGame() throws Exception {

    File file = new File(DIR, "jaxb-ikaruga.xml");

    FileEngine engine = new JAXBEngine();
    Game game = engine.loadFromFile(file);
//    System.out.printf("%s : %s", game, game.getAwards());

  }


}