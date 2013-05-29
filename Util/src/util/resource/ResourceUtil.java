package util.resource;


import static java.util.Locale.ENGLISH;
import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;

import static util.swing.ImageUtil.getImage;
import static util.swing.ImageUtil.getImageIcon;

import static util.misc.StringUtil.formatMessage;
import static util.resource.Logger.getLogger;
import static util.resources.ResourceKey.RESOURCE_UTIL_ADD;
import static util.resources.ResourceKey.RESOURCE_UTIL_ERR_MISSING_BUNDLE;
import static util.resources.ResourceKey.RESOURCE_UTIL_ERR_MISSING_RESOURCE;


import java.awt.Image;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import javax.swing.Icon;

import util.resources.SwingGuiKey;
import util.resources.AppKey;
import util.resources.CodecKey;
import util.resources.CollectionKey;
import util.resources.IOKey;
import util.resources.ResourceKey;
import util.resources.SwingLogKey;


public final class ResourceUtil {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(ResourceUtil.class);

  private static final Map<Class<? extends IKey>, ResourceBundle> bundles =
               new HashMap<Class<? extends IKey>, ResourceBundle>();

  // logs
  static {
    loadBundles(ENGLISH, true);
  }


  // ——————————————————————————————————————————————————————————— Private Methods


  private static void loadBundles(Locale language, boolean isLog) {
    if (isLog) {
      // Logs
      addBundle(ResourceKey.class,   language); // util.resource : 1st !!!
      addBundle(CodecKey.class,      language); // util.codec
      addBundle(CollectionKey.class, language); // util.collection
      addBundle(IOKey.class,         language); // util.io
      addBundle(AppKey.class,        language); // util.app
      addBundle(SwingLogKey.class,   language); // util.swing
    }
    else {
      // GUI
      addBundle(SwingGuiKey.class,   language); // util.swing
    }
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public static void setLanguageLog(Locale language) {
    loadBundles(language, true);
  }


  public static void setLanguageGui(Locale language) {
    loadBundles(language, false);
  }


  public static void addBundle(Class<? extends IKey> klass) {
    addBundle(klass, getDefault());
  }


  public static void addBundle(Class<? extends IKey> klass, Locale locale) {

    String pkgName  = klass.getPackage().getName();
    String fileName = klass.getAnnotation(FileName.class).value();
    String baseName = pkgName + "." + fileName;

    Control control = new XMLResourceBundleControl();

    try {
      ResourceBundle bundle = getBundle(baseName, (locale != null) ? locale : getDefault(), control);
      bundles.put(klass, bundle);
    }
    catch (MissingResourceException x) {
      logger.error(RESOURCE_UTIL_ERR_MISSING_BUNDLE, x, baseName);
    }

    logger.debug(RESOURCE_UTIL_ADD, locale, baseName);

  }


  public static String getMsg(IKey key, Object... params) {

    String msg = null;
    ResourceBundle bundle = bundles.get(key.getClass());

    try {
      msg = bundle.getString(key.getKey());
      msg = formatMessage(msg, params);
    }
    catch (MissingResourceException x) {
      msg = "!" + key.getKey() + "!";
      logger.error(RESOURCE_UTIL_ERR_MISSING_RESOURCE, x, msg);
    }

    return msg;

  }


  public static Icon getIcon(IKey key) {
    return getImageIcon(null, key, 0, 0);
  }


  public static Image getImg(IKey key) {
    return getImage(null, key, 0, 0);
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass */
  private ResourceUtil() { }


}