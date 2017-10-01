package util.bean;


import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestBeanUtil {


  // ———————————————————————————————————————————————————————————— Public Methods


  @Before
  public void before() throws Exception { }


  @After
  public void after() throws Exception { }


  // —————————————————————————————————————————————————————————————— Test Methods


  @Test
  public void testAncestors() {

    List<Class<?>> classes;

    System.out.println();
    System.out.println("ArrayList --------------------------");
    classes = BeanUtil.getAncestors(java.util.ArrayList.class);

    for (Class<?> klass : classes) {
      System.out.println(klass);
    }

    System.out.println();
    System.out.println("LinkedList --------------------------");
    classes = BeanUtil.getAncestors(java.util.LinkedList.class);

    for (Class<?> klass : classes) {
      System.out.println(klass);
    }

  }


  @Test
  public void testCommonAncestor() {

    System.out.println();
    System.out.println("CommonAncestor --------------------------");
    Class<?> klass = BeanUtil.getCommonAncestor(java.util.ArrayList.class, java.util.LinkedList.class);
    System.out.println(klass);

  }


}