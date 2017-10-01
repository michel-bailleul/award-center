package util.collection;


import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.collection.ArrayUtil;


public class TestArrayUtil {


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void setUp() throws Exception {
  }


  @After
  public void tearDown() throws Exception {
  }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void isEmptyObject() {

    Object[] array = null;
    assertTrue(ArrayUtil.isEmpty(array));

    array = new Object[0];
    assertTrue(ArrayUtil.isEmpty(array));

    array = new Object[1];
    assertTrue(ArrayUtil.isEmpty(array));

    array = new Object[] { };
    assertTrue(ArrayUtil.isEmpty(array));

    array = new Object[] { null };
    assertTrue(ArrayUtil.isEmpty(array));

    array = new Object[] { new Object() };
    assertFalse(ArrayUtil.isEmpty(array));

  }


  @Test
  public void sort() {

    Product[] array = new Product[12];

    int i = 0;
    array[i++] = new Product("k", "Red", 0);
    array[i++] = new Product("j", null, 0);
    array[i++] = new Product("c", "Orange", 0);
    array[i++] = new Product("a", "Blue", 0);
    array[i++] = new Product("e", "Red", 0);
    array[i++] = new Product("g", "Red", 0);
    array[i++] = new Product("h", "Yellow", 0);
    array[i++] = new Product("f", "Pink", 0);
    array[i++] = new Product("b", null, 0);
    array[i++] = new Product("d", "Green", 0);
    array[i++] = new Product("i", "Red", 0);

    ArrayUtil.sort("name", true, true, array);
    System.out.println(Arrays.toString(array));
    ArrayUtil.sort("color", true, true, array);
    System.out.println(Arrays.toString(array));
    ArrayUtil.sort("color", false, true, array);
    System.out.println(Arrays.toString(array));

  }


  @Test
  public void groupBy() {

    Product[] array = new Product[8];

    array[0] = new Product("Car", "RED", 20000);
    array[1] = new Product("Boat", null, 500000);
    array[2] = new Product("Bike", "BLUE", 200);
    array[3] = new Product("Plane", "RED", 1000000);
    array[4] = new Product("Book", "", 0);
    array[5] = new Product("Flower", "BLUE", 0);
    array[6] = new Product("Computer", null, 0);
    array[7] = new Product("Object", "", 0);

    System.out.println(Arrays.toString(array));
    Map<Object, Product[]> map = ArrayUtil.groupBy("color", array);
    for (Map.Entry<Object, Product[]> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " : " + Arrays.toString(entry.getValue()));
    }

  }


  @Test
  public void collect() {

    Product[] array = new Product[8];

    array[0] = new Product("g", "Red", 0);
    array[1] = new Product("e", null, 0);
    array[2] = new Product("c", "Orange", 0);
    array[3] = new Product("a", "Blue", 0);
    array[4] = new Product("h", "Yellow", 0);
    array[5] = new Product("f", "Pink", 0);
    array[6] = new Product("b", null, 0);
    array[7] = new Product("d", "Green", 0);

    System.out.println(Arrays.toString(array));
    String[] values = ArrayUtil.collect("color", String.class, array);
    System.out.println(Arrays.toString(values));

  }


  @Test
  public void trim() {

    Object[] array = { null, "A", null, "B", "C", null, null, "D" };
    Object[] ref   = { "A", "B", "C", "D" };

    System.out.println(array.length + " : " + Arrays.toString(array));
    array = ArrayUtil.trim(array);
    System.out.println(array.length + " : " + Arrays.toString(array));
    assertArrayEquals(ref, array);

    array = new Object[0];

    System.out.println(array.length + " : " + Arrays.toString(array));
    array = ArrayUtil.trim(array);
    System.out.println(array.length + " : " + Arrays.toString(array));
    assertArrayEquals(new Object[0], array);

    array = new Object[] { null };

    System.out.println(array.length + " : " + Arrays.toString(array));
    array = ArrayUtil.trim(array);
    System.out.println(array.length + " : " + Arrays.toString(array));
    assertArrayEquals(new Object[0], array);

  }


}