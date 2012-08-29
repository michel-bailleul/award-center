package util.collection;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.collection.MinMaxHeap;


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

    for (int i=1; i <= 10; i++) {
      heap.add(Math.random());
    }

    System.out.printf("Capacity : %d %n", heap.getCapacity());
    System.out.printf("Size     : %d %n", heap.getSize());

    while (!heap.isEmpty()) {
      System.out.printf("min = %1$.18f | max = %2$.18f %n", heap.removeMin(), heap.removeMax());
    }

  }


}