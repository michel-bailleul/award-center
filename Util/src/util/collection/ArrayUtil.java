package util.collection;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.invokeMethod;


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
   * Verifie le contenu de chaque emplacement d'un tableau
   * et pas seulement sa taille.
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si tous les emplacements du tableau sont vides<br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(Object[] array) {

    if (array != null && array.length > 0) {
      for (Object o : array) {
        if (o != null) {
          return false;
        }
      }
    }

    return true;

  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(byte[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(short[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(char[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(int[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(long[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(float[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(double[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Verifie si le tableau est vide ou <code>null</code>
   *
   * @param array - Le tableau a verifier
   * @return <b>true</b> si le tableau est vide ou <code>null</code><br/>
   *         <b>false</b> si le tableau n'est pas vide
   */
  public static boolean isEmpty(boolean[] array) {
    return array == null || array.length == 0;
  }


  /**
   * Permute 2 elements d'un tableau
   *
   * @param a - Tableau
   * @param i - 1er index
   * @param j - 2nd index
   */
  public static <T> void swap(T[] a, int i, int j) {
    T t = a[i];
    a[i] = a[j];
    a[j] = t;
  }


  /**
   * Trie un tableau en fonction d'une propriete de ses elements
   *
   * @param array       - Tableau a trier
   * @param property    - Propriete cible du tri
   * @param isAsc       - Tri ascendant / descendant
   * @param isNullFisrt - Valeurs [{@code null}] au debut / a la fin
   */
  public static <T> void sort(T[] array, String property, boolean isAsc, boolean isNullFisrt) {

    if (!isEmpty(array) && array.length > 1 && !StringUtil.isEmpty(property)) {
      Comparator<T> c = null;
      try {
        c = new BeanComparator<T>(array.getClass().getComponentType(), property, isAsc, isNullFisrt);
      }
      catch (IllegalStateException x) {
        x.printStackTrace(); // TODO: log
      }
      if (c != null) {
        Arrays.sort(array, 0, array.length, c);
      }
    }

  }


  /**
   * Equivalent a {@code sort(array, property, true, true)}
   *
   * @param array    - Tableau a trier
   * @param property - Propriete cible du tri
   *
   * @see #sort(T[], String, boolean, boolean)
   */
  public static <T> void sort(T[] array, String property) {
    sort(array, property, true, true);
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
   * @param array    - Le tableau de donnees
   * @param property - Le nom de la propriete qui sert de critere de regroupement
   *
   * @return Une map de la forme [key:property;value:T[]]
   */
  public static <T> Map<Object,T[]> groupBy(T[] array, String property) {

    Map<Object,T[]> mapTab = new HashMap<Object,T[]>();

    if (!StringUtil.isEmpty(property) && !isEmpty(array)) {
      Map<Object,List<T>> mapList = new HashMap<Object,List<T>>();
      Method method = getGetter(array.getClass().getComponentType(), property);
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
   * Supprime les cellules vides [{@code null}] d'un tableau
   *
   * @param array - Le tableau a traiter
   * @return Un tableau sans element {@code null}
   */
  public static <T> T[] trim(T[] array) {

    if (array == null) {
      return null;
    }

    List<T> list = new ArrayList<T>(Arrays.asList(array));
    list.removeAll(Collections.singleton(null));

    return list.toArray(Arrays.copyOf(array, 0));

  }


  /**
   * Collecte la valeur d'une propriete de chaque element d'un tableau
   *
   * @param array    - Tableau d'elements
   * @param property - Propriete a collecter
   *
   * @return La valeur de la propriete de chaque element
   */
  public static <T> Object[] collect(T[] array, String property) {

    Object[] values = null;

    /* TODO
    if (array != null && !StringUtil.isEmpty(property)) {
      values = new Object[array.length];
      for (int i=0; i < array.length; i++) {
        try {
          Object value = (array[i] != null) ? PropertyUtils.getProperty(array[i], property) : null;
          values[i] = value;
        }
        catch (Exception x) {
          x.printStackTrace();
        }
      }
    }
     */

    return values;

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private ArrayUtil() { }


}