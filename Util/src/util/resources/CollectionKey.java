package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("collection")
public enum CollectionKey implements IKey {


  // Sorter --------------------------------------------------------------------

  SORTER_CHECK_KO("sorter.check.ko");


  // —————————————————————————————————————————————————————————————— Constructors


  private CollectionKey(String key) {
    this.key = key;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final String key;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String getValue() {
    return key;
  }


}