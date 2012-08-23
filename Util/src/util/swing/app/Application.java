package util.swing.app;


import static java.awt.EventQueue.isDispatchThread;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;

import static javax.swing.SwingUtilities.invokeLater;

import static util.swing.app.ActionType.RUN_POST_INIT_EVENT_QUEUE_EMPTY;
import static util.swing.app.ActionType.RUN_START_ON_EDT;
import static util.swing.app.ActionType.RUN_WAIT_EVENTS;

import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.AppKey.APP_ALREADY_EXISTS;
import static util.resources.AppKey.APP_ALREADY_RUNNING;


import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import util.resource.Logger;


public abstract class Application implements UncaughtExceptionHandler {


  // —————————————————————————————————————————————————————————— Static Constants


  private static Logger logger = getLogger(Application.class);

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


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _start() {
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

    if (type instanceof ActionType) {
      // basic features
      switch ((ActionType)type) {
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

    return isAction;

  }


  // ————————————————————————————————————————————————————————— Protected Methods


  protected void preInit() { }

  protected void init() { }

  protected void postInit() { }

  protected void neo() { }

  protected void open() { }

  protected void close() { }

  protected void closeAll() { }

  protected void save() { }

  protected void saveAs() { }

  protected void saveAll() { }

  protected void print() { }

  protected void undo() { }

  protected void redo() { }

  protected void cut() { }

  protected void copy() { }

  protected void paste() { }

  protected void delete() { }

  protected void selectAll() { }

  protected void find() { }

  protected void findNext() { }

  protected void findPrevious() { }

  protected void replace() { }

  protected void about() { }

  protected void exit() {
    System.exit(0);
  }

  protected boolean action(IActionType type, Object... params) {
    return false;
  };


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
    logger.fatal("Uncaught Exception", x); //TODO: deplacer message en resource
  }


  /**
   * Starts the application
   */
  public final void start() {

    if (isRunning) {
      throw new IllegalStateException(getMsg(APP_ALREADY_RUNNING));
    }

    isRunning = true;

    if (!isDispatchThread()) {
      invokeLater(new Runner(this, RUN_START_ON_EDT));
    }
    else {
      _start();
    }

  }


}