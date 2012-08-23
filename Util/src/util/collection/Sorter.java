package util.collection;


import static util.resources.CollectionKey.SORTER_CHECK_KO;


import util.resource.Logger;


public final class Sorter {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = Logger.getLogger(Sorter.class);

  private static final float SHRINK_FACTOR = (float) 1.3; // comb sort


  // ——————————————————————————————————————————————————————————— Private Methods


  private static final boolean _check(int[] a, int lo, int hi) {

    if (a == null || lo < 0 || !(lo < hi) || !(hi < a.length)) {
      logger.info(SORTER_CHECK_KO);
      return false;
    }
    else {
      return true;
    }

  }


  private static final void _swap(int[] a, int i, int j) {

    int t = a[i];
    a[i] = a[j];
    a[j] = t;

  }


  // Exchange sorts ------------------------------------------------------------


  private static final void _bubbleSort(int[] a, int lo, int hi) {

    int l = hi;
    int i, j, max;

    while (l > lo) {
      max = lo;
      for (i=lo; i < l; i++) {
        j = i + 1;
        if (a[i] > a[j]) { // test la position relative des 2 elements
          _swap(a, i, j);  // permute les valeurs dans le bon ordre
          max = i;           // sauvegarde l'emplacement de la permutation
        }
      }
      l = max; // inutile de parcourir le tableau apres la derniere permutation
    }

  }


  private static final void _cocktailSort(int[] a, int lo, int hi) {

    int l = hi;
    int h = lo;
    int i, j, min, max;

    while (l > h) {
      // forward
      max = h;
      for (i=max; i < l; i++) {
        j = i + 1;
        if (a[i] > a[j]) {
          _swap(a, i, j);
          max = i;
        }
      }
      // backward
      min = l = max;
      for (i=min; i > h; i--) {
        j = i - 1;
        if (a[i] < a[j]) {
          _swap(a, i, j);
          min = i;
        }
      }
      h = min;
    }

  }


  private static final void _combSort11(int[] a, int lo, int hi) {

    int gap = hi - lo + 1;
    int l = hi;
    int i, j, max;

    while ((gap > 1) || (l > lo)) {
      gap = (int) (gap / SHRINK_FACTOR);
      switch (gap) {
        case 0:
          gap = 1;
          break;
        case 9:
        case 10:
          gap = 11; // valeur optimum
        default:
          l = hi - gap + 1;
      }
      max = lo;
      for (i=lo; i < l; i++) {
        j = i + gap;
        if (a[i] > a[j]) {
          _swap(a, i, j);
          max = i;
        }
      }
      l = max;
    }

  }


  private static final void _gnomeSort(int[] a, int lo, int hi) {

    int i = lo;

    while (i <= hi) {
      if ((i > lo) && (a[i-1] > a[i])) {
        _swap(a, i, --i);
      }
      else {
        i++;
      }
    }

  }


  private static final void _quickSort(int[] a, int lo, int hi) {

    // cas particulier d'un intervalle de 2 elements
    if (hi-lo == 1) {
      if (a[lo] > a[hi]) {
        _swap(a, lo, hi);
      }
      return;
    }

    int i = lo;
    int j = hi;
    int x = a[(lo+hi)/2]; // pivot

    while (i < j) {
      while (a[i] < x) i++;
      while (a[j] > x) j--;
      if (i < j) {
        _swap(a, i, j);
        i++; j--;
      }
    }

    // appels recursifs sur les deux sous-ensembles
    if (lo < j) _quickSort(a, lo, j);
    if (hi > i) _quickSort(a, i, hi);

  }


  // Selection sorts -----------------------------------------------------------


  private static final void _selectionSort(int[] a, int lo, int hi) {

    int h = hi;
    int i, max;

    while (lo < h) {
      // on recupere la position du plus grand element de l'intervalle a trier
      max = h;
      for (i=lo; i < h; i++) {
        if (a[i] > a[max]) max = i;
      }
      if (max != h) {
        _swap(a, h, max); // on met le plus grand element a la bonne position...
      }
      h--; // ...et on reduit l'intervalle restant a trier
    }

  }


  private static final void _shakerSort(int[] a, int lo, int hi) {

    int l = lo;
    int h = hi;
    int i, min, max;

    while (l < h) {
      // on recupere les positions des elements extremes
      min = l;
      max = h;
      for (i=l-1; i++ < h;) {
        if (a[i] < a[min]) min = i; // nouveau minimum
        else
        if (a[i] > a[max]) max = i; // nouveau maximum
      }
      // on met le plus petit element au debut de l'intervalle en cours
      if (min != l) {
        _swap(a, l, min);
        if (max == l) max = min; // cas particulier
      }
      // on met le plus grand element a la fin de l'intervalle en cours
      if (max != h) {
        _swap(a, h, max);
      }
      // on reduit l'intervalle
      l++; h--;
    }

  }


  private static final void _heapSort(int[] a, int lo, int hi) {

    int s = hi - lo + 1; // heap size

    // build heap
    for (int n=s/2; n > 0;) {
      _heapify(a, lo, --n, s);
    }

    // sorting
    while (--s > 0) {
      _swap(a, lo, s+lo);
      _heapify(a, lo, 0, s);
    }

  }


  private static final void _heapify(int[] a, int lo, int no, int s) {

    int n = no;
    int i = lo + no, j;
    int t = a[i];

    while ((n = 2*n + 1) < s) {
      j = lo + n;
      if ((n+1 < s) && (a[j] < a[j+1])) {
        n++; j++;
      }
      if (t < a[j]) {
        a[i] = a[j];
        i = j;
      }
      else {
        break;
      }
    }

    a[i] = t;

  }


  // Insertion sorts -----------------------------------------------------------


  private static final void _insertionSort(int[] a, int lo, int hi) {

    int i, j, t;

    for (i=lo; i < hi; i++) {
      j = i + 1;
      t = a[j]; // variable tampon
      while ((j > lo) && (a[j-1] > t)) {
        a[j] = a[--j]; // on decale les elements pour permettre l'insertion
      }
      a[j] = t;
    }

  }


  private static final void _shellSort(int[] a, int lo, int hi) {

    int gap = 1;
    int i, j, t;

    while (3*gap < hi-lo) { gap = 3*gap + 1; }

    while (gap > 0) {
      for (i=lo+gap-1; i++ < hi;) {
        j = i-gap;
        t = a[i];
        while ((j >= lo) && (a[j] > t)) {
          a[j+gap] = a[j];
          j -= gap;
        }
        a[j+gap] = t;
      }
      gap /= 3;
    }

  }


  // Merge sorts ---------------------------------------------------------------


  private static final void _mergeSort(int[] a, int lo, int hi, int[] t) {

    if (lo == hi) {
      return;
    }

    int mid = (lo+hi)/2; // milieu du tableau

    // appels recursifs sur les 2 moities du tableau
    _mergeSort(a, lo, mid, t);
    _mergeSort(a, ++mid, hi, t);

    // fusion des 2 moities du tableau
    int l = lo;
    int h = mid;

    for (int i=lo-1; i++ < hi;) {
      if ((l < mid) && ((h > hi) || (a[l] < a[h]))) {
        t[i] = a[l++];
      }
      else {
        t[i] = a[h++];
      }
    }

    // copie vers le tableau source
    for (int i=lo-1; i++ < hi;) {
      a[i] = t[i];
    }

  }


  private static final void _mergeSortInPlace(int[] a, int lo, int hi) {

    if (lo == hi) {
      return;
    }

    int mid = (lo+hi)/2; // milieu du tableau

    // appels recursifs sur les 2 moities du tableau
    _mergeSortInPlace(a, lo, mid);
    _mergeSortInPlace(a, ++mid, hi);

    // fusion des 2 moities du tableau
    int i = lo;
    int j, t;

    while ((i < mid) && (mid <= hi)) {
      if (a[i] > a[mid]) {
        j = mid;
        t = a[j];
        while (j > i) {
          a[j] = a[--j];
        }
        a[i] = t;
        mid++;
      }
      i++;
    }

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  // Exchange sorts ------------------------------------------------------------


  /**
   * <b>Bubble Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Exchanging</td><td style="background:Green;color:White;"><b>Yes</b></td>
   *     <td>O(n)</td><td>O(n²)</td><td>O(n²)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   */
  public static final boolean bubbleSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _bubbleSort(a, lo, hi);

    return true;

  }


  /**
   * <b>Cocktail Sort</b> [Bidirectional Bubble Sort]
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Exchanging</td><td style="background:Green;color:White;"><b>Yes</b></td>
   *     <td>O(n)</td><td>O(n²)</td><td>O(n²)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   * @see #bubbleSort(int[], int, int)
   */
  public static final boolean cocktailSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _cocktailSort(a, lo, hi);

    return true;

  }


  /**
   * Odd-even sort
   *
   * Odd-even sort is a relatively simple sorting algorithm. It is a comparison
   * sort based on bubble sort with which it shares many characteristics.
   * It functions by comparing all (odd, even)-indexed pairs of adjacent
   * elements in the list and, if a pair is in the wrong order (the first is
   * larger than the second) the elements are switched. The next step repeats
   * this for (even, odd)-indexed pairs (of adjacent elements). Then it
   * alternates between (odd, even) and (even, odd) steps until the list is
   * sorted. It can be thought of as using parallel processors, each using
   * bubblesort but starting at different points in the list (all odd indices
   * for the first step). This sorting algorithm is only marginally more
   * difficult than bubble sort to implement.
   */
  static final boolean oddEvenSort(int[] a, int lo, int hi) {

    return true;

  }


  /**
   * <b>Comb Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Exchanging</td><td style="background:Red;color:White;"><b>No</b></td>
   *     <td>O(n log n)</td><td>O(n log n)</td><td>O(n log n)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   * @see #bubbleSort(int[], int, int)
   */
  public static final boolean combSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _combSort11(a, lo, hi);

    return true;

  }


  /**
   * <b>Gnome Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Exchanging</td><td style="background:Green;color:White;"><b>Yes</b></td>
   *     <td>O(n)</td><td>-</td><td>O(n²)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   */
  public static final boolean gnomeSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _gnomeSort(a, lo, hi);

    return true;

  }


  /**
   * <b>Quick Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Partitioning</td><td style="background:Red;color:White;"><b>No</b></td>
   *     <td>O(n log n)</td><td>O(n log n)</td><td>O(n²)</td><td>O(log n)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   */
  public static final boolean quickSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _quickSort(a, lo, hi);

    return true;

  }


  // Selection sorts -----------------------------------------------------------


  /**
   * <b>Selection Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Selection</td><td style="background:Red;color:White;"><b>No</b></td>
   *     <td>O(n²)</td><td>O(n²)</td><td>O(n²)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   */
  public static final boolean selectionSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _selectionSort(a, lo, hi);

    return true;

  }


  /**
   * <b>Shaker Sort</b> [Bidirectional Selection Sort]
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Selection</td><td style="background:Red;color:White;"><b>No</b></td>
   *     <td>O(n²)</td><td>O(n²)</td><td>O(n²)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   * @see #selectionSort(int[], int, int)
   */
  public static final boolean shakerSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _shakerSort(a, lo, hi);

    return true;

  }


  /**
   * <b>Heap Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Selection</td><td style="background:Red;color:White;"><b>No</b></td>
   *     <td>O(n log n)</td><td>O(n log n)</td><td>O(n log n)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   */
  public static final boolean heapSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _heapSort(a, lo, hi);

    return true;

  }


  /**
   * Smoothsort
   *
   * Smoothsort[1][2] (method) is a comparison-based sorting algorithm. It is a
   * variation of heapsort developed by Edsger Dijkstra in 1981. Like heapsort,
   * smoothsort's upper bound is O(n log n). The advantage of smoothsort is that
   * it comes closer to O(n) time if the input is already sorted to some degree,
   * whereas heapsort averages O(n log n) regardless of the initial sorted state
   */

  /** Cartesian tree */

  /**
   * Tournament sort
   *
   * Tournament sorts's principle is just like conducting a tournament in which
   * two players play with each other.It was initially used for just finding the
   * first two smallest or greatest numbers .It takes just n+lg n - 2 to find
   * the first two smallest or largest numbers. This is infact the most
   * effective method to do this. We compare numbers in pairs .Then form a
   * temporary array with the winning elements based on your priority condition.
   * We repeat this process until we get the greatest or smallest element based
   * on your choice
   */


  // Insertion sorts -----------------------------------------------------------


  /**
   * <b>Insertion Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Insertion</td><td style="background:Green;color:White;"><b>Yes</b></td>
   *     <td>O(n)</td><td>O(n+d)</td><td>O(n²)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   */
  public static final boolean insertionSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _insertionSort(a, lo, hi);

    return true;

  }


  /**
   * <b>Shell Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Insertion</td><td style="background:Red;color:White;"><b>No</b></td>
   *     <td>-</td><td>-</td><td>O(n log² n)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   * @see #insertionSort(int[], int, int)
   */
  public static final boolean shellSort(int[] a, int lo, int hi) {

    if (!_check(a, lo, hi)) return false;

    _shellSort(a, lo, hi);

    return true;

  }


  /**
   * Tree sort
   *
   * A tree sort is a sort algorithm that builds a binary search tree from the
   * keys to be sorted, and then traverses the tree (in-order) so that the keys
   * come out in sorted order. Its typical use is when sorting the elements of a
   * stream from a file. Several other sorts would have to load the elements to
   * a temporary data structure, whereas in a tree sort the act of loading the
   * input into a data structure is sorting it.
   */


  /**
   * Library sort
   *
   * Library sort, or gapped insertion sort is a sorting algorithm that uses an
   * insertion sort, but with gaps in the array to accelerate subsequent
   * insertions
   */


  /**
   * Patience sorting
   *
   * Patience sorting is a sorting algorithm, based on a solitaire card game,
   * that has the property of being able to efficiently compute the length of
   * the longest increasing subsequence in a given array
   */


  // Merge sorts ---------------------------------------------------------------


  /**
   * <b>Merge Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Merging</td><td style="background:Green;color:White;"><b>Yes</b></td>
   *     <td>O(n log n)</td><td>O(n log n)</td><td>O(n log n)</td><td>O(n)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   * @see #mergeSortInPlace(int[], int, int)
   */
  public static final boolean mergeSort(int[] a, int lo, int hi) {

  if (!_check(a, lo, hi)) return false;

  _mergeSort(a, lo, hi, new int[a.length]);

  return true;

  }


  /**
   * <b>In-Place Merge Sort</b>
   * <hr/>
   * <table border="1" cellpadding="5">
   *   <tr>
   *     <th>Method</th><th>Stable</th>
   *     <th>Best</th><th>Average</th><th>Worst</th><th>Memory</th>
   *   </tr>
   *   <tr align="center">
   *     <td>Merging</td><td style="background:Green;color:White;"><b>Yes</b></td>
   *     <td>O(n log n)</td><td>O(n log n)</td><td>O(n log n)</td><td>O(1)</td>
   *   </tr>
   * </table>
   * <br/>
   * @param a  - Array of <code>int</code> to be sorted
   * @param lo - Low index  : index of the first element [inclusive]
   * @param hi - High index : index of the last element [inclusive]
   * @return <code><b>true </b></code> : if a sorting is relevant and performed<br/>
   *         <code><b>false</b></code> : if a sorting is not relevant and not performed
   * @see #mergeSort(int[], int, int)
   */
  public static final boolean mergeSortInPlace(int[] a, int lo, int hi) {

  if (!_check(a, lo, hi)) return false;

  _mergeSortInPlace(a, lo, hi);

  return true;

  }


  /**
   * Strand sort
   *
   * Strand sort is a sorting algorithm. It works by repeatedly pulling sorted
   * sublists out of the list to be sorted and merging them with a result array.
   * Each iteration through the unsorted list pulls out a series of elements
   * which were already sorted, and merges those series together
   */


  // Hybrid --------------------------------------------------------------------


  /**
   * Timsort
   *
   * Timsort is a hybrid sorting algorithm derived from merge sort and insertion
   * sort, designed to perform well on many kinds of real-world data. It was
   * invented by Tim Peters in 2002, for use in versions 2.3 and later of the
   * Python programming language, and may soon be used in Java as well
   */


  /**
   * Introsort
   *
   * Introsort or introspective sort is a sorting algorithm designed by
   * David Musser in 1997. It begins with quicksort and switches to heapsort
   * when the recursion depth exceeds a level based on (the logarithm of) the
   * number of elements being sorted. It is the best of both worlds, with a
   * worst-case O(n log n) runtime and practical performance comparable to
   * quicksort on typical data sets. Since both algorithms it uses are
   * comparison sorts, it too is a comparison sort
   */


  /**
   * <p>
   * Le tri par casiers est une méthode de tri très spéciale car
   * elle ne s'applique qu'au tri de valeurs entières incluses
   * dans un ensemble pas trop grand. Cette contrainte réduit énormément
   * la portée des utilisations de ce tri, mais quand ce tri est implémentable,
   * il est d'une redoutable efficacité. Cela est du au fait que c'est la seule
   * méthode de tri qui ne nécessite aucune comparaison entre
   * les différents éléments.<br><br>
   * Son principe est le suivant :<br>
   * <ul><li>
   * Il faut rechercher dans l'ensemble d'éléments le plus petit élément u
   * et le plus grand élément v.<br><br>
   * </li><li>
   * Dans un deuxième temps, un tableau de taille (v-u+1) dont
   * toutes les valeurs sont initialisés à 0 est construit.<br><br>
   * </li><li>
   * Ensuite, l'ensemble des valeurs à classer est parcourut,
   * et pour chaque valeur x, la case n° (x-u) est incrémentée.<br><br>
   * </li><li>
   * Enfin, le tableau est parcourut et l'ensemble des éléments est recomposé
   * en créant pour chaque case, autant de valeur qu'indiqué par son contenu.
   * </li></ul>
   * </p>
   * @param a  : le tableau d'entiers à trier
   */
  protected static final void triCasier(int[] a) {

    int l = a.length;

    //on parcours le tableau afin d'en récupérer les valeurs minimales et maximales
    int min = a[0];
    int max = a[0];

    for (int i=1; i < l; i++) {
      if (min > a[i]) {
        min = a[i];
      }
      if (max < a[i]) {
        max = a[i];
      }
    }

    //on construit un tableau contenant le nombre d'entiers maxi-mini+1
    int tmpLong = max - min + 1;
    int tmpTableau[] = new int[tmpLong];

    //on initialise le tableau à 0
    for (int i=0; i < tmpLong; i++) {
      tmpTableau[i] = 0;
    }
    //puis on increment le compteur correspondant à chaque entier trouvé
    for (int i=0; i < l; i++) {
      tmpTableau[a[i] - min]++;
    }
    //enfin, on reconstitue le tableau initial classé
    int compt = 0;
    for (int i=0; i < tmpLong; i++) {
      while (tmpTableau[i] > 0) {
        a[compt] = min + i;
        tmpTableau[i]--;
        compt++;
      }
    }

  }


  // ——————————————————————————————————————————————————————————————— Constructor


  /** Don't let anyone instantiate this fucking klass ;o) */
  private Sorter() { }


}