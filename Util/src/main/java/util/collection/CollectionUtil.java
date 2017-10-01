package util.collection;


import static util.bean.BeanUtil.getCommonAncestor;
import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.instantiate;
import static util.bean.BeanUtil.invokeMethod;
import static util.resource.Logger.getLogger;
import static util.resources.LogKey.COLLECTION_UTIL_PROPERTY_NOT_FOUND;


import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import util.bean.BeanComparator;
import util.resource.Logger;


public final class CollectionUtil {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(CollectionUtil.class);


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Verifie le contenu de la collection s'il est vide ou {@code null}
   *
   * @param c - La collection a verifier
   *
   * @return {@code true} si la collection est vide ou {@code null}<br/>
   *         {@code false} si la collection n'est pas vide
   */
  public static boolean isEmpty(Collection<?> c) {

    if (c != null) {
      for (Object o : c) {
        if (o != null) {
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
  public static <T extends IFilter<? super T>> void filter(Collection<T> c) {

    if (c != null) {
      Iterator<T> it = c.iterator();
      while (it.hasNext()) {
        T o = it.next();
        if (o == null || !o.matches(o)) {
          it.remove(); // using Collection.remove() method throws a ConcurrentModificationException
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
  public static <T extends IFilter<? super T>> void filter(Collection<T> src, Collection<T> trg) {

    if (src != null && trg != null) {
      trg.clear();
      for (T o : src) {
        if (o != null && o.matches(o)) {
          trg.add(o);
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
  public static <T> void filter(Collection<T> src, Collection<T> trg, IFilter<T> f) {

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
   * <p>
   * Regroupe les elements d'une collection par une de leurs proprietes.
   * A noter que les elements {@code null} sont exclus du traitement.
   * En revanche, les proprietes {@code null} sont parfaitement valides et generent une cle {@code null} dans la Map.
   * </p>
   *
   * Exemple avec la liste suivante :<br/>
   * <code>[{"Jean","DURAND"},{"Paul","DURAND"},{"Jean","DUPOND"},{"Paul","DUPOND"}, null]</code><br/><br/>
   *
   * Un regroupement par la propriete "nom" donnera la Map :<br/>
   * <code>["DURAND":[{"Jean","DURAND"},{"Paul","DURAND"}];<br/>
   *        "DUPOND":[{"Jean","DUPOND"},{"Paul","DUPOND"}]]</code><br/><br/>
   *
   * Un regroupement par la propriete "prenom" donnera la Map :<br/>
   * <code>["Jean":[{"Jean","DURAND"},{"Jean","DUPOND"}];<br/>
   *        "Paul":[{"Paul","DURAND"},{"Paul","DUPOND"}]]</code><br/><br/>
   *
   * @param property - Le nom de la propriete qui sert de critere de regroupement
   * @param c        - La collection source
   *
   * @return Une Map de la forme [key:property;value:List]
   */
  public static <T, C extends Collection<T>> Map<Object,C> groupBy(String property, C c) {

    if (c == null) {
      return null;
    }

    T x = null;
    for (T o : c) {
      if ((x = o) != null) break;
    }

    Method method = null;
    if (x != null && (method = getGetter(property, x.getClass())) == null) {
      throw new IllegalArgumentException(logger.error(COLLECTION_UTIL_PROPERTY_NOT_FOUND, property));
    }

    Map<Object,C> map = new HashMap<>();

    for (T o : c) {
      if (o != null) {
        Object key = invokeMethod(o, method);
        if (!map.containsKey(key)) {
          C value = instantiate(c);
          map.put(key, value);
        }
        map.get(key).add(o);
      }
    }

    return map;

  }


  /**
   * Collecte la valeur d'une propriete de chaque element d'une collection
   *
   * @param property - Le nom de la propriete a collecter
   * @param klass    - Le type de la propriete a collecter
   * @param c        - La collection source
   *
   * @return La valeur de la propriete de chaque element
   */
  public static <C1 extends Collection<?>, C2 extends Collection<U>, U> C2 collect(String property, Class<U> klass, C1 c) {

    if (c == null) {
      return null;
    }

    Class<?> type = getComponentType(c);

    Method method = null;
    if (type != null && (method = getGetter(property, type)) == null) {
      throw new IllegalArgumentException(logger.error(COLLECTION_UTIL_PROPERTY_NOT_FOUND, property));
    }

    @SuppressWarnings("unchecked")
    C2 values = (C2) instantiate(c);

    for (Object o : c) {
      @SuppressWarnings("unchecked")
      U value = (o != null) ? (U) invokeMethod(o, method) : null;
      values.add(value);
    }

    return values;

  }


  /**
   * Collecte la valeur d'une propriete de chaque element d'une collection
   *
   * @param property - Le nom de la propriete a collecter
   * @param c        - La collection source
   *
   * @return La valeur de la propriete de chaque element
   */
  public static <C1 extends Collection<?>, C2 extends Collection<Object>> C2 collect(String property, C1 c) {
    return collect(property, Object.class, c);
  }


  /**
   * Trie une liste en fonction d'une propriete de ses objets
   *
   * @param list     - Liste a trier
   * @param property - Propriete cible du tri
   * @param isAsc    - Tri ascendant / descendant
   */
  public static <T> void sort(String property, boolean isAsc, boolean isNullFisrt, List<T> list) {

    if (isEmpty(list)) {
      return;
    }

    Collections.sort(list, new BeanComparator<T>(property, getComponentType(list), isAsc, isNullFisrt));

  }


  /**
   * Equivalent a {@code sort(property, true, true, list)}
   *
   * @param property - Propriete cible du tri
   * @param list     - Liste a trier
   *
   * @see #sort(String, boolean, boolean, List)
   */
  public static <T> void sort(String property, List<T> list) {
    sort(property, true, true, list);
  }


  /**
   *
   * @param c - Collection
   * @return
   */
  public static Class<?> getComponentType(Collection<?> c) {

    Class<?> klass = null;

    for (Object o : c) {
      if (o != null) {
        if (klass == null) {
          klass = o.getClass();
        }
        else if (klass != o.getClass()) {
          klass = getCommonAncestor(klass, o.getClass());
        }
      }
    }

    return klass;

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this klass */
  private CollectionUtil() { }


}