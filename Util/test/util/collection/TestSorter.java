package util.collection;


import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.collection.Sorter;



public class TestSorter {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final String BUBBLE    = "Bubble Sort";

  private static final String COCKTAIL  = "Cocktail Sort";

  private static final String COMB      = "Comb Sort";

  private static final String GNOME     = "Gnome Sort";

  private static final String QUICK     = "Quick Sort";

  private static final String SELECTION = "Selection Sort";

  private static final String SHAKER    = "Shaker Sort";

  private static final String HEAP      = "Heap Sort";

  private static final String INSERTION = "Insertion Sort";

  private static final String SHELL     = "Shell Sort";

  private static final String MERGE     = "Merge Sort";

  private static final String MERGE_IP  = "In-Place Merge Sort";


  private static final List<String> FAST = Arrays.asList(COMB, QUICK, HEAP, SHELL, MERGE);

//  private static final List<String> SLOW = Arrays.asList(BUBBLE, COCKTAIL, GNOME, SELECTION, SHAKER, INSERTION, MERGE_IP);


  // —————————————————————————————————————————————————————————— Static Variables


  private static int[] EXPECTED = { 13, 16, 19, 19, 21, 26, 31, 32, 65, 68 };

  private static int[] SMALL = { 21, 32, 19, 13, 16, 65, 31, 19, 68, 26 };

  private static int[] LARGE;


  // ———————————————————————————————————————————————————————— Instance Variables


//  private boolean isSmall = true;
//  private boolean isFast  = false;

  private boolean isSmall = false;
  private boolean isFast  = true;

  private String sortType;
  private long before, after;
  private int length = 8*1024*1024; // large array 32 Mo
  private int start = 0;
  private int end = length - 1;
  private int[] a;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _smallArray() {

    length = SMALL.length;

    a = new int[length];
    System.arraycopy(SMALL, 0, a, 0, length);

    start = 0;
    end = SMALL.length - 1;

    // range [1, 7]
//    EXPECTED = new int[] { 21, 13, 16, 19, 19, 31, 32, 65, 68, 26 };
//    start = 1;
//    end = SMALL.length - 2;
/*
    List<Integer> list = new ArrayList<Integer>(EXPECTED.length);
    for (int i=0; i < EXPECTED.length; i++) {
      list.add(EXPECTED[i]);
    }
    Collections.shuffle(list);
    Integer[] tmp = list.toArray(new Integer[EXPECTED.length]);
    a = new int[EXPECTED.length];
    for (int i=0; i < EXPECTED.length; i++) {
      a[i] = tmp[i];
    }
*/
  }


  private void _largeArray() {

    if (LARGE == null) {
      LARGE = new int[length];
      final Random random = new Random(before);
      before = System.currentTimeMillis();

      for (int i=0; i < length; i++) {
        LARGE[i] = random.nextInt();
      }
      after = System.currentTimeMillis();
      System.out.format("Loading data : %d ms\n", after - before);

      if (length < 1024*1024) {
        EXPECTED = new int[length];
        System.arraycopy(LARGE, 0, EXPECTED, 0, length);
        before = System.currentTimeMillis();
        Arrays.sort(EXPECTED);
        after = System.currentTimeMillis();
        System.out.format("Sorting data : %d ms\n", after - before);
      }
    }

    a = new int[length];
    System.arraycopy(LARGE, 0, a, 0, length);

  }


  private boolean _check(String sortType) {
    this.sortType = sortType;
    return !isFast || FAST.contains(sortType);
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() throws Exception {

    if (isSmall) {
      _smallArray();
    }
    else {
      _largeArray();
    }

    before = System.currentTimeMillis();

  }


  @After
  public void after() throws Exception {

    if (_check(sortType)) {
      after = System.currentTimeMillis();

      System.out.println();

      if (isSmall) {
        System.out.println(Arrays.toString(SMALL));
        System.out.println(Arrays.toString(a));
      }

      System.out.format("%1$-38s : %2$5d ms\n", sortType, after - before);

      if (isSmall) {
        assertArrayEquals(EXPECTED, a);
        System.out.println("Sorted");
      }
    }

  }


  // —————————————————————————————————————————————————————————————— Test Methods


  // Exchange sorts ------------------------------------------------------------


  @Test
  public void testBubbleSort() {

    if (_check(BUBBLE)) {
      Sorter.bubbleSort(a, start, end);
    }

  }


  @Test
  public void testCocktailSort() {

    if (_check(COCKTAIL)) {
      Sorter.cocktailSort(a, start, end);
    }

  }


  @Test
  public void testCombSort() {

    if (_check(COMB)) {
      Sorter.combSort(a, start, end);
    }

  }


  @Test
  public void testGnomeSort() {

    if (_check(GNOME)) {
      Sorter.gnomeSort(a, start, end);
    }

  }


  @Test
  public void testQuickSort() {

    if (_check(QUICK)) {
      Sorter.quickSort(a, start, end);
    }

  }


  // Selection sorts -----------------------------------------------------------


  @Test
  public void testSelectionSort() {

    if (_check(SELECTION)) {
      Sorter.selectionSort(a, start, end);
    }

  }


  @Test
  public void testShakerSort() {

    if (_check(SHAKER)) {
      Sorter.shakerSort(a, start, end);
    }

  }


  @Test
  public void testHeapSort() {

    if (_check(HEAP)) {
      Sorter.heapSort(a, start, end);
    }

  }


  // Insertion sorts -----------------------------------------------------------


  @Test
  public void testInsertionSort() {

    if (_check(INSERTION)) {
      Sorter.insertionSort(a, start, end);
    }

  }


  @Test
  public void testShellSort() {

    if (_check(SHELL)) {
      Sorter.shellSort(a, start, end);
    }

  }


  // Merge sorts ---------------------------------------------------------------


  @Test
  public void testMergeSort() {

    if (_check(MERGE)) {
      Sorter.mergeSort(a, start, end);
    }

  }


  @Test
  public void testMergeSortInPlace() {

    if (_check(MERGE_IP)) {
      Sorter.mergeSortInPlace(a, start, end);
    }

  }


}