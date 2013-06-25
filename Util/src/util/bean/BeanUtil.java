package util.bean;


import static java.beans.Introspector.getBeanInfo;
import static util.misc.StringUtil.isEmptyTrim;
import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.BeanKey.BEAN_UTIL_ERR_BEAN_INFO;
import static util.resources.BeanKey.BEAN_UTIL_ERR_BEAN_NULL;
import static util.resources.BeanKey.BEAN_UTIL_ERR_CLASS_NULL;
import static util.resources.BeanKey.BEAN_UTIL_ERR_INVOKE_METHOD;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import util.resource.Logger;


public final class BeanUtil {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(BeanUtil.class);


  // ——————————————————————————————————————————————————————————— Private Methods


  private static Method _getGetterOrSetter(String name, Class<?> klass, boolean isGetter) {

    if (klass == null || isEmptyTrim(name)) {
      String msg = getMsg(BEAN_UTIL_ERR_CLASS_NULL);
      logger.error(msg);
      throw new IllegalArgumentException(msg);
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
      String msg = getMsg(BEAN_UTIL_ERR_BEAN_NULL);
      logger.error(msg);
      throw new IllegalArgumentException(msg);
    }

    try {
      return method.invoke(bean, args);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
      logger.error(BEAN_UTIL_ERR_INVOKE_METHOD, x, method.getName());
    }

    return null;

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private BeanUtil() { }


}