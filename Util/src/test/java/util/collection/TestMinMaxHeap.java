package util.collection;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestMinMaxHeap {


  // —————————————————————————————————————————————————————————— Static Constants


  // —————————————————————————————————————————————————————————— Static Variables



  // ———————————————————————————————————————————————————————— Instance Variables




  // ——————————————————————————————————————————————————————————— Private Methods




  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() throws Exception {

  }


  @After
  public void after() throws Exception {

  }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testRemove() {

    MinMaxHeap<Double> heap = new MinMaxHeap<Double>(10);

    for (int i=0; i < 8; i++) {
      heap.add(Math.random());
    }

    System.out.printf("Capacity : %d %n", heap.getCapacity());
    System.out.printf("Size     : %d %n", heap.getSize());

    while (!heap.isEmpty()) {
      System.out.printf("min = %1$.18f | max = %2$.18f %n", heap.removeMin(), heap.removeMax());
    }

  }


}