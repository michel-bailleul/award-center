package awardcenter.model;


import static util.misc.StringUtil.nbsp;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.swing.app.Bean;


public class Game extends Bean implements Comparable<Game> {


  // —————————————————————————————————————————————————————————————— Constructors


  public Game() {
    setActive(false);
    awards = new ArrayList<Award>();
  }


  // ———————————————————————————————————————————————————————————————— Properties

  // file ----------------------------------------------------------------------

  private transient File file;

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  // name ----------------------------------------------------------------------

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String old = this.name;
    this.name = nbsp(name);
    firePropertyChange("name", old, this.name);
  }

  // developer -----------------------------------------------------------------

  private String developer;

  public String getDeveloper() {
    return developer;
  }

  public void setDeveloper(String developer) {
    String old = this.developer;
    this.developer = developer;
    firePropertyChange("developer", old, developer);
  }

  // publisher -----------------------------------------------------------------

  private String publisher;

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    String old = this.publisher;
    this.publisher = publisher;
    firePropertyChange("publisher", old, publisher);
  }

  // rating --------------------------------------------------------------------

  private int rating;

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    int old = this.rating;
    this.rating = rating;
    firePropertyChange("rating", old, rating);
  }

  // score ---------------------------------------------------------------------

  private int score;

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    int old = this.score;
    this.score = score;
    firePropertyChange("score", old, score);
  }

  // score max -----------------------------------------------------------------

  private int scoreMax;

  public int getScoreMax() {
    return scoreMax;
  }

  public void setScoreMax(int scoreMax) {
    int old = this.scoreMax;
    this.scoreMax = scoreMax;
    firePropertyChange("scoreMax", old, scoreMax);
  }

  // image ---------------------------------------------------------------------

  private String image;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  // bytes ---------------------------------------------------------------------

  private transient byte[] bytes;

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    byte[] old = this.bytes;
    this.bytes = bytes;
    firePropertyChange("bytes", old, bytes);
  }

  // awards --------------------------------------------------------------------

  private List<Award> awards;

  public List<Award> getAwards() {
    return awards;
  }

  public void setAwards(List<Award> awards) {
    this.awards = awards;
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public void addAward(Award award) {

    if (award != null && awards != null) {
      award.setIndex(awards.size());
      awards.add(award);
    }

  }

  public void removeAward(Award award) {

    if (award != null && awards != null) {
      if (awards.remove(award)) {
        setDirty(true);
        for (int i=0; i < awards.size(); i++) {
          awards.get(i).setIndex(i);
        }
        updateScore();
      }
    }

  }

  public boolean moveAward(Award award, int index) {

    if (!(index < 0) && index < awards.size() && index != awards.indexOf(award)) {
      awards.remove(award);
      awards.add(index, award);
      for (int i=0; i < awards.size(); i++) {
        awards.get(i).setIndex(i);
      }
      return true;
    }

    return false;

  }


  public void updateScore() {

    int score = 0;
    int scoreMax = 0;

    for (Award award : awards) {
      if (award.isAchieved()) {
        score += award.getValue();
      }
      scoreMax += award.getValue();
    }

    setScore(score);
    setScoreMax(scoreMax);

  }


  @Override
  public String toString() {
    return getName();
  }


  @Override
  public int compareTo(Game game) {
    return game != null ? getName().compareToIgnoreCase(game.getName()) : 0;
  }


  @Override
  public Game clone() {

    Game clone = new Game();

    clone.setFile(file);
    clone.setName(name);
    clone.setDeveloper(developer);
    clone.setPublisher(publisher);
    clone.setRating(rating);
    clone.setScore(score);
    clone.setScoreMax(scoreMax);
    clone.setImage(image);
    clone.setBytes(bytes);

    for (Award award : awards) {
      clone.getAwards().add(award.clone());
    }

    return clone;

  }


}