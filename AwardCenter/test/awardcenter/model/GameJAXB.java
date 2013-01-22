package awardcenter.model;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="game")
public class GameJAXB extends Game {


  /** default constructor */
  public GameJAXB() { }


  public GameJAXB(Game game) {

    setId(game.getId());
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


  @Override
  @XmlElement(name="award")
//  @XmlElementWrapper(name="awards") // does not work !
  @XmlElementWrapper(name="award-list")
  public List<Award> getAwards() {
    return super.getAwards();
  }


}