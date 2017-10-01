package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("log")
public enum LogKey implements IKey {


  // Application ---------------------------------------------------------------

  APP_ALREADY_EXISTS  ("app.already.exists"),
  APP_ALREADY_RUNNING ("app.already.running"),


  // BeanUtil ------------------------------------------------------------------

  BEAN_UTIL_ERR_CLASS_NULL    ("bean.util.err.class.null"),
  BEAN_UTIL_ERR_BEAN_INFO     ("bean.util.err.bean.info"),
  BEAN_UTIL_ERR_BEAN_NULL     ("bean.util.err.bean.null"),
  BEAN_UTIL_ERR_INVOKE_METHOD ("bean.util.err.invoke.method"),


  // BeanComparator ------------------------------------------------------------

  BEAN_COMPARATOR_ERR_GETTER  ("bean.comparator.err.getter"),


  // Base64 --------------------------------------------------------------------

  BASE64_ERR_LENGTH    ("base64.err.length"),
  BASE64_ERR_CHARACTER ("base64.err.character"),


  // Sorter --------------------------------------------------------------------

  SORTER_CHECK_KO("sorter.check.ko"),
  COLLECTION_UTIL_PROPERTY_NOT_FOUND("collection.util.property.not.found"),


  // FileUtil ------------------------------------------------------------------

  FILE_UTIL_COPY      ("file.util.copy"),
  FILE_UTIL_READ      ("file.util.read"),
  FILE_UTIL_IOX       ("file.util.iox"),
  FILE_UTIL_NOT_FOUND ("file.util.not.found"),


  // ImageUtil -----------------------------------------------------------------

  IMAGE_UTIL_ADD           ("image.util.add"),
  IMAGE_UTIL_GET           ("image.util.get"),
  IMAGE_UTIL_DEFAULT       ("image.util.default"),
  IMAGE_UTIL_ERR_CREATE    ("image.util.err.create"),
  IMAGE_UTIL_ERR_URL       ("image.util.err.url"),
  IMAGE_UTIL_ERR_NOT_FOUND ("image.util.err.not.found"),


  // ImageViewer ---------------------------------------------------------------

  IMAGEVIEWER_READ_FILE ("imageviewer.read.file"),


  // GradientPanel -------------------------------------------------------------

  GRADIENTPANEL_ILLEGALARGUMENT ("gradientpanel.illegalargument"),


  // ResourceUtil --------------------------------------------------------------

  RESOURCE_UTIL_ADD_BUNDLE           ("resource.util.add.bundle"),
  RESOURCE_UTIL_ERR_MISSING_RESOURCE ("resource.util.err.missing.resource"),
  RESOURCE_UTIL_ERR_MISSING_BUNDLE   ("resource.util.err.missing.bundle");


  // —————————————————————————————————————————————————————————————— Constructors


  private LogKey(String key) {
    this.key = key;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final String key;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String getKey() {
    return key;
  }


}