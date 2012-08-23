package awardcenter.gui.component;


import static java.awt.FlowLayout.LEADING;

import static util.swing.ImageUtil.getImageIcon;


import static util.swing.SwingUtil.STYLE_B;
import static util.swing.SwingUtil.createPanel;
import static util.swing.SwingUtil.formatHTML;

import static awardcenter.gui.AwardCenterApplication.IMG_SIZE_SMALL;


import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import awardcenter.model.Game;


public class ImportExportCellRenderer implements TableCellRenderer {


  // —————————————————————————————————————————————————————————————— Constructors


  public ImportExportCellRenderer() {

    panel = createPanel();
    panel.setLayout(new FlowLayout(LEADING));

    image = new JLabel();
    panel.add(image);

    label = new JLabel();
    panel.add(label);

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private JPanel panel;

  private JLabel label;

  private JLabel image;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public Component getTableCellRendererComponent(JTable table, Object o,
                                                 boolean isSelected, boolean hasFocus,
                                                 int row, int column)
  {
    Game game = (Game)o;
    label.setText(formatHTML(game.getName(), STYLE_B));

    if (game.getBytes() != null) {
      image.setIcon(getImageIcon(image, game.getBytes(), IMG_SIZE_SMALL, IMG_SIZE_SMALL));
    }
    else {
      image.setIcon(getImageIcon(image, (ImageIcon)null, IMG_SIZE_SMALL, IMG_SIZE_SMALL));
    }

    if (isSelected) {
      panel.setForeground(table.getSelectionForeground());
      panel.setBackground(table.getSelectionBackground());
      label.setForeground(table.getSelectionForeground());
      label.setBackground(table.getSelectionBackground());
    }
    else {
      panel.setForeground(table.getForeground());
      panel.setBackground(table.getBackground());
      label.setForeground(table.getForeground());
      label.setBackground(table.getBackground());
    }

    return panel;
  }


  public int getHeight(JTable table, Game o) {
    getTableCellRendererComponent(table, o, false, false, 0, 0);
    return (int) panel.getPreferredSize().getHeight();
  }


}