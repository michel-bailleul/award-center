package util.resource;


import static org.slf4j.MarkerFactory.getMarker;
import static util.misc.StringUtil.formatMessage;
import static util.resource.ResourceUtil.getMsg;


import org.slf4j.LoggerFactory;
import org.slf4j.Marker;


public final class Logger {


  // ————————————————————————————————————————————————————————————————————— Enums


  private static enum Level { TRACE, DEBUG, INFO, WARN, ERROR, FATAL }

  private static Marker FATAL = getMarker("FATAL");


  // ———————————————————————————————————————————————————————————— Static Methods


  public static Logger getLogger(Class<?> klass) {
    return new Logger(klass);
  }


  // —————————————————————————————————————————————————————————————— Constructors


  private Logger(Class<?> klass) {
    logger = LoggerFactory.getLogger(klass);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private org.slf4j.Logger logger;


  // ——————————————————————————————————————————————————————————— Private Methods


  private String _log(Level level, IKey key, Throwable t, Object... params) {
    return _log(level, getMsg(key), t, params);
  }


  private String _log(Level level, String msg, Throwable t, Object... params) {

    String s = formatMessage(msg, params);

    if (logger != null) {
      switch (level) {
        case TRACE :
          if (logger.isTraceEnabled()) {
            if (t == null) {
              logger.trace(s);
            }
            else {
              logger.trace(s, t);
            }
          }
          break;
        case DEBUG :
          if (logger.isDebugEnabled()) {
            if (t == null) {
              logger.debug(s);
            }
            else {
              logger.debug(s, t);
            }
          }
          break;
        case INFO :
          if (logger.isInfoEnabled()) {
            if (t == null) {
              logger.info(s);
            }
            else {
              logger.info(s, t);
            }
          }
          break;
        case WARN :
          if (logger.isWarnEnabled()) {
            if (t == null) {
              logger.warn(s);
            }
            else {
              logger.warn(s, t);
            }
          }
          break;
        case ERROR :
          if (logger.isErrorEnabled()) {
            if (t == null) {
              logger.error(s);
            }
            else {
              logger.error(s, t);
            }
          }
          break;
        case FATAL :
          if (t == null) {
            logger.error(FATAL, s);
          }
          else {
            logger.error(FATAL, s, t);
          }
          break;
      }
    }

    return s;

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  // Trace ---------------------------------------------------------------------

  public String trace(String msg, Object... params) {
    return _log(Level.TRACE, msg, null, params);
  }

  public String trace(String msg, Throwable t, Object... params) {
    return _log(Level.TRACE, msg, t, params);
  }

  public String trace(IKey key, Object... params) {
    return _log(Level.TRACE, key, null, params);
  }

  public String trace(IKey key, Throwable t, Object... params) {
    return _log(Level.TRACE, key, t, params);
  }


  // Debug ---------------------------------------------------------------------

  public String debug(String msg, Object... params) {
    return _log(Level.DEBUG, msg, null, params);
  }

  public String debug(String msg, Throwable t, Object... params) {
    return _log(Level.DEBUG, msg, t, params);
  }

  public String debug(IKey key, Object... params) {
    return _log(Level.DEBUG, key, null, params);
  }

  public String debug(IKey key, Throwable t, Object... params) {
    return _log(Level.DEBUG, key, t, params);
  }


  // Info ----------------------------------------------------------------------

  public String info(String msg, Object... params) {
    return _log(Level.INFO, msg, null, params);
  }

  public String info(String msg, Throwable t, Object... params) {
    return _log(Level.INFO, msg, t, params);
  }

  public String info(IKey key, Object... params) {
    return _log(Level.INFO, key, null, params);
  }

  public String info(IKey key, Throwable t, Object... params) {
    return _log(Level.INFO, key, t, params);
  }


  // Warn ----------------------------------------------------------------------

  public String warn(String msg, Object... params) {
    return _log(Level.WARN, msg, null, params);
  }

  public String warn(String msg, Throwable t, Object... params) {
    return _log(Level.WARN, msg, t, params);
  }

  public String warn(IKey key, Object... params) {
    return _log(Level.WARN, key, null, params);
  }

  public String warn(IKey key, Throwable t, Object... params) {
    return _log(Level.WARN, key, t, params);
  }


  // Error ---------------------------------------------------------------------

  public String error(String msg, Object... params) {
    return _log(Level.ERROR, msg, null, params);
  }

  public String error(String msg, Throwable t, Object... params) {
    return _log(Level.ERROR, msg, t, params);
  }

  public String error(IKey key, Object... params) {
    return _log(Level.ERROR, key, null, params);
  }

  public String error(IKey key, Throwable t, Object... params) {
    return _log(Level.ERROR, key, t, params);
  }


  // Fatal ---------------------------------------------------------------------

  public String fatal(String msg, Object... params) {
    return _log(Level.FATAL, msg, null, params);
  }

  public String fatal(String msg, Throwable t, Object... params) {
    return _log(Level.FATAL, msg, t, params);
  }

  public String fatal(IKey key, Object... params) {
    return _log(Level.FATAL, key, null, params);
  }

  public String fatal(IKey key, Throwable t, Object... params) {
    return _log(Level.FATAL, key, t, params);
  }


}