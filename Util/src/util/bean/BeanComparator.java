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
      throw new IllegalStateException("unable to find method"); // TODO: externaliser
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

    int x = 0;
    Object value1 = invokeMethod(o1, method);
    Object value2 = invokeMethod(o2, method);

    if (value1 != null && value2 != null) {
      x = isAsc ? ((Comparable)value1).compareTo(value2) :
                  ((Comparable)value2).compareTo(value1);
    }
    else if (value1 != null) {
      x = isNullFirst ? 1 : -1;
    }
    else if (value2 != null) {
      x = isNullFirst ? -1 : 1;
    }

    return x;

  }


}