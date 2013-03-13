package util.bean;


import static java.beans.Introspector.getBeanInfo;
import static util.misc.StringUtil.isEmptyTrim;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public final class BeanUtil {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(BeanUtil.class);


  // ——————————————————————————————————————————————————————————— Private Methods


  private static Method _getGetterOrSetter(Class<?> klass, String name, boolean isGetter) {

    if (klass == null || isEmptyTrim(name)) {
      throw new IllegalArgumentException("class or property is null"); // TODO: externaliser
    }

    BeanInfo info = null;

    try {
      info = getBeanInfo(klass);
    }
    catch (IntrospectionException x) {
      x.printStackTrace(); // TODO: log
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


  public static Object getProperty(Object bean, String name) {
    return invokeMethod(bean, getGetter(bean.getClass(), name));
  }


  public static Method getGetter(Class<?> klass, String name) {
    return _getGetterOrSetter(klass, name, true);
  }


  public static Method getSetter(Class<?> klass, String name) {
    return _getGetterOrSetter(klass, name, false);
  }


  public static Object invokeMethod(Object bean, Method method, Object... args) {

    if (bean == null || method == null) {
      throw new IllegalArgumentException("bean or method is null"); // TODO: externaliser
    }

    try {
      return method.invoke(bean, args);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
      x.printStackTrace(); // TODO: log
    }

    return null;

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private BeanUtil() { }


}