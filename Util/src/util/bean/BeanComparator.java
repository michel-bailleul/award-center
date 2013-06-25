package util.bean;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.invokeMethod;
import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.BeanKey.BEAN_COMPARATOR_ERR_GETTER;


import java.lang.reflect.Method;
import java.util.Comparator;

import util.resource.Logger;


public class BeanComparator<T> implements Comparator<T> {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(BeanComparator.class);


  // —————————————————————————————————————————————————————————————— Constructors


  public BeanComparator(String name, Class<?> klass) {
    this(name, true, klass);
  }


  public BeanComparator(String name, boolean isAsc, Class<?> klass) {
    this(name, true, false, klass);
  }


  public BeanComparator(String name, boolean isAsc, boolean isNullFirst, Class<?> klass) {
    this.isAsc = isAsc;
    this.isNullFirst = isNullFirst;
    method = getGetter(name, klass);
    if (method == null) {
      String msg = getMsg(BEAN_COMPARATOR_ERR_GETTER, name);
      logger.error(msg);
      throw new IllegalStateException(msg);
    }
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final boolean isAsc;

  private final boolean isNullFirst;

  private final Method method;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public int compare(T o1, T o2) {

    int i = 0;
    Object v1 = invokeMethod(o1, method);
    Object v2 = invokeMethod(o2, method);

    if (v1 != null && v2 != null) {
      i = isAsc ? ((Comparable)v1).compareTo(v2) : ((Comparable)v2).compareTo(v1);
    }
    else if (v1 != null) {
      i = isNullFirst ? 1 : -1;
    }
    else if (v2 != null) {
      i = isNullFirst ? -1 : 1;
    }

    return i;

  }


}