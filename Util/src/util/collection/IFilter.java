package util.collection;


public interface IFilter<T> {

  boolean matches(T obj);

}
