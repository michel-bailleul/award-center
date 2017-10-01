package util.swing.component;


import static java.awt.Cursor.DEFAULT_CURSOR;
import static java.awt.Cursor.getPredefinedCursor;

import static javax.swing.BorderFactory.createEmptyBorder;


import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTextField;


public class JTextFieldCheckBox extends JTextField {


  // ————————————————————————————————————————————————————————————————————— Enums


  public static enum Side { LEFT, RIGHT }


  // —————————————————————————————————————————————————————————————— Constructors


  public JTextFieldCheckBox() {
    this(0, null, null, null);
  }


  public JTextFieldCheckBox(Icon defaultIcon) {
    this(0, defaultIcon, null, null);
  }


  public JTextFieldCheckBox(Icon defaultIcon, Side side) {
    this(0, defaultIcon, null, side);
  }


  public JTextFieldCheckBox(Icon defaultIcon, Icon selectedIcon) {
    this(0, defaultIcon, selectedIcon, null);
  }


  public JTextFieldCheckBox(Icon defaultIcon, Icon selectedIcon, Side side) {
    this(0, defaultIcon, selectedIcon, side);
  }


  public JTextFieldCheckBox(int col) {
    this(col, null, null, null);
  }


  public JTextFieldCheckBox(int col, Icon defaultIcon) {
    this(col, defaultIcon, null, null);
  }


  public JTextFieldCheckBox(int col, Icon defaultIcon, Icon selectedIcon) {
    this(col, defaultIcon, selectedIcon, null);
  }


  public JTextFieldCheckBox(int col, Icon defaultIcon, Icon selectedIcon, Side side) {

    super(col);
    this.defaultIcon = defaultIcon;
    this.selectedIcon = selectedIcon;
    this.side = (side != null) ? side : Side.LEFT;

    cb = new JCheckBox(defaultIcon);
    cb.setOpaque(false);
    cb.setCursor(getPredefinedCursor(DEFAULT_CURSOR));
    cb.setSelectedIcon(selectedIcon);

    setLayout(null);
    add(cb);

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private JCheckBox cb;

  private Icon defaultIcon;

  private Icon selectedIcon;

  private Side side;

  private Insets m = null;


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    if (defaultIcon != null) {
      int w = defaultIcon.getIconWidth();
      int h = defaultIcon.getIconHeight();
      int y = (getHeight() - h) / 2;
      int x = (side == Side.LEFT) ? 0 : getWidth() - w - 2*y;
      cb.setBounds(x, y, w + 2*y, h);
      if (m == null) {
        cb.setBorder(createEmptyBorder(0, y, 0, y));
        m = (side == Side.LEFT) ? new Insets(y, w + 2*y, y, y) :
                                  new Insets(y, y, y, w + 2*y);
        setMargin(m);
      }
    }

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public Icon getDefaultIcon() {
    return defaultIcon;
  }

  public void setDefaultIcon(Icon icon) {
    this.defaultIcon = icon;
    cb.setIcon(defaultIcon);
  }


  public Icon getSelectedIcon() {
    return selectedIcon;
  }

  public void setSelectedIcon(Icon icon) {
    this.selectedIcon = icon;
    cb.setSelectedIcon(selectedIcon);
  }


  public Side getSide() {
    return side;
  }

  public void setSide(Side side) {
    this.side = side;
  }


  public void setCheckBoxSelected(boolean isSelected) {
    cb.setSelected(isSelected);
  }


  public void setCheckBoxEnabled(boolean isEnabled) {
    cb.setEnabled(isEnabled);
  }


  public void setCheckBoxCursor(Cursor cursor) {
    cb.setCursor(cursor);
  }


  public void setCheckBoxToolTip(String txt) {
    cb.setToolTipText(txt);
  }


  public void addCheckBoxActionListener(ActionListener listener) {
    cb.addActionListener(listener);
  }


}