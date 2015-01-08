package util.bean;


import static java.beans.Introspector.getBeanInfo;

import static util.misc.StringUtil.isEmptyTrim;
import static util.resource.Logger.getLogger;
import static util.resources.LogKey.BEAN_UTIL_ERR_BEAN_INFO;
import static util.resources.LogKey.BEAN_UTIL_ERR_BEAN_NULL;
import static util.resources.LogKey.BEAN_UTIL_ERR_CLASS_NULL;
import static util.resources.LogKey.BEAN_UTIL_ERR_INVOKE_METHOD;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import util.resource.Logger;


public final class BeanUtil {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(BeanUtil.class);


  // ——————————————————————————————————————————————————————————— Private Methods


  private static Method _getGetterOrSetter(String name, Class<?> klass, boolean isGetter) {

    if (klass == null || isEmptyTrim(name)) {
      throw new IllegalArgumentException(logger.error(BEAN_UTIL_ERR_CLASS_NULL));
    }

    BeanInfo info = null;

    try {
      info = getBeanInfo(klass);
    }
    catch (IntrospectionException x) {
      logger.error(BEAN_UTIL_ERR_BEAN_INFO, x, klass.getName());
    }

    if (info != null) {
      PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

      if (descriptors != null) {
        for (PropertyDescriptor descriptor : descriptors) {
          if (descriptor.getName().equals(name)) {
            return isGetter ? descriptor.getReadMethod() : descriptor.getWriteMethod();
          }
        }
      }
    }

    return null;

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @SuppressWarnings("unchecked")
  public static <T> T instantiate(T o) {
    return (T) ((o != null) ? instantiate(o.getClass()) : null);
  }


  public static <T> T instantiate(Class<T> klass) {

    try {
      return (klass != null) ? klass.newInstance() : null;
    }
    catch (ReflectiveOperationException x) {
      throw new UnsupportedOperationException(x);
    }

  }


  public static Object getProperty(String name, Object bean) {
    return invokeMethod(bean, getGetter(name, bean.getClass()));
  }


  public static Method getGetter(String name, Class<?> klass) {
    return _getGetterOrSetter(name, klass, true);
  }


  public static Method getSetter(String name, Class<?> klass) {
    return _getGetterOrSetter(name, klass, false);
  }


  public static Object invokeMethod(Object bean, Method method, Object... args) {

    if (bean == null || method == null) {
      throw new IllegalArgumentException(logger.error(BEAN_UTIL_ERR_BEAN_NULL));
    }

    try {
      return method.invoke(bean, args);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
      logger.error(BEAN_UTIL_ERR_INVOKE_METHOD, x, method.getName());
    }

    return null;

  }


  /**
   * Looking for all ancestors of a class
   *
   * @param c - Any class
   *
   * @return All ancestors of the class in descending order
   */
  public static List<Class<?>> getAncestors(Class<?> c) {

    if (c == null) {
      return new ArrayList<Class<?>>();
    }

    List<Class<?>> s = getAncestors(c.getSuperclass());
    s.add(c);

    return s;

  }


  /**
   * Looking for all ancestors of an object
   *
   * @param o - Any object
   *
   * @return All ancestors of the object in descending order
   */
  public static <T> List<Class<?>> getAncestors(T o) {
    return getAncestors((o != null) ? o.getClass() : null);
  }


  /**
   * Looking for the common ancestor of 2 classes
   *
   * @param c1 - 1st class
   * @param c2 - 2nd class
   *
   * @return The common ancestor of the classes
   */
  public static Class<?> getCommonAncestor(Class<?> c1, Class<?> c2) {

    List<Class<?>> s = getAncestors(c1);

    while (!s.contains(c2)) {
      c2 = c2.getSuperclass();
    }

    return c2;

  }


  /**
   * Looking for the common ancestor of 2 objects
   *
   * @param o1 - 1st object
   * @param o2 - 2nd object
   *
   * @return The common ancestor of the objects
   */
  public static <U, V> Class<?> getCommonAncestor(U o1, V o2) {
    return getCommonAncestor((o1 != null) ? o1.getClass() : null, (o2 != null) ? o2.getClass() : null);
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private BeanUtil() { }


}