package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("swing-log")
public enum SwingLogKey implements IKey {


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