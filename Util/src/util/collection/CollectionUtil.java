package util.collection;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.instantiate;
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


  // ——————————————————————————————————————————————————————————— Private Methods


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
  public static <T extends IFilter<? super T>> void filter(Collection<T> c) {

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
  public static <T extends IFilter<? super T>> void filter(Collection<T> src, Collection<T> trg) {

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
   * <p>Regroupement les elements d'une collection par une de leurs proprietes.<p>
   * Exemple avec la liste suivante :<br/>
   * <code>[{"Jean","DURAND"},{"Paul","DURAND"},{"Jean","DUPOND"},{"Paul","DUPOND"}]</code><br/><br/>
   * Un regroupement par la propriete "nom" donnera la Map :<br/>
   * <code>["DURAND":[{"Jean","DURAND"},{"Paul","DURAND"}];"DUPOND":[{"Jean","DUPOND"},{"Paul","DUPOND"}]]</code><br/><br/>
   * Un regroupement par la propriete "prenom" donnera la Map :<br/>
   * <code>["Jean":[{"Jean","DURAND"},{"Jean","DUPOND"}];"Paul":[{"Paul","DURAND"},{"Paul","DUPOND"}]]</code><br/>
   *
   * @param c        - La collection source
   * @param property - Le nom de la propriete qui sert de critere de regroupement
   *
   * @return Une map de la forme [key:property;value:List]
   */
  public static <T, C extends Collection<T>> Map<Object,C> groupBy(C c, String property) {

    if (c == null) {
      return null;
    }

    Map<Object,C> map = new HashMap<Object,C>();

    if (!StringUtil.isEmpty(property)) {
      // TODO: [WARNING] if (1st element == null) => NullPointerException
      Method method = getGetter(c.iterator().next().getClass(), property);
      if (method != null) {
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
      }
    }

    return map;

  }


  /**
   * Collecte la valeur d'une propriete de chaque element d'une collection
   *
   * @param c        - La collection source
   * @param property - Le nom de la propriete a collecter
   *
   * @return La valeur de la propriete de chaque element
   */
  public static <C1 extends Collection<?>, C2 extends Collection<Object>> C2 collect(C1 c, String property) {

    if (c == null) {
      return null;
    }

    @SuppressWarnings("unchecked")
    C2 values = (C2) instantiate(c);

    if (!StringUtil.isEmpty(property)) {
      // TODO: [WARNING] if (1st element == null) => NullPointerException
      Method method = getGetter(c.iterator().next().getClass(), property);
      if (method != null) {
        for (Object o : c) {
          Object value = (o != null) ? invokeMethod(o, method) : null;
          values.add(value);
        }
      }
    }

    return values;

  }


//  /**
//   * Trie une liste en fonction d'une propriete de ses objets
//   *
//   * @param list     - Liste a trier
//   * @param property - Propriete cible du tri
//   * @param isAsc    - Tri ascendant / descendant
//   */
//  @SuppressWarnings("unchecked")
//  public static <T> void sort(List<T> list, String property, boolean isAsc) {
//
//    if (!isEmpty(list) && list.size() > 1) {
//      BeanComparator comp;
//      if (isAsc) {
//        comp = new BeanComparator(property);
//      }
//      else {
//        comp = new BeanComparator(property, Collections.reverseOrder());
//      }
//      try {
//        List<T> exclude = new ArrayList<T>();
//        List<T> include = new ArrayList<T>();
//        // exclure du tri les objets et les proprietes 'null'
//        Iterator<T> it = list.iterator();
//        while (it.hasNext()) {
//          T o = it.next();
//          Object value = (o != null) ? PropertyUtils.getProperty(o, property) : null;
//          if (value != null) {
//            include.add(o);
//          }
//          else {
//            exclude.add(o);
//          }
//        }
//        Collections.sort(include, comp);
//        try {
//          list.clear(); // WARN: risque d'exception UnsupportedOperationException
//          list.addAll(include); // elements tries au debut de la liste
//          list.addAll(exclude); // elements non tries a la fin de la liste
//        }
//        catch (UnsupportedOperationException x) {
//          // s'il s'agit d'une liste de taille fixe, la methode clear() est inutilisable
//          // il faut donc assigner les elements un a un
//          int i = 0;
//          for (T o : include) {
//            list.set(i++, o);
//          }
//          for (T o : exclude) {
//            list.set(i++, o);
//          }
//        }
//      }
//      catch (Exception x) {
//        x.printStackTrace();
//      }
//    }
//
//  }
//
//
//  /**
//   * Equivalent a {@code sort(list, property, true)}
//   *
//   * @param list    - Liste a trier
//   * @param property - Propriete cible du tri
//   * @see #sort(List, String, boolean)
//   */
//  public static <T> void sort(List<T> list, String property) {
//    sort(list, property, true);
//  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private CollectionUtil() { }


}