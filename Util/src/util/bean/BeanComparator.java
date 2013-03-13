package util.bean;


import static util.bean.BeanUtil.getGetter;
import static util.bean.BeanUtil.invokeMethod;


import java.lang.reflect.Method;
import java.util.Comparator;


public class BeanComparator<T> implements Comparator<T> {


  // —————————————————————————————————————————————————————————————— Constructors


  public BeanComparator(Class<?> klass, String name) {
    this(klass, name, true);
  }


  public BeanComparator(Class<?> klass, String name, boolean isAsc) {
    this(klass, name, true, false);
  }


  public BeanComparator(Class<?> klass, String name, boolean isAsc, boolean isNullFirst) {
    this.isAsc = isAsc;
    this.isNullFirst = isNullFirst;
    method = getGetter(klass, name);
    if (method == null) {
      throw new IllegalStateException("unable to find getter for property: " + name); // TODO: externaliser
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