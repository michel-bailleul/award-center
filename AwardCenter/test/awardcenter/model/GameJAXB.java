package awardcenter.model;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="game")
public class GameJAXB extends Game {


  @XmlElementWrapper
  @XmlElement(name="award")
  public List<Award> getAwards() {
    return super.getAwards();
  }


}
