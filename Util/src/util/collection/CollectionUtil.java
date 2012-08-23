package util.collection;


import java.util.Collection;
import java.util.Iterator;


public final class CollectionUtil {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(CollectionUtil.class);


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Filter a collection of objects implementing {@link IFilter Filter} interface
   *
   * @param c - The collection to be filtered
   *
   * @see IFilter
   */
  public static <T extends IFilter<? super T>>  void filter(Collection<T> c) {

    if (c != null) {
      Iterator<T> it = c.iterator();
      while (it.hasNext()) {
        T o = it.next();
        if (!o.matches(o)) {
          it.remove();
        }
      }
    }

  }


  /**
   * Copy into a target collection the filtering result of a source collection
   * of objects implementing {@link IFilter Filter} interface
   *
   * @param src - Source collection
   * @param trg - Target collection
   *
   * @see IFilter
   */
  public static <T extends IFilter<? super T>> void filter(Collection<T> src, Collection<T> trg)
  {
    if (src != null && trg != null) {
      trg.clear();
      for (T obj : src) {
        if (obj.matches(obj)) {
          trg.add(obj);
        }
      }
    }

  }


  /**
   * Filter a collection by an object implementing
   * {@link IFilter Filter} interface
   *
   * @param c - The collection to be filtered
   * @param f - The filter
   *
   * @see IFilter
   */
  public static <T> void filter(Collection<T> c, IFilter<T> f) {

    if (c != null) {
      Iterator<T> it = c.iterator();
      while (it.hasNext()) {
        T o = it.next();
        if (f != null && !f.matches(o)) {
          it.remove();
        }
      }
    }

  }


  /**
   * Copy into a target collection the filtering result of a source collection
   * by an object implementing {@link IFilter Filter} interface
   *
   * @param src - Source collection
   * @param trg - Target collection
   * @param f   - The filter
   *
   * @see IFilter
   */
  public static <T> void filter(Collection<T> src, Collection<T> trg, IFilter<T> f)
  {
    if (src != null && trg != null) {
      trg.clear();
      for (T obj : src) {
        if (f == null || f.matches(obj)) {
          trg.add(obj);
        }
      }
    }

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private CollectionUtil() { }


}