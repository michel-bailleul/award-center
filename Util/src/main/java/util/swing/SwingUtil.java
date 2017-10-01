package util.swing;


import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;


public final class SwingUtil {


  // —————————————————————————————————————————————————————————— Static Constants


//  private static final Logger logger = Logger.getLogger(SwingUtil.class);


  public static final int STYLE_B = 1;
  public static final int STYLE_I = STYLE_B << 1;
  public static final int STYLE_U = STYLE_I << 1;
  public static final int STYLE_BI = STYLE_B + STYLE_I;
  public static final int STYLE_BU = STYLE_B + STYLE_U;
  public static final int STYLE_IU = STYLE_I + STYLE_U;
  public static final int STYLE_BIU = STYLE_B + STYLE_I + STYLE_U;

//  private static final String FACE_01 = "Arial, Helvetica, Sans-Serif";
//  private static final String FACE_02 = "Courier New, Courier, Mono";
//  private static final String FACE_03 = "Alpha Geometrique, Critter, Cottonwood, Fantasy";
//  private static final String FACE_04 = "Courier, MS Courier New, Prestige, Monospace";

  private static final String HTML_O = "<html>";
  private static final String HTML_C = "</html>";
  private static final String FONT_O = "<font size='value'>";
  private static final String FONT_C = "</font>";
  private static final String B_O = "<b>";
  private static final String B_C = "</b>";
  private static final String I_O = "<i>";
  private static final String I_C = "</i>";
  private static final String U_O = "<u>";
  private static final String U_C = "</u>";


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * Formate un texte en HTML
   *
   * @param text  - Texte a formater
   * @param style - Le style HTML [0 -> aucun style]
   * @return Le texte au format HTML
   */
  public static String formatHTML(Object text, int style) {
    return formatHTML(text, style, 0);
  }


  /**
   * Formate un texte en HTML
   *
   * @param text  - Texte a formater
   * @param style - Le style HTML [0 -> aucun style]
   * @param size  - Taille relative du texte [ex: +2 ou -2]
   * @return Le texte au format HTML
   */
  public static String formatHTML(Object text, int style, int size) {

    StringBuilder sb = new StringBuilder();

    sb.append(HTML_O);
    sb.append((style & STYLE_B) > 0 ? B_O : "");
    sb.append((style & STYLE_I) > 0 ? I_O : "");
    sb.append((style & STYLE_U) > 0 ? U_O : "");
    if (size == 0) {
      sb.append(text);
    }
    else {
      sb.append(FONT_O.replaceAll("value", (size > 0 ? "+" : "") + size));
      sb.append(text);
      sb.append(FONT_C);
    }
    sb.append((style & STYLE_U) > 0 ? U_C : "");
    sb.append((style & STYLE_I) > 0 ? I_C : "");
    sb.append((style & STYLE_B) > 0 ? B_C : "");
    sb.append(HTML_C);

    return sb.toString();

  }


  public static JPanel createPanel() {
    return new JPanel();
  }


  public static JPanel createPanel(Color color) {

    JPanel panel = createPanel();

    if (color != null) {
      panel.setBackground(color);
    }

    return panel;

  }


  public static JPanel createPanel(LayoutManager lm) {

    JPanel panel = createPanel();

    if (lm != null) {
      panel.setLayout(lm);
    }

    return panel;

  }


  public static JPanel createPanel(Border border) {

    JPanel panel = createPanel();

    if (border != null) {
      panel.setBorder(border);
    }

    return panel;

  }


  public static JPanel createPanel(Color color, LayoutManager lm) {

    JPanel panel = createPanel(color);

    if (lm != null) {
      panel.setLayout(lm);
    }

    return panel;

  }


  public static JPanel createPanel(Color color, Border border) {

    JPanel panel = createPanel(color);

    if (border != null) {
      panel.setBorder(border);
    }

    return panel;

  }


  public static JPanel createPanel(LayoutManager lm, Border border) {

    JPanel panel = createPanel(lm);

    if (border != null) {
      panel.setBorder(border);
    }

    return panel;

  }


  public static JPanel createPanel(Color color, LayoutManager lm, Border border) {

    JPanel panel = createPanel(color);

    if (lm != null) {
      panel.setLayout(lm);
    }
    if (border != null) {
      panel.setBorder(border);
    }

    return panel;

  }


  public static JLabel createLabel(Color color) {

    JLabel label = new JLabel();

    if (color != null) {
      label.setForeground(color);
    }

    return label;

  }


  public static JSeparator createSeparator(int orientation, Color background, Color foreground) {

    JSeparator separator = new JSeparator(orientation);
    separator.setBackground(background);
    separator.setForeground(foreground);

    return separator;

  }


  public static JMenu createMenu(String text) {

    JMenu menu = new JMenu();
    menu.setText(text);

    return menu;

  }


  public static JMenu addMenu(JMenuBar menuBar, String text) {

    JMenu menu = createMenu(text);
    menuBar.add(menu);

    return menu;

  }


  public static JMenuItem createMenuItem(String text) {
    JMenuItem menuItem = new JMenuItem(text);
    return menuItem;
  }


  public static JMenuItem createMenuItem(String text, Icon icon) {
    JMenuItem menuItem = new JMenuItem(text, icon);
    return menuItem;
  }


  public static JMenuItem addMenuItem(JMenu menu, String text) {
    JMenuItem menuItem = createMenuItem(text);
    menu.add(menuItem);
    return menuItem;
  }


  public static JMenuItem addMenuItem(JMenu menu, String text, Icon icon) {
    JMenuItem menuItem = createMenuItem(text, icon);
    menu.add(menuItem);
    return menuItem;
  }


  public static JMenuItem createMenuItemCheck(String text) {
    JMenuItem menuItem = new JCheckBoxMenuItem(text);
    return menuItem;
  }


  public static JMenuItem createMenuItemCheck(String text, Icon icon) {
    JMenuItem menuItem = new JCheckBoxMenuItem(text, icon);
    return menuItem;
  }


  public static JMenuItem addMenuItemCheck(JMenu menu, String text) {
    JMenuItem menuItem = createMenuItemCheck(text);
    menu.add(menuItem);
    return menuItem;
  }


  public static JMenuItem addMenuItemCheck(JMenu menu, String text, Icon icon) {
    JMenuItem menuItem = createMenuItemCheck(text, icon);
    menu.add(menuItem);
    return menuItem;
  }


  // —————————————————————————————————————————————————————————————— Constructors


  /** Don't let anyone instantiate this klass */
  private SwingUtil() { }


}