package util.collection;


/**
 * Double Ended Priority Queue implementation based on Min-Max Heap
 *
 * The min-max heap is an extension of the binary heap that has both a min order
 * and max order thus supporting both removeMin( ) and removeMax( ). Min order of
 * the heap is maintained on even levels of the heap, the root being at level 0,
 * and max order is maintained on odd levels of the heap.  Both orders are
 * intermeshed such that no child of a min level is ever less than the parent and
 * no child of a max level is ever greater than the parent.
 *
 * @author Todd Wease (C++ version)
 * @author Genc Doko (Java version)
 * @author Michel BAILLEUL (Generic version)
 */
public class MinMaxHeap<T extends Comparable<T>> {


  // —————————————————————————————————————————————————————————————— Constructors


  /** Constructs a min-max heap with a specified capacity */
  @SuppressWarnings("unchecked")
  public MinMaxHeap(int capacity) {
    array = (T[]) new Comparable[capacity + 1];
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  /** Current number of items in min-max heap */
  private int size = 0;

  /** Array-representation of min-max heap */
  private T[] array;


  // ——————————————————————————————————————————————————————————— Private Methods


  /**
   * Helper method for swapping items in the array
   * @param a - 1st index
   * @param b - 2nd index
   */
  private void _swap(int a, int b) {
    T tmp = array[a];
    array[a] = array[b];
    array[b] = tmp;
  }


  /**
   * Used to maintain min-max heap order after insertion. Determines whether
   * current heap level is a min level or a max level and calls percolateUpMin( )
   * or percolateUpMax( ).
   *
   * @param hole index in array where item was inserted
   */
  private void _percolateUp(int hole) {

    int parent = hole / 2;
    int level = 0;

    for (int i=2; i <= hole; i *= 2) {
      level++;
    }

    // Min level
    if (level % 2 == 0) {
      if (parent > 0 && array[hole].compareTo(array[parent]) > 0) {
        _swap(hole, parent);
        _percolateUpMax(parent);
      }
      else {
        _percolateUpMin(hole);
      }
    }
    // Max Level
    else {
      if (parent > 0 && array[hole].compareTo(array[parent]) < 0) {
        _swap(hole, parent);
        _percolateUpMin(parent);
      }
      else {
        _percolateUpMax(hole);
      }
    }

  }


  /**
   * Called by percolateUp to maintain min-max heap order on the min levels.
   *
   * @param hole index in array of item to be percolated
   */
  private void _percolateUpMin(int hole) {

    int grandparent = hole / 4;

    if (grandparent > 0 && array[hole].compareTo(array[grandparent]) < 0) {
      _swap(hole, grandparent);
      _percolateUpMin(grandparent);
    }

  }


  /**
   * Called by percolateUp to maintain min-max heap order on the max levels.
   *
   * @param hole index in array of item to be percolated
   */
  private void _percolateUpMax(int hole) {

    int grandparent = hole / 4;

    if (grandparent > 0 && array[hole].compareTo(array[grandparent]) > 0) {
      _swap(hole, grandparent);
      _percolateUpMax(grandparent);
    }

  }


  /**
   * Used to maintain min-max heap order after deletion. Determines whether
   * current heap level is a min level or a max level and calls percolateDownMin( )
   * or percolateDownMax( ).
   *
   * @param hole index in array where item was deleted
   */
  private void _percolateDown(int hole) {

    int level = 0;

    for (int i=2; i <= hole; i *= 2) {
      level++;
    }

    if (level % 2 == 0) {
      _percolateDownMin(hole);
    }
    else {
      _percolateDownMax(hole);
    }

  }


  /**
   * Called by percolateDown to maintain min-max heap order on the min levels.
   *
   * @param hole index in array of item to be percolated
   */
  private void _percolateDownMin(int hole) {

    // find minimum value of children and grandchildren
    // hole * 2 = index of first child if it exists
    // hole * 4 = index of first grandchild if it exists
    int minIndex = _findMinDescendant(hole * 2, hole * 4 );

    // at least one descendant
    if (minIndex > 0) {
      // min descendant is a grandchild
      if (minIndex >= hole * 4) {
        // if less than grandparent, i.e value at hole, swap
        if (array[minIndex].compareTo(array[hole]) < 0) {
          _swap(hole, minIndex);
          // if greater than parent, swap
          if (array[minIndex].compareTo(array[minIndex / 2]) > 0 ) {
            _swap(minIndex, minIndex / 2);
          }
          _percolateDownMin(minIndex);
        }
      }
      // if less than parent, i.e value at hole, swap
      else if (array[minIndex].compareTo(array[hole]) < 0) {
        _swap(hole, minIndex);
      }
    }

  }


  /**
   * Called by percolateDown to maintain min-max heap order on the max levels.
   *
   * @param hole index in array of item to be percolated
   */
  private void _percolateDownMax(int hole) {

    // find maximum value of children and grandchildren
    // hole * 2 = index of first child if it exists
    // hole * 4 = index of first grandchild if it exists
    int maxIndex = _findMaxDescendant(hole * 2, hole * 4);

    // at least one descendant
    if (maxIndex > 0) {
      // max descendant is a grandchild
      if (maxIndex >= hole * 4) {
        // if greater than grandparent, i.e value at hole, swap
        if (array[maxIndex].compareTo(array[hole]) > 0) {
          _swap(hole, maxIndex);
          // if less than parent, swap
          if (array[maxIndex].compareTo(array[maxIndex / 2]) < 0) {
            _swap(maxIndex, maxIndex / 2);
          }
          _percolateDownMax(maxIndex);
        }
      }
      // if greater than parent, i.e value at hole, swap
      else if (array[maxIndex].compareTo(array[hole]) > 0) {
        _swap(hole, maxIndex);
      }
    }

  }


  /**
   * Helper method of percolateDownMin( ) that finds the min of the children
   * and grandchildren of the item to be percolated down.
   *
   * @param child array index of first child
   * @param grandchild array index of first grandchild
   */
  private int _findMinDescendant(int child, int grandchild) {

    int minIndex = 0;
    int minChild = child;
    int minGrandchild = grandchild;

    if (child <= size) {
      if (child != size && array[child + 1].compareTo(array[minChild]) < 0) {
        minChild = child + 1;
      }

      minIndex = minChild;

      if (grandchild <= size) {
        for (int i=1; grandchild < size && i < 4; i++, grandchild++) {
          if (array[grandchild + 1].compareTo(array[minGrandchild]) < 0) {
            minGrandchild = grandchild + 1;
          }
        }

        if (array[minGrandchild].compareTo(array[minChild]) < 0) {
          minIndex = minGrandchild;
        }
      }
    }

    return minIndex;

  }


  /**
   * Helper method of percolateDownMax that finds the max of the children and
   * grandchildren of the item to be percolated down.
   *
   * @param child array index of first child
   * @param grandchild array index of first grandchild
   */
  private int _findMaxDescendant(int child, int grandchild) {

    int maxIndex = 0;
    int maxChild = child;
    int maxGrandchild = grandchild;

    if (child <= size) {
      if (child != size && array[child + 1].compareTo(array[maxChild]) > 0) {
        maxChild = child + 1;
      }

      maxIndex = maxChild;

      if (grandchild <= size) {
        for (int i=1; grandchild < size && i < 4; i++, grandchild++) {
          if (array[grandchild + 1].compareTo(array[maxGrandchild]) > 0) {
            maxGrandchild = grandchild + 1;
          }
        }

        if (array[maxGrandchild].compareTo(array[maxChild]) > 0) {
          maxIndex = maxGrandchild;
        }
      }
    }

    return maxIndex;

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public int getSize() {
    return size;
  }


  public int getCapacity() {
    return array.length - 1;
  }


  /** Checks if the min-max heap is empty */
  public boolean isEmpty() {
    return size == 0;
  }


  /** Checks if the min-max heap is full */
  public boolean isFull() {
    return size == array.length - 1;
  }


  /** Returns the minimum item in the min-max heap */
  public T min() {

    if (isEmpty()) {
      return null;
    }

    return array[1];

  }


  /** Returns the maximum item in the min-max heap */
  public T max() {

    if (isEmpty()) {
      return null;
    }

    if (size == 1) {
      return array[1];
    }
    else if (size == 2) {
      return array[2];
    }
    else {
      return (array[2].compareTo(array[3]) > 0) ? array[2] : array[3];
    }

  }


  /** Adds a new item to the min-max heap, while keep the heap order. */
  public void add(T item) {

    if (isFull()) {
      throw new RuntimeException("Heap is full");
    }

    int hole = ++size;
    array[hole] = item;

    _percolateUp(hole);

  }


  /** Returns and removes the minimum item from min-max heap */
  public T removeMin() {

    if (isEmpty()) {
      return null;
    }

    T min = array[1];
    array[1] = array[size--];
    _percolateDown(1);

    return min;

  }


  /** Returns and removes the maximum item from min-max heap */
  public T removeMax() {

    if (isEmpty()) {
      return null;
    }

    int maxIndex;

    if (size == 1) {
      maxIndex = 1;
    }
    else if (size == 2) {
      maxIndex = 2;
    }
    else {
      maxIndex = (array[2].compareTo(array[3]) > 0) ? 2 : 3;
    }

    T max = array[maxIndex];
    array[maxIndex] = array[size--];
    _percolateDown(maxIndex);

    return max;

  }


}