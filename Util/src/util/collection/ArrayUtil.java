package util.collection;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.invokeMethod;


import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.bean.BeanComparator;
import util.misc.StringUtil;


public final class ArrayUtil {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(ArrayUtil.class);


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Controle le contenu de chaque emplacement d'un tableau
   * et pas seulement sa taille.
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si tous les emplacements du tableau sont vides<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  @SafeVarargs
  public static <T> boolean isEmpty(T... array) {

    if (array != null && array.length > 0) {
      for (T o : array) {
        if (o != null) {
          return false;
        }
      }
    }

    return true;

  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(byte[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(short[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(char[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(int[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(long[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(float[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(double[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou {@code null}
   *
   * @param array - Le tableau a controler
   *
   * @return {@code true} : si le tableau est vide ou {@code null}<br/>
   *         {@code false} : si le tableau n'est pas vide
   */
  public static boolean isEmpty(boolean[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Controle la presence d'une valeur dans une liste de valeurs
   *
   * @param value  - La valeur a controler
   * @param values - La liste de valeurs
   *
   * @return {@code true} si la valeur existe dans la liste
   * @see Collection#contains(Object)
   */
  @SafeVarargs
  public static <T> boolean contains(T value, T... values) {

    if (values != null) {
      for (T o : values) {
        // la valeur 'null' est une valeur valide comme une autre !
        if (o == null ? values == null : o.equals(value)) {
          return true;
        }
      }
    }

    return false;

  }


  /**
   * Permute 2 elements d'un tableau
   *
   * @param i - 1er index
   * @param j - 2nd index
   * @param a - Tableau
   */
  public static <T> void swap(int i, int j, T[] a) {
    T t = a[i];
    a[i] = a[j];
    a[j] = t;
  }


  /**
   * Supprime les cellules vides [{@code null}] d'un tableau
   *
   * @param array - Le tableau a traiter
   *
   * @return Un tableau sans element {@code null}
   */
  @SafeVarargs
  public static <T> T[] trim(T... array) {

    if (array == null) {
      return null;
    }

    List<T> list = new ArrayList<T>(Arrays.asList(array));
    list.removeAll(Collections.singleton(null));

    return list.toArray(Arrays.copyOf(array, 0));

  }


  /**
   * Trie un tableau ou une liste de valeurs en fonction d'une propriete de ses elements
   *
   * @param property    - Le nom de la propriete cible du tri
   * @param isAsc       - Le type de tri ascendant / descendant
   * @param isNullFisrt - Les valeurs [{@code null}] au debut / a la fin
   * @param array       - Le tableau ou l'ensemble de valeurs a trier
   *
   * @return Le meme tableau, et non une copie
   */
  @SafeVarargs
  public static <T> T[] sort(String property, boolean isAsc, boolean isNullFisrt, T... array) {

    if (!isEmpty(array) && array.length > 1 && !StringUtil.isEmpty(property)) {
      Comparator<T> c = null;
      try {
        c = new BeanComparator<T>(property, isAsc, isNullFisrt, array.getClass().getComponentType());
      }
      catch (IllegalStateException x) {
        x.printStackTrace(); // TODO: log
      }
      if (c != null) {
        Arrays.sort(array, c);
      }
    }

    return array;

  }


  /**
   * Equivalent a {@code sort(array, property, true, true)}
   *
   * @param property - Le nom de la propriete cible du tri
   * @param array    - Le tableau ou l'ensemble de valeurs a trier
   *
   * @return Le meme tableau, et non une copie
   *
   * @see #sort(T[], String, boolean, boolean)
   */
  @SafeVarargs
  public static <T> T[] sort(String property, T... array) {
    return sort(property, true, true, array);
  }


  /**
   * Regroupement d'un tableau de beans par une de leurs proprietes.<br/><br/>
   * Exemple avec le tableau suivant :<br/>
   * <code>[{"Jean","DURAND"},{"Paul","DURAND"},{"Jean","DUPOND"},{"Paul","DUPOND"}]</code><br/><br/>
   * Un regroupement par la propriete "nom" donnera la Map :<br/>
   * <code>["DURAND":[{"Jean","DURAND"},{"Paul","DURAND"}];"DUPOND":[{"Jean","DUPOND"},{"Paul","DUPOND"}]]</code><br/><br/>
   * Un regroupement par la propriete "prenom" donnera la Map :<br/>
   * <code>["Jean":[{"Jean","DURAND"},{"Jean","DUPOND"}];"Paul":[{"Paul","DURAND"},{"Paul","DUPOND"}]]</code><br/>
   *
   * @param property - Le nom de la propriete qui sert de critere de regroupement
   * @param array    - Le tableau de donnees
   *
   * @return Une map de la forme [key:property;value:T[]]
   */
  @SafeVarargs
  public static <T> Map<Object,T[]> groupBy(String property, T... array) {

    Map<Object,T[]> mapTab = new HashMap<Object,T[]>();

    if (!StringUtil.isEmpty(property) && !isEmpty(array)) {
      Map<Object,List<T>> mapList = new HashMap<Object,List<T>>();
      Method method = getGetter(property, array.getClass().getComponentType());
      if (method != null) {
        for (T o : array) {
          if (o != null) {
            Object key = invokeMethod(o, method);
            if (!mapList.containsKey(key)) {
              mapList.put(key, new ArrayList<T>()); // liste temporaire
            }
            mapList.get(key).add(o); // ajout du bean au groupe
          }
        }
      }
      // substitution de la liste temporaire par son tableau equivalent
      for (Map.Entry<Object, List<T>> entry : mapList.entrySet()) {
        mapTab.put(entry.getKey(), entry.getValue().toArray(Arrays.copyOf(array, 0)));
      }
    }

    return mapTab;

  }


  /**
   * Collecte la valeur d'une propriete de chaque element d'un tableau
   *
   * @param property - Le nom de la propriete a collecter
   * @param klass    - Le type de la propriete a collecter
   * @param array    - Le tableau d'elements
   *
   * @return La valeur de la propriete de chaque element
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T,U> U[] collect(String property, Class<U> klass, T... array) {

    U[] values = null;

    if (array != null && !StringUtil.isEmpty(property)) {
      values = (U[]) Array.newInstance(klass, array.length);
      Method method = getGetter(property, array.getClass().getComponentType());
      if (method != null) {
        for (int i=0; i < array.length; i++) {
          values[i] = (U) ((array[i] != null) ? invokeMethod(array[i], method) : null);
        }
      }
    }

    return values;

  }


  /**
   * Equivalent a {@code collect(property, Object.class, array)}
   *
   * @param property - Le nom de la propriete a collecter
   * @param array    - Le tableau d'elements
   *
   * @return La valeur de la propriete de chaque element
   *
   * @see #collect(String, Class, Object...)
   */
  @SafeVarargs
  public static <T> Object[] collect(String property, T... array) {
    return collect(property, Object.class, array);
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private ArrayUtil() { }


}