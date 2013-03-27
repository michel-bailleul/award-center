package awardcenter.engine;


import java.util.Iterator;

import awardcenter.model.Game;


abstract class ReadOnlyIterator implements Iterator<Game> {

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}