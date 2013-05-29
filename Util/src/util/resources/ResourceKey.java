package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("resource")
public enum ResourceKey implements IKey {


  // ResourceUtil --------------------------------------------------------------

  RESOURCE_UTIL_ADD                    ("resource.util.add"),
  RESOURCE_UTIL_ERROR_MISSING_RESOURCE ("resource.util.error.missing.resource"),
  RESOURCE_UTIL_ERROR_MISSING_BUNDLE   ("resource.util.error.missing.bundle");


  // —————————————————————————————————————————————————————————————— Constructors


  private ResourceKey(String key) {
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