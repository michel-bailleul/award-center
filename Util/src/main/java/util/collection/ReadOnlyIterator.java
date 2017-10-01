package util.collection;


import java.util.Iterator;


public interface ReadOnlyIterator<E> extends Iterator<E> {

  @Override
  public default void remove() {
    throw new UnsupportedOperationException();
  }

}