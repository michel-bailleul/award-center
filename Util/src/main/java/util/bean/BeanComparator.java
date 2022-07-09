package util.bean;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.invokeMethod;
import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.LogKey.BEAN_COMPARATOR_ERR_GETTER;


import java.lang.reflect.Method;
import java.util.Comparator;

import util.resource.Logger;


public class BeanComparator<T> implements Comparator<T> {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(BeanComparator.class);


  // —————————————————————————————————————————————————————————————— Constructors


  /**
   * {@code BeanComparator(klass, property, true)}
   *
   * @param klass    - Component type
   * @param property - Property name
   */
  public BeanComparator(Class<?> klass, String property) {
    this(klass, property, true);
  }


  /**
   * {@code BeanComparator(klass, property, isAsc, false)}
   *
   * @param klass    - Component type
   * @param property - Property name
   * @param isAsc    - Specifies that the results should be returned in ascending / descending order.
   */
  public BeanComparator(Class<?> klass, String property, boolean isAsc) {
    this(klass, property, isAsc, false);
  }


  /**
   * Initialize the bean comparator
   *
   * @param klass       - Component type
   * @param property    - Property name
   * @param isAsc       - Specifies that the results should be returned in ascending / descending order.
   * @param isNullFirst - Specifies that NULL values should be returned before / after non-NULL values.
   */
  public BeanComparator(Class<?> klass, String property, boolean isAsc, boolean isNullFirst) {

    asc = isAsc ? 1 : -1;
    this.isNullFirst = isNullFirst;
    method = getGetter(property, klass);

    if (method == null) {
      String msg = getMsg(BEAN_COMPARATOR_ERR_GETTER, property);
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    }

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final int asc;

  private final boolean isNullFirst;

  private final Method method;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public int compare(T o1, T o2) {

    Object v1 = (o1 != null) ? invokeMethod(o1, method) : null;
    Object v2 = (o2 != null) ? invokeMethod(o2, method) : null;

    if (v1 == null && v2 == null) {
      return 0;
    }

    if (v1 == null) {
      return isNullFirst ? -1 : 1;
    }

    if (v2 == null) {
      return isNullFirst ? 1 : -1;
    }

    return asc * ((Comparable) v1).compareTo(v2);

  }


}