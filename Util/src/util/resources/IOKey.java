package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("io")
public enum IOKey implements IKey {


  // FileUtil ------------------------------------------------------------------

  FILE_UTIL_COPY      ("file.util.copy"),
  FILE_UTIL_READ      ("file.util.read"),
  FILE_UTIL_IOX       ("file.util.iox"),
  FILE_UTIL_NOT_FOUND ("file.util.not.found");


  // —————————————————————————————————————————————————————————————— Constructors


  private IOKey(String key) {
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