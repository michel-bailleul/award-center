package util.swing.app;


import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;
import static java.util.prefs.Preferences.userNodeForPackage;
import static javax.swing.SwingUtilities.invokeLater;

import static util.swing.app.ActionType.RUN_POST_INIT_EVENT_QUEUE_EMPTY;
import static util.swing.app.ActionType.RUN_START_ON_EDT;
import static util.swing.app.ActionType.RUN_WAIT_EVENTS;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.LogKey.APP_ALREADY_EXISTS;
import static util.resources.LogKey.APP_ALREADY_RUNNING;


import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

import util.resource.Logger;


public abstract class Application implements UncaughtExceptionHandler {


  // —————————————————————————————————————————————————————————— Static Constants


  private static Application SINGLETON;


  // ———————————————————————————————————————————————————————————— Static Methods


  public static Application getSingleton() {
    return SINGLETON;
  }


  // —————————————————————————————————————————————————————————————— Constructors


  public Application() {

    actions = new ConcurrentHashMap<IActionType, ActionListener>();

    if (SINGLETON != null) {
      throw new IllegalStateException(getMsg(APP_ALREADY_EXISTS));
    }

    SINGLETON = this;

    try {
      setDefaultUncaughtExceptionHandler(this);
    }
    catch (SecurityException x) {
      x.printStackTrace();
    }


  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private boolean isRunning;

  private Map<IActionType, ActionListener> actions;

  private Preferences preferences;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _start() throws Exception {
    preInit();
    init();
    new Thread(new Runner(this, RUN_WAIT_EVENTS)).start();
  }


  private void _waitForEmptyEventQueue() {

    EventQueue eq = getDefaultToolkit().getSystemEventQueue();
    WaitEvent we = new WaitEvent();

    do {
      we.reset();
      eq.postEvent(we);
    }
    while (!we.isEmpty());

    invokeLater(new Runner(this, RUN_POST_INIT_EVENT_QUEUE_EMPTY));

  }


  // ——————————————————————————————————————————————————————————————————— Methods


  boolean exec(IActionType type, Object... params) {

    boolean isAction = true;

    try {
      if (type instanceof ActionType) {
        // basic features
        switch ((ActionType) type) {
          case RUN_START_ON_EDT :
            _start();
            break;
          case RUN_WAIT_EVENTS :
            _waitForEmptyEventQueue();
            break;
          case RUN_POST_INIT_EVENT_QUEUE_EMPTY :
            postInit();
            break;
          case NEW :
            neo();
            break;
          case OPEN :
            open();
            break;
          case CLOSE :
            close();
            break;
          case CLOSE_ALL :
            closeAll();
            break;
          case SAVE :
            save();
            break;
          case SAVE_AS :
            saveAs();
            break;
          case SAVE_ALL :
            saveAll();
            break;
          case PRINT :
            print();
            break;
          case UNDO :
            undo();
            break;
          case REDO :
            redo();
            break;
          case CUT :
            cut();
            break;
          case COPY :
            copy();
            break;
          case PASTE :
            paste();
            break;
          case DELETE :
            delete();
            break;
          case SELECT_ALL :
            selectAll();
            break;
          case FIND :
            find();
            break;
          case FIND_NEXT :
            findNext();
            break;
          case FIND_PREVIOUS :
            findPrevious();
            break;
          case REPLACE :
            replace();
            break;
          case ABOUT :
            about();
            break;
          case EXIT :
            exit();
            break;
          default :
            isAction = false;
        }
      }
      else {
        // specific features
        isAction = action(type, params);
      }
    }
    catch (Exception x) {
      getLogger().error("Unexpected Exception [{0}]", x, type);
    }

    return isAction;

  }


  // ————————————————————————————————————————————————————————— Protected Methods


  protected void preInit() throws Exception { }

  protected void init() throws Exception { }

  protected void postInit() throws Exception { }

  protected void neo() throws Exception { }

  protected void open() throws Exception { }

  protected void close() throws Exception { }

  protected void closeAll() throws Exception { }

  protected void save() throws Exception { }

  protected void saveAs() throws Exception { }

  protected void saveAll() throws Exception { }

  protected void print() throws Exception { }

  protected void undo() throws Exception { }

  protected void redo() throws Exception { }

  protected void cut() throws Exception { }

  protected void copy() throws Exception { }

  protected void paste() throws Exception { }

  protected void delete() throws Exception { }

  protected void selectAll() throws Exception { }

  protected void find() throws Exception { }

  protected void findNext() throws Exception { }

  protected void findPrevious() throws Exception { }

  protected void replace() throws Exception { }

  protected void about() throws Exception { }

  protected void exit() throws Exception {
    if (preferences != null) {
      preferences.flush();
    }
    System.exit(0);
  }


  protected boolean action(IActionType type, Object... params) throws Exception {
    return false;
  };


  protected Preferences loadPreferences(Class<?> klass) {
    return preferences = userNodeForPackage(klass);
  }


  protected Preferences getPreferences() {
    return preferences;
  }


  protected Logger getLogger() {
    return Logger.getLogger(getClass());
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public ActionListener getActionListener(IActionType actionType, Object... params) {

    ActionListener action = actions.get(actionType);

    if (action == null) {
      action = new ActionAdapter(this, actionType, params);
      actions.put(actionType, action);
    }

    return action;

  }


  //TODO: a refaire, ce n'est pas propre !
  public WindowListener getWindowListener(IActionType actionType, Object... params) {
    return new WindowAdapter(this, actionType, params);
  }


  @Override
  public void uncaughtException(Thread t, Throwable x) {
    getLogger().fatal("Uncaught Exception", x); //TODO: deplacer message en resource
  }


  /**
   * Starts the application
   */
  public final void start() {

    if (isRunning) {
      throw new IllegalStateException(getMsg(APP_ALREADY_RUNNING));
    }

    isRunning = true;

    invokeLater(new Runner(this, RUN_START_ON_EDT));

  }


}