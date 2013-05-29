package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("app")
public enum AppKey implements IKey {


  // Application ---------------------------------------------------------------

  APP_ALREADY_EXISTS  ("app.already.exists"),
  APP_ALREADY_RUNNING ("app.already.running");


  // —————————————————————————————————————————————————————————————— Constructors


  private AppKey(String key) {
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