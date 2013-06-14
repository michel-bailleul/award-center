package util.collection;


import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.collection.CollectionUtil;
import util.collection.IFilter;


public class TestCollectionUtil {


  // ———————————————————————————————————————————————————————— Instance Variables


  int listSize;
  List<Product> list;

  int setSize;
  Set<Product> set;

  IFilter<Product> filter;

  int expected;
  String _filterColor = "RED";
  Double _filterPriceMin =  1000000.00;
  Double _filterPriceMax = 10000000.00;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void setUp() throws Exception {

    list = new ArrayList<Product>();
    list.add(new Product("Ferrari",    "red",   1500000.00));
    list.add(new Product("New Beetle", "red",     20000.00));
    list.add(new Product("Twingo",     "black",   12000.00));
    list.add(new Product("Watch",      "black",      99.00));
    list.add(new Product("Computer",   null,        990.00));
    list.add(new Product("House",      "white",  300000.00));
    list.add(new Product("Wasabi",     "green",       3.50));
    list.add(new Product("Star",       "red",   9999999.99));

    listSize = list.size();

    set = new HashSet<Product>(list);
    setSize = set.size();

    filter = new IFilter<Product>()
    {
      @Override
      public boolean matches(Product obj) {
        if (obj == null) {
          return false;
        }
        if (_filterColor != null && obj.getColor() != null &&
            !obj.getColor().equalsIgnoreCase(_filterColor))
        {
          return false;
        }
        if (_filterPriceMin != null && _filterPriceMin > obj.getPrice()) {
          return false;
        }
        if (_filterPriceMax != null && _filterPriceMax < obj.getPrice()) {
          return false;
        }
        return true;
      }
    };

    Product.resetFilter();
    Product.setFilterColor(_filterColor);
    Product.setFilterPriceMin(_filterPriceMin);
    Product.setFilterPriceMax(_filterPriceMax);
    expected = 2;

  }


  @After
  public void tearDown() throws Exception {
  }


  // —————————————————————————————————————————————————————————————— Test Methods


  private void fill(Collection c) {

    c.add(new Product("Car", "RED", 20000));
    c.add(new Product("Boat", null, 500000));
    c.add(new Product("Bike", "BLUE", 200));
    c.add(new Product("Plane", "RED", 1000000));
    c.add(new Product("Book", "", 0));
    c.add(new Product("Flower", "BLUE", 0));
    c.add(new Product("Computer", null, 0));
    c.add(new Product("Object", "", 0));

  }


  @Test
  public void testFilter() {

    // List -
    System.out.println("Test List ----------");
    System.out.println(list);
    CollectionUtil.filter(list, filter);
    System.out.println(list);
    assertEquals(list.size(), expected);

    // Set -
    System.out.println("Test Set ----------");
    System.out.println(set);
    CollectionUtil.filter(set, filter);
    System.out.println(set);
    assertEquals(set.size(), expected);

  }


  @Test
  public void testFilterCopy() {

    Collection<Product> trg;

    // List -
    System.out.println("Test List Copy ----------");
    trg = new ArrayList<Product>();
    CollectionUtil.filter(list, trg, filter);
    System.out.println(list);
    assertEquals(list.size(), listSize);
    System.out.println(trg);
    assertEquals(trg.size(), expected);

    // Set -
    System.out.println("Test Set Copy ----------");
    trg = new HashSet<Product>();
    CollectionUtil.filter(set, trg, filter);
    System.out.println(set);
    assertEquals(set.size(), setSize);
    System.out.println(trg);
    assertEquals(trg.size(), expected);

  }


  @Test
  public void testFilterStatic() {

    // List -
    System.out.println("Test List ----------");
    System.out.println(list);
    CollectionUtil.filter(list);
    System.out.println(list);
    assertEquals(list.size(), expected);

    // Set -
    System.out.println("Test Set ----------");
    System.out.println(set);
    CollectionUtil.filter(set);
    System.out.println(set);
    assertEquals(set.size(), expected);

  }


  @Test
  public void testFilterCopyStatic() {

    Collection<Product> trg;

    // List -
    System.out.println("Test List Copy ----------");
    trg = new ArrayList<Product>();
    CollectionUtil.filter(list, trg);
    System.out.println(list);
    assertEquals(list.size(), listSize);
    System.out.println(trg);
    assertEquals(trg.size(), expected);

    // Set -
    System.out.println("Test Set Copy ----------");
    trg = new HashSet<Product>();
    CollectionUtil.filter(set, trg);
    System.out.println(set);
    assertEquals(set.size(), setSize);
    System.out.println(trg);
    assertEquals(trg.size(), expected);

  }


  /**
   * Test de la methode {@link CollectionUtil#groupBy(Collection, String)}
   */
  @Test
  public void testGroupByList() {

    System.out.println("Test Group By List ----------");

    List<Product> list = new LinkedList<Product>();
    fill(list);
    System.out.println(list);

    Map<Object, List<Product>> map = CollectionUtil.groupBy(list, "color");
    for (Map.Entry<Object, List<Product>> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }

  }


  /**
   * Test de la methode {@link CollectionUtil#groupBy(Collection, String)}
   */
  @Test
  public void testGroupBySet() {

    System.out.println("Test Group By Set ----------");

    Set<Product> set = new TreeSet<Product>();
    fill(set);
    System.out.println(set);

    Map<Object, Set<Product>> map = CollectionUtil.groupBy(set, "color");
    for (Map.Entry<Object, Set<Product>> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }

  }


  /**
   * Test de la methode {@link CollectionUtil#groupBy(Collection, String)}
   */
  @Test
  public void testGroupByQueue() {

    System.out.println("Test Group By Queue ----------");

    Queue<Product> queue = new LinkedList<Product>();
    fill(queue);
    System.out.println(queue);

    Map<Object, Queue<Product>> map = CollectionUtil.groupBy(queue, "color");
    for (Map.Entry<Object, Queue<Product>> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }

  }


  /**
   * Test de la methode {@link CollectionUtil#collect(Collection, String)}
   */
  @Test
  public void testCollectList() {

    List<Product> list = new ArrayList<Product>();

    fill(list);
    System.out.println(list);

    List<Object> values = CollectionUtil.collect(list, "color");
    System.out.println(values);

  }


  /**
   * Test de la methode {@link CollectionUtil#collect(Collection, String)}
   */
  @Test
  public void testCollectSet() {

    Set<Product> set = new HashSet<Product>();

    fill(set);
    System.out.println(set);

    Set<Object> values = CollectionUtil.collect(set, "color");
    System.out.println(values);

  }


}