package awardcenter.gui.component;


import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.SwingConstants.HORIZONTAL;

import static util.misc.StringUtil.rPad;
import static util.resource.ResourceUtil.getIcon;
import static util.swing.ImageUtil.getImageIcon;
import static util.swing.SwingUtil.STYLE_B;
import static util.swing.SwingUtil.createPanel;
import static util.swing.SwingUtil.createSeparator;
import static util.swing.SwingUtil.formatHTML;

import static awardcenter.gui.AwardCenterApplication.IMG_SIZE_SMALL;
import static awardcenter.resources.Key.ICON_ADDED;
import static awardcenter.resources.Key.ICON_LOCK;
import static awardcenter.resources.Key.ICON_MULTI;
import static awardcenter.resources.Key.ICON_SECRET;
import static awardcenter.resources.Key.ICON_TICK;


import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;

import awardcenter.model.Award;


public class AwardCellRenderer implements ListCellRenderer<Award> {


  // —————————————————————————————————————————————————————————————— Constructors


  public AwardCellRenderer() {

    // award -------------------------------------------------------------------

    panel = createPanel(new BorderLayout(10, 0), createEmptyBorder(5, 5, 5, 5));

    image = new JLabel();
    panel.add(image, WEST);

    label = new JLabel();
    panel.add(label, CENTER);

    statusPanel = createPanel();
    statusPanel.setOpaque(false);
    panel.add(statusPanel, EAST);

    JPanel tmpPanel = createPanel();
    tmpPanel.setOpaque(false);
    statusPanel.add(tmpPanel);

    awardIsSecret = new JLabel();
    awardIsSecret.setIcon(getIcon(ICON_SECRET));
    tmpPanel.add(awardIsSecret);

    awardIsMulti = new JLabel();
    awardIsMulti.setIcon(getIcon(ICON_MULTI));
    tmpPanel.add(awardIsMulti);

    awardIsAdded = new JLabel();
    awardIsAdded.setIcon(getIcon(ICON_ADDED));
    tmpPanel.add(awardIsAdded);

    _value = new JLabel();
    statusPanel.add(_value);

    value = new JLabel();
    statusPanel.add(value);

    awardIsAchieved = new JCheckBox(getIcon(ICON_LOCK));
    awardIsAchieved.setSelectedIcon(getIcon(ICON_TICK));
    awardIsAchieved.setOpaque(false);
    statusPanel.add(awardIsAchieved);

    // separator ---------------------------------------------------------------

    panelSeparator = createPanel(new BorderLayout(10, 0),
                                 createEmptyBorder(5, 5, 5, 5));
    labelSeparator = new JLabel();
    panelSeparator.add(labelSeparator, CENTER);
    separator = createSeparator(HORIZONTAL, labelSeparator.getBackground(),
                                            labelSeparator.getForeground());
    panelSeparator.add(separator, SOUTH);

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  // award ---------------------------------------------------------------------

  private JPanel panel;

  private JPanel statusPanel;

  private JLabel image;

  private JLabel label;

  private JLabel _value;

  private JLabel value;

  private JLabel awardIsSecret;

  private JLabel awardIsMulti;

  private JLabel awardIsAdded;

  private JCheckBox awardIsAchieved;

  // separator -----------------------------------------------------------------

  private JPanel panelSeparator;

  private JLabel labelSeparator;

  private JSeparator separator;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public Component getListCellRendererComponent(JList<? extends Award> list, Award award, int index,
                                                boolean isSelected, boolean cellHasFocus)
  {
    if (award.isSeparator()) {
      labelSeparator.setText(formatHTML(award.getLabel(), STYLE_B));
      if (isSelected) {
        panelSeparator.setForeground(list.getSelectionForeground());
        panelSeparator.setBackground(list.getSelectionBackground());
        labelSeparator.setForeground(list.getSelectionForeground());
        labelSeparator.setBackground(list.getSelectionBackground());
        separator.setForeground(list.getSelectionForeground());
        separator.setBackground(list.getSelectionBackground());
      }
      else {
        panelSeparator.setForeground(list.getForeground());
        panelSeparator.setBackground(list.getBackground());
        labelSeparator.setForeground(list.getForeground());
        labelSeparator.setBackground(list.getBackground());
        separator.setForeground(list.getForeground());
        separator.setBackground(list.getBackground());
      }
      return panelSeparator;
    }
    else {
      label.setText(formatHTML(award.getLabel(), STYLE_B));
      int length = 3 - String.valueOf(award.getValue()).length();
      _value.setText(formatHTML(rPad("", '_', length), STYLE_B, 1));
      value.setText(formatHTML(award.getValue(), STYLE_B, 1));
      awardIsAchieved.setSelected(award.isAchieved());
      awardIsSecret.setVisible(award.isSecret());
      awardIsMulti.setVisible(award.isMulti());
      awardIsAdded.setVisible(award.isAdded());
      if (award.getBytes() != null) {
        image.setIcon(getImageIcon(image, award.getBytes(), IMG_SIZE_SMALL, IMG_SIZE_SMALL));
      }
      else {
        image.setIcon(getImageIcon(image, (ImageIcon)null, IMG_SIZE_SMALL, IMG_SIZE_SMALL));
      }
      if (isSelected) {
        panel.setForeground(list.getSelectionForeground());
        panel.setBackground(list.getSelectionBackground());
        label.setForeground(list.getSelectionForeground());
        label.setBackground(list.getSelectionBackground());
        _value.setForeground(list.getSelectionBackground());
        value.setForeground(list.getSelectionForeground());
        value.setBackground(list.getSelectionBackground());
        awardIsSecret.setForeground(list.getSelectionForeground());
        awardIsSecret.setBackground(list.getSelectionBackground());
        awardIsMulti.setForeground(list.getSelectionForeground());
        awardIsMulti.setBackground(list.getSelectionBackground());
        awardIsAdded.setForeground(list.getSelectionForeground());
        awardIsAdded.setBackground(list.getSelectionBackground());
      }
      else {
        panel.setForeground(list.getForeground());
        panel.setBackground(list.getBackground());
        label.setForeground(list.getForeground());
        label.setBackground(list.getBackground());
        _value.setForeground(list.getBackground());
        value.setForeground(list.getForeground());
        value.setBackground(list.getBackground());
        awardIsSecret.setForeground(list.getForeground());
        awardIsSecret.setBackground(list.getBackground());
        awardIsMulti.setForeground(list.getForeground());
        awardIsMulti.setBackground(list.getBackground());
        awardIsAdded.setForeground(list.getForeground());
        awardIsAdded.setBackground(list.getBackground());
      }
      return panel;
    }

  }


}