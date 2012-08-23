package util.collection;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;


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
   * Permuter 2 objets d'un tableau
   *
   * @param a - Tableau
   * @param i - 1er index
   * @param j - 2nd index
   */
  public static void swap(Object a[], int i, int j) {
    Object t = a[i];
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
  public static void sort(Object[] array, String property, boolean isAsc, boolean isNullFisrt) {

    if (!isEmpty(array) && array.length > 1) {
      // traitement des valeurs 'null'
      int j = isNullFisrt ? 0 : array.length;
      try {
        for (int i=0; i < array.length; i++) {
          Object o = array[i];
          Object value = (o != null) ? PropertyUtils.getProperty(o, property) : null;
          if (value == null) {
            swap(array, i, isNullFisrt ? j++ : --j);
            if (!isNullFisrt) {
              if (i < j) {
                i--; // il faut rester a la meme position pour traiter le nouvel element
              }
              else {
                break; // ou sortir si tous les elements ont ete traites
              }
            }
          }
        }
      }
      catch (ReflectiveOperationException x) {
        x.printStackTrace();
      }
      // sens du tri
      @SuppressWarnings("unchecked")
      Comparator<Object> c = (isAsc) ? new BeanComparator(property) :
                                       new BeanComparator(property, Collections.reverseOrder());
      // s'il y a des valeurs 'null', elles sont ecartees du tri
      if (isNullFisrt) {
        Arrays.sort(array, j, array.length, c);
      }
      else {
        Arrays.sort(array, 0, j, c);
      }
    }

  }


  /**
   * Equivalent a {@code sort(array, property, true, true)}
   *
   * @param array    - Tableau a trier
   * @param property - Propriete cible du tri
   *
   * @see #sort(Object[], String, boolean, boolean)
   */
  public static void sort(Object[] array, String property) {
    sort(array, property, true, true);
  }


  /**
   * Regroupement d'un tableau de beans par une de leurs proprietes.<br/>
   * Exemple avec le tableau suivant :<br/>
   * <code>[{"Jean","DURAND"},{"Paul","DURAND"},{"Jean","DUPOND"},{"Paul","DUPOND"}]</code><br/>
   * Un regroupement par la propriete "nom" donnera la Map :<br/>
   * <code>["DURAND":[{"Jean","DURAND"},{"Paul","DURAND"}];"DUPOND":[{"Jean","DUPOND"},{"Paul","DUPOND"}]]</code><br/>
   * Un regroupement par la propriete "prenom" donnera la Map :<br/>
   * <code>["Jean":[{"Jean","DURAND"},{"Jean","DUPOND"}];"Paul":[{"Paul","DURAND"},{"Paul","DUPOND"}]]</code><br/>
   *
   * @param array    - Le tableau de donnees
   * @param property - Le nom de la propriete qui sert de critere de regroupement
   * @return Une map de la forme [key:property;value:Object[]]
   */
  public static <T> Map<Object,T[]> groupBy(T[] array, String property) {

    Map<Object,T[]> mapTab = new HashMap<Object,T[]>();

    if (!isEmpty(array)) {
      Map<Object,List<T>> mapList = new HashMap<Object,List<T>>();
      try {
        for (int i=0; i < array.length; i++) {
          T o = array[i];
          if (o != null) {
            Object key = PropertyUtils.getProperty(o, property);
            if (!mapList.containsKey(key)) {
              mapList.put(key, new ArrayList<T>()); // liste temporaire
            }
            mapList.get(key).add(o); // ajout du bean au groupe
          }
        }
      }
      catch (ReflectiveOperationException x) {
        x.printStackTrace();
      }
      // substitution de la liste temporaire par son tableau equivalent
      Iterator<Object> it = mapList.keySet().iterator();
      while (it.hasNext()) {
        Object key = it.next();
        List<T> list = mapList.get(key);
        mapTab.put(key, list.toArray(Arrays.copyOf(array, 0)));
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


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private ArrayUtil() { }


}