package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("codec")
public enum CodecKey implements IKey {


  // Base64 --------------------------------------------------------------------

  BASE64_ERR_LENGTH    ("base64.err.length"),
  BASE64_ERR_CHARACTER ("base64.err.character");


  // —————————————————————————————————————————————————————————————— Constructors


  private CodecKey(String key) {
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