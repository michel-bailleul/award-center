package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("resource")
public enum ResourceKey implements IKey {


  // ResourceUtil --------------------------------------------------------------

  RESOURCE_UTIL_ADD_BUNDLE           ("resource.util.add.bundle"),
  RESOURCE_UTIL_ERR_MISSING_RESOURCE ("resource.util.err.missing.resource"),
  RESOURCE_UTIL_ERR_MISSING_BUNDLE   ("resource.util.err.missing.bundle");


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