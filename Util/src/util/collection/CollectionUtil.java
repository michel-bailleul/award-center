package util.collection;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.invokeMethod;


import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import util.misc.StringUtil;


public final class CollectionUtil {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(CollectionUtil.class);


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Verifie le contenu de la collection s'il est vide ou <code>null</code>
   *
   * @param c - La collection a verifier
   * @return <b>true</b> si la collection est vide ou <code>null</code><br/>
   *         <b>false</b> si la collection n'est pas vide
   */
  public static boolean isEmpty(Collection<?> c) {

    if (c != null && !c.isEmpty()) {
      Iterator<?> it = c.iterator();
      while (it.hasNext()) {
        if (it.next() != null) {
          return false;
        }
      }
    }

    return true;

  }


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


  /**
   * Regroupement d'une collection de beans par une de leurs proprietes.<br/><br/>
   * Exemple avec la liste suivante :<br/>
   * <code>[{"Jean","DURAND"},{"Paul","DURAND"},{"Jean","DUPOND"},{"Paul","DUPOND"}]</code><br/><br/>
   * Un regroupement par la propriete "nom" donnera la Map :<br/>
   * <code>["DURAND":[{"Jean","DURAND"},{"Paul","DURAND"}];"DUPOND":[{"Jean","DUPOND"},{"Paul","DUPOND"}]]</code><br/><br/>
   * Un regroupement par la propriete "prenom" donnera la Map :<br/>
   * <code>["Jean":[{"Jean","DURAND"},{"Jean","DUPOND"}];"Paul":[{"Paul","DURAND"},{"Paul","DUPOND"}]]</code><br/>
   *
   * @param collection - La collection de donnees
   * @param property   - Le nom de la propriete qui sert de critere de regroupement
   * @return Une map de la forme [key:property;value:List]
   */
  public static <T,C extends Collection<T>> Map<Object,C> groupBy(C collection, String property) {

    Map<Object,C> map = new HashMap<Object,C>();

    if (!StringUtil.isEmpty(property) && !isEmpty(collection)) {
      Method method = getGetter(collection.iterator().next().getClass(), property);
      if (method != null) {
        for (T o : collection) {
          if (o != null) {
            Object key = invokeMethod(o, method);
            if (!map.containsKey(key)) {
              try {
                @SuppressWarnings("unchecked")
                C c = (C) collection.getClass().newInstance();
                map.put(key, c);
              }
              catch (ReflectiveOperationException x) {
                throw new UnsupportedOperationException(x);
              }
            }
            map.get(key).add(o);
          }
        }
      }
    }

    return map;

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private CollectionUtil() { }


}