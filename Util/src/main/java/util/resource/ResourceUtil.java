package util.resource;


import static java.util.Locale.ENGLISH;
import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;

import static util.misc.StringUtil.formatMessage;
import static util.resource.Logger.getLogger;
import static util.resources.LogKey.RESOURCE_UTIL_ADD_BUNDLE;
import static util.resources.LogKey.RESOURCE_UTIL_ERR_MISSING_BUNDLE;
import static util.resources.LogKey.RESOURCE_UTIL_ERR_MISSING_RESOURCE;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import javax.annotation.Resource;

import util.resources.GuiKey;
import util.resources.LogKey;


public final class ResourceUtil {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(ResourceUtil.class);

  private static final Map<Class<? extends IKey>, ResourceBundle> BUNDLES = new HashMap<>();


  // —————————————————————————————————————————————————————————— Static Variables


  private static Locale languageLog;

  private static Locale languageGui;

  // init resource bundles
  static {
    setLanguageLog(ENGLISH);      // default language for logs
    setLanguageGui(getDefault()); // default language for GUI
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public static void setLanguageLog(Locale language) {
    languageLog = language;
    addBundleLog(LogKey.class);
  }


  public static void setLanguageGui(Locale language) {
    languageGui = language;
    addBundleGui(GuiKey.class);
  }


  public static void addBundleLog(Class<? extends IKey> klass) {
    addBundle(klass, languageLog);
  }


  public static void addBundleGui(Class<? extends IKey> klass) {
    addBundle(klass, languageGui);
  }


  public static void addBundle(Class<? extends IKey> klass) {
    addBundle(klass, getDefault());
  }


  public static void addBundle(Class<? extends IKey> klass, Locale locale) {

//    final String baseName = klass.getAnnotation(FileName.class).value();
    final String baseName = klass.getAnnotation(Resource.class).name();
//    String fileName = klass.getAnnotation(FileName.class).value();
//    String pkgName  = klass.getPackage().getName();
//    String baseName = pkgName + "." + fileName;

    Control control = new XMLResourceBundleControl();

    try {
      ResourceBundle bundle = getBundle(baseName, (locale != null) ? locale : getDefault(), control);
      BUNDLES.put(klass, bundle);
      logger.info(RESOURCE_UTIL_ADD_BUNDLE, locale, baseName);
    }
    catch (MissingResourceException x) {
      logger.error(RESOURCE_UTIL_ERR_MISSING_BUNDLE, x, baseName);
    }

  }


  public static String getMsg(IKey key, Object... params) {

    String msg = null;
    ResourceBundle bundle = BUNDLES.get(key.getClass());

    try {
      if (bundle == null) {
        throw new IllegalStateException();
      }
      msg = bundle.getString(key.getKey());
      msg = formatMessage(msg, params);
    }
    catch (MissingResourceException x) {
      logger.error(RESOURCE_UTIL_ERR_MISSING_RESOURCE, x, key.getKey());
    }
    catch (Exception x) {
      logger.error("ResourceBundle not found [{0}]", x, key.getClass().getName());
    }

    return msg;

  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this klass */
  private ResourceUtil() { }


}
