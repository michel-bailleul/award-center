package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("swing-log")
public enum SwingLogKey implements IKey {


  // ImageUtil -----------------------------------------------------------------

  IMAGE_UTIL_ADD             ("image.util.add"),
  IMAGE_UTIL_GET             ("image.util.get"),
  IMAGE_UTIL_DEFAULT         ("image.util.default"),
  IMAGE_UTIL_ERROR_CREATE    ("image.util.error.create"),
  IMAGE_UTIL_ERROR_URL       ("image.util.error.url"),
  IMAGE_UTIL_ERROR_NOT_FOUND ("image.util.error.not.found"),


  // ImageViewer ---------------------------------------------------------------

  IMAGEVIEWER_READ_FILE    ("imageviewer.read.file"),


  // GradientPanel -------------------------------------------------------------

  GRADIENTPANEL_ILLEGALARGUMENT ("gradientpanel.illegalargument");


  // —————————————————————————————————————————————————————————————— Constructors


  private SwingLogKey(String key) {
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