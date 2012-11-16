package util.misc;


import java.text.MessageFormat;
import java.text.Normalizer;

import util.collection.ArrayUtil;


public final class StringUtil {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(StringUtil.class);

  /** Non-breaking space */
  public static final char NBSP = '\u00a0';


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Verifie si le texte est vide ou {@code null}
   *
   * @param text - Le texte a verifier
   * @return {@code true} si le texte est vide ou {@code null}<br/>
   *         {@code false} si le texte n'est pas vide
   * @see #isEmptyTrim(String)
   */
  public static boolean isEmpty(String text) {
    return text == null || text.length() == 0;
  }


  /**
   * Similaire a la methode {@code isEmpty(String)}
   * avec un {@code trim()} en plus
   *
   * @param text - Le texte a verifier
   * @return {@code true} si le texte est vide ou {@code null}<br/>
   *         {@code false} si le texte n'est pas vide
   * @see #isEmpty(String)
   */
  public static boolean isEmptyTrim(String text) {
    return text == null || text.trim().length() == 0;
  }


  /**
   * Verifie le contenu d'un tableau de {@code String}
   *
   * @param text - Le tableau de {@code String} a verifier
   * @return {@code true} si le tableau ne contient pas de texte<br/>
   *         {@code false} si le tableau contient du texte
   * @see #isEmptyTrim(String[])
   */
  public static boolean isEmpty(String[] text) {

    if (text != null) {
      for (int i = 0; i < text.length; i++) {
        if (!isEmpty(text[i])) {
          return false;
        }
      }
    }

    return true;

  }


  /**
   * Similaire a la methode {@code isEmpty(String[])}
   * avec un {@code trim()} en plus
   *
   * @param text - Le tableau de {@code String} a verifier
   * @return {@code true} si le tableau ne contient pas de texte<br/>
   *         {@code false} si le tableau contient du texte
   * @see #isEmpty(String[])
   */
  public static boolean isEmptyTrim(String[] text) {

    if (text != null) {
      for (int i = 0; i < text.length; i++) {
        if (!isEmptyTrim(text[i])) {
          return false;
        }
      }
    }

    return true;

  }


  /**
   * Complete a gauche avec un caractere a une longueur fixe
   *
   * @param text   - Le texte a modifier
   * @param c      - Le caractere de a ajouter
   * @param length - La longueur souhaitee
   * @return Le texte apres modification
   */
  public static String lPad(String text, char c, int length) {

    int l = 0;

    if (text != null) {
      l = text.length();
      if (!(l < length)) {
        return text; // chaine trop longue -> pas de padding
      }
    }

    StringBuffer sb = new StringBuffer(length);

    for (int i = 0; i < length - l; i++) {
      sb.append(c);
    }

    return sb.append(l > 0 ? text : "").toString();

  }


  /**
   * Complete a gauche avec des espaces a une longueur fixe.<br/>
   * Similaire a la methode {@code lPad(String, char, int)}
   * avec ' ' pour le caractere
   *
   * @param text   - Le texte a modifier
   * @param length - La longueur souhaitee
   * @return Le texte apres modification
   */
  public static String lPad(String text, int length) {
    return lPad(text, ' ', length);
  }


  /**
   * Complete a droite avec un caractere a une longueur fixe
   *
   * @param text   - Le texte a modifier
   * @param c      - Le caractere de a ajouter
   * @param length - La longueur souhaitee
   * @return Le texte apres modification
   */
  public static String rPad(String text, char c, int length) {

    int l = 0;

    if (text != null) {
      l = text.length();
      if (!(l < length)) {
        return text; // chaine trop longue -> pas de padding
      }
    }

    StringBuffer sb = new StringBuffer(length);

    sb.append(l > 0 ? text : "");

    for (int i = 0; i < length - l; i++) {
      sb.append(c);
    }

    return sb.toString();

  }


  /**
   * Complete a droite avec des espaces a une longueur fixe.<br/>
   * Similaire a la methode {@code rPad(String, char, int)}
   * avec ' ' pour le caractere
   *
   * @param text   - Le texte a modifier
   * @param length - La longueur souhaitee
   * @return Le texte apres modification
   */
  public static String rPad(String text, int length) {
    return rPad(text, ' ', length);
  }


  /**
   * Convertit les espaces ordinaires d'un texte en espaces insecables.<br/>
   * nbsp : Non-Breaking SPace
   *
   * @param text - le texte a modifier
   * @return Le texte avec tous ses espaces insecables
   */
  public static String nbsp(String text) {

    if (isEmpty(text)) {
      return text;
    }
    else {
      return text.replace(' ', NBSP);
    }

  }


  /**
   * Formate un texte avec une liste de parametres
   *
   * @param msg    - le texte a formater
   * @param params - liste de parametres [facultatif]
   *
   * @return le texte apres formatage
   *
   * @see String#format(String, Object...)
   */
  public static String formatString(String msg, Object... params) {
    return ArrayUtil.isEmpty(params) ? msg : String.format(msg, params);
  }


  /**
   * Formate un texte avec une liste de parametres
   *
   * @param msg    - le texte a formater
   * @param params - liste de parametres [facultatif]
   *
   * @return le texte apres formatage
   *
   * @see MessageFormat#format(String, Object...)
   */
  public static String formatMessage(String msg, Object... params) {
    return ArrayUtil.isEmpty(params) || isEmpty(msg) ? msg : MessageFormat.format(msg.replace("'", "''"), params);
  }


  public static String normalizeASCII(String text) {
    return Normalizer.normalize(text, Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]", "");
//    return Normalizer.normalize(text, Normalizer.Form.NFKD).replaceAll("[\u0300-\u036F]", "");
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this fucking klass ;o) */
  private StringUtil() { }


}