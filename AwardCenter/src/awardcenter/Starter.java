package awardcenter;


import static java.util.prefs.Preferences.userNodeForPackage;


import awardcenter.gui.AwardCenterApplication;


public class Starter {


  // ——————————————————————————————————————————————————————————————— Main Method


  public static void main(String[] args) {

    AwardCenterApplication app = new AwardCenterApplication();
    app.setPreferences(userNodeForPackage(Starter.class));
    app.start();

  }


}