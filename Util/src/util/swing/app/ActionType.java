package util.swing.app;


public enum ActionType implements IActionType {

  RUN_POST_INIT_EVENT_QUEUE_EMPTY,
  RUN_START_ON_EDT,
  RUN_WAIT_EVENTS,
  RUN_WAIT_FOR_BACKGROUND_THREADS,
  UNCAUGHT_EXCEPTION,
  // basic features
  NEW,
  OPEN,
  CLOSE,
  CLOSE_ALL,
  SAVE,
  SAVE_AS,
  SAVE_ALL,
  PRINT,
  UNDO,
  REDO,
  CUT,
  COPY,
  PASTE,
  DELETE,
  SELECT_ALL,
  FIND,
  FIND_NEXT,
  FIND_PREVIOUS,
  REPLACE,
  ABOUT,
  EXIT;

}