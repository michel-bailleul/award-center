package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("codec")
public enum CodecKey implements IKey {


  // Coder ------------------------------------------------------------------

  CODEC_ERROR_LENGTH    ("codec.error.length"),
  CODEC_ERROR_CHARACTER ("codec.error.character");


  // —————————————————————————————————————————————————————————————— Constructors


  private CodecKey(String key) {
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