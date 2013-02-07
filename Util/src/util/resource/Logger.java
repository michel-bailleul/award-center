package util.resource;


import static org.apache.commons.logging.LogFactory.getFactory;

import static util.misc.StringUtil.formatMessage;
import static util.resource.ResourceUtil.getMsg;


import org.apache.commons.logging.Log;


public final class Logger {


  // ————————————————————————————————————————————————————————————————————— Enums


  private static enum Level { TRACE, DEBUG, INFO, WARN, ERROR, FATAL }


  // ———————————————————————————————————————————————————————————— Static Methods


  public static Logger getLogger(Class<?> klass) {
    return new Logger(klass);
  }


  // —————————————————————————————————————————————————————————————— Constructors


  private Logger(Class<?> klass) {
    logger = getFactory().getInstance(klass);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private Log logger;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _log(Level level, IKey key, Throwable t, Object... params) {
    _log(level, getMsg(key), t, params);
  }


  private void _log(Level level, String msg, Throwable t, Object... params) {

    String message = formatMessage(msg, params);

    if (logger != null) {
      switch (level) {
        case TRACE :
          if (logger.isTraceEnabled()) {
            if (t == null) {
              logger.trace(message);
            }
            else {
              logger.trace(message, t);
            }
          }
          break;
        case DEBUG :
          if (logger.isDebugEnabled()) {
            if (t == null) {
              logger.debug(message);
            }
            else {
              logger.debug(message, t);
            }
          }
          break;
        case INFO :
          if (logger.isInfoEnabled()) {
            if (t == null) {
              logger.info(message);
            }
            else {
              logger.info(message, t);
            }
          }
          break;
        case WARN :
          if (logger.isWarnEnabled()) {
            if (t == null) {
              logger.warn(message);
            }
            else {
              logger.warn(message, t);
            }
          }
          break;
        case ERROR :
          if (logger.isErrorEnabled()) {
            if (t == null) {
              logger.error(message);
            }
            else {
              logger.error(message, t);
            }
          }
          break;
        case FATAL :
          if (logger.isFatalEnabled()) {
            if (t == null) {
              logger.fatal(message);
            }
            else {
              logger.fatal(message, t);
            }
          }
          break;
      }
    }

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  // Trace ---------------------------------------------------------------------

  public void trace(String msg, Object... params) {
    _log(Level.TRACE, msg, null, params);
  }

  public void trace(String msg, Throwable t, Object... params) {
    _log(Level.TRACE, msg, t, params);
  }

  public void trace(IKey key, Object... params) {
    _log(Level.TRACE, key, null, params);
  }

  public void trace(IKey key, Throwable t, Object... params) {
    _log(Level.TRACE, key, t, params);
  }


  // Debug ---------------------------------------------------------------------

  public void debug(String msg, Object... params) {
    _log(Level.DEBUG, msg, null, params);
  }

  public void debug(String msg, Throwable t, Object... params) {
    _log(Level.DEBUG, msg, t, params);
  }

  public void debug(IKey key, Object... params) {
    _log(Level.DEBUG, key, null, params);
  }

  public void debug(IKey key, Throwable t, Object... params) {
    _log(Level.DEBUG, key, t, params);
  }


  // Info ----------------------------------------------------------------------

  public void info(String msg, Object... params) {
    _log(Level.INFO, msg, null, params);
  }

  public void info(String msg, Throwable t, Object... params) {
    _log(Level.INFO, msg, t, params);
  }

  public void info(IKey key, Object... params) {
    _log(Level.INFO, key, null, params);
  }

  public void info(IKey key, Throwable t, Object... params) {
    _log(Level.INFO, key, t, params);
  }


  // Warn ----------------------------------------------------------------------

  public void warn(String msg, Object... params) {
    _log(Level.WARN, msg, null, params);
  }

  public void warn(String msg, Throwable t, Object... params) {
    _log(Level.WARN, msg, t, params);
  }

  public void warn(IKey key, Object... params) {
    _log(Level.WARN, key, null, params);
  }

  public void warn(IKey key, Throwable t, Object... params) {
    _log(Level.WARN, key, t, params);
  }


  // Error ---------------------------------------------------------------------

  public void error(String msg, Object... params) {
    _log(Level.ERROR, msg, null, params);
  }

  public void error(String msg, Throwable t, Object... params) {
    _log(Level.ERROR, msg, t, params);
  }

  public void error(IKey key, Object... params) {
    _log(Level.ERROR, key, null, params);
  }

  public void error(IKey key, Throwable t, Object... params) {
    _log(Level.ERROR, key, t, params);
  }


  // Fatal ---------------------------------------------------------------------

  public void fatal(String msg, Object... params) {
    _log(Level.FATAL, msg, null, params);
  }

  public void fatal(String msg, Throwable t, Object... params) {
    _log(Level.FATAL, msg, t, params);
  }

  public void fatal(IKey key, Object... params) {
    _log(Level.FATAL, key, null, params);
  }

  public void fatal(IKey key, Throwable t, Object... params) {
    _log(Level.FATAL, key, t, params);
  }


}