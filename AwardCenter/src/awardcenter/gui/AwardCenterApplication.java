package awardcenter.gui;


import static java.lang.System.getProperty;
import static java.util.Locale.FRENCH;

import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.*;
import static util.swing.app.ActionType.EXIT;

import static awardcenter.resources.Key.*;


import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import util.misc.StringUtil;
import util.resource.Logger;
import util.swing.app.Application;
import util.swing.app.IActionType;

import awardcenter.engine.IEngine;
import awardcenter.engine.XMLCodecEngine;
import awardcenter.resources.Key;


public class AwardCenterApplication extends Application {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(AwardCenterApplication.class);


  public static final int GAP = 10;

  public static final int IMG_SIZE_SMALL = 32;

  public static final int IMG_SIZE_LARGE = 64;

  private static final String IMAGE_DIR = "image.dir";

  private static final String ENGINE = "engine";


  // —————————————————————————————————————————————————————————————— Constructors


  public AwardCenterApplication() {
    setEngine(getProperty(ENGINE));
    Locale language = FRENCH; // TODO: get language from properties
    setLanguageGui(language);
    addBundle(Key.class, language);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private Controller controller;

  private Preferences preferences;

  private File dir = null;

  private IEngine engine;


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected void preInit() {

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (InstantiationException x) {
    }
    catch (IllegalAccessException x) {
    }
    catch (ClassNotFoundException x) {
    }
    catch (UnsupportedLookAndFeelException x) {
    }

  }


  @Override
  protected void init() {

    JFrame frame = new JFrame(getMsg(APP_NAME));
    List<Image> icons = new ArrayList<Image>();
    icons.add(getImg(ICON_APP16));
//    icons.add(getImg(ICON_APP32));
//    icons.add(getImg(ICON_APP64));
    frame.setIconImages(icons);
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(getWindowListener(EXIT));
    controller = new Controller(this, frame);
//    frame.setLocationRelativeTo(null);
    frame.setLocationByPlatform(true);
    frame.setResizable(true);
    frame.setSize(1200, 900);
    frame.setVisible(true);

  }


  @Override
  protected boolean action(IActionType type, Object... objs) {

    boolean hasAction = true;

    if (type instanceof AwardCenterActionType) {
      switch ((AwardCenterActionType)type) {
        case IMPORT :
          controller.actionGameImport();
          break;
        case EXPORT :
          controller.actionGameExport();
          break;
        case GAME_INSERT :
          controller.actionGameInsert();
          break;
        case GAME_UPDATE :
          controller.actionGameUpdate();
          break;
        case GAME_DELETE :
          controller.actionGameDelete();
          break;
        case GAME_FILTER :
          controller.actionGameFilter();
          break;
        case AWARD_INSERT :
          controller.actionAwardInsert();
          break;
        case AWARD_DELETE :
          controller.actionAwardDelete();
          break;
        case AWARD_SEPARATOR :
          controller.actionAwardSeparator();
          break;
        case AWARD_FILTER :
          controller.actionAwardFilter();
          break;
        default :
          hasAction = false;
      }
    }

    return hasAction;

  }


  @Override
  protected void save() {
    controller.actionSave();
  }


  @Override
  protected void about() {
    controller.actionAbout();
  }


  @Override
  protected void exit() {
    if (controller.actionClose()) {
      logger.debug("Exit");
      super.exit();
    }
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public Preferences getPreferences() {
    return preferences;
  }


  public void setPreferences(Preferences preferences) {
    this.preferences = preferences;
  }


  /** retourne une preference de l'application [version String] */
  public String getPreference(String key, String def) {
    return preferences.get(key, def);
  }

  public void setPreference(String key, String value) {
    preferences.put(key, value);
  }


  /** retourne une preference de l'application [version int] */
  public int getPreference(String key, int def) {
    return preferences.getInt(key, def);
  }

  public void setPreference(String key, int value) {
    preferences.putInt(key, value);
  }


  public File getImageDir() {

    if (dir == null) {
      String path = getPreference(IMAGE_DIR, null);
      if (!StringUtil.isEmpty(path)) {
        dir = new File(path);
      }
    }

    return (dir != null && dir.isDirectory()) ? dir : null;

  }


  public void setImageDir(File dir) {

    if (dir != null) {
      setPreference(IMAGE_DIR, dir.getAbsolutePath());
    }

  }


  public IEngine getEngine() {
    return engine;
  }


  public void setEngine(String className) {

    if (StringUtil.isEmpty(className)) {
      engine = new XMLCodecEngine();
    }
    else {
      try {
        engine = (IEngine) Class.forName(className).getConstructor().newInstance();
      }
      catch (Exception x) {
        RuntimeException ex = new IllegalArgumentException(className, x);
        logger.fatal("Unexpected Exception", ex);
        throw ex;
      }
    }

    logger.info("Engine : [{0}]", engine.getClass().getName());

  }


}