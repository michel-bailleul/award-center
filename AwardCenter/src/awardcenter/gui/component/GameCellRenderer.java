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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import awardcenter.model.Game;


public class GameCellRenderer implements ListCellRenderer<Game> {


  // —————————————————————————————————————————————————————————————— Constructors


  public GameCellRenderer() {

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
  public Component getListCellRendererComponent(JList<? extends Game> list, Game game, int index,
                                                boolean isSelected, boolean cellHasFocus)
  {
    label.setText(formatHTML(game.getName(), STYLE_B));
    if (game.getBytes() != null) {
      image.setIcon(getImageIcon(image, game.getBytes(), IMG_SIZE_SMALL, IMG_SIZE_SMALL));
    }
    else {
      image.setIcon(getImageIcon(image, (ImageIcon)null, IMG_SIZE_SMALL, IMG_SIZE_SMALL));
    }
    if (isSelected) {
      panel.setForeground(list.getSelectionForeground());
      panel.setBackground(list.getSelectionBackground());
      label.setForeground(list.getSelectionForeground());
      label.setBackground(list.getSelectionBackground());
    }
    else {
      panel.setForeground(list.getForeground());
      panel.setBackground(list.getBackground());
      label.setForeground(list.getForeground());
      label.setBackground(list.getBackground());
    }
    return panel;
  }


}