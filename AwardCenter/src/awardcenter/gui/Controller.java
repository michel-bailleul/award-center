package awardcenter.gui;


import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.BorderLayout.WEST;
import static java.awt.Color.BLACK;
import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.GRAY;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.Cursor.getDefaultCursor;
import static java.awt.Cursor.getPredefinedCursor;
import static java.awt.FlowLayout.LEADING;
import static java.awt.FlowLayout.RIGHT;
import static java.awt.Font.DIALOG;
import static java.awt.Font.getFont;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.HORIZONTAL;
import static javax.swing.SwingConstants.VERTICAL;

import static util.io.FileUtil.getBytes;
import static util.misc.StringUtil.NBSP;
import static util.misc.StringUtil.count;
import static util.misc.StringUtil.isEmpty;
import static util.misc.StringUtil.rPad;
import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getIcon;
import static util.resource.ResourceUtil.getMsg;
import static util.swing.SwingUtil.*;
import static util.swing.ImageUtil.getImage;
import static util.swing.app.ActionType.ABOUT;
import static util.swing.app.ActionType.EXIT;
import static util.swing.app.ActionType.SAVE;

import static awardcenter.gui.AwardCenterActionType.*;
import static awardcenter.gui.AwardCenterApplication.GAP;
import static awardcenter.gui.AwardCenterApplication.IMG_SIZE_LARGE;
import static awardcenter.resources.Key.*;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import util.swing.component.JTextFieldSearch;
import util.swing.component.ImageViewer;
import util.swing.component.GradientPanel;
import util.swing.app.ListModel;
import util.collection.IFilter;
import util.resource.Logger;

import awardcenter.gui.component.AwardCellRenderer;
import awardcenter.gui.component.GameCellRenderer;
import awardcenter.gui.dialog.ImportExportDialog;
import awardcenter.model.Award;
import awardcenter.model.AwardModel;
import awardcenter.model.Game;
import awardcenter.resources.Key;


public class Controller {


  // ————————————————————————————————————————————————————————————————————— Enums


  private static enum Move { FIRST, PREVIOUS, NEXT, LAST }


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(Controller.class);


  private static final Color COLOR_GAME_INFO_F = LIGHT_GRAY;

  private static final Icon STAR             = getIcon(ICON_STAR);
  private static final Icon STAR_EMPTY       = getIcon(ICON_STAR_EMPTY);
  private static final Icon STAR_SMALL       = getIcon(ICON_STAR_SMALL);
  private static final Icon STAR_SMALL_EMPTY = getIcon(ICON_STAR_SMALL_EMPTY);

  private static final String CARD_AWARD     = "0";
  private static final String CARD_SEPARATOR = "1";


  // —————————————————————————————————————————————————————————————— Constructors


  public Controller(AwardCenterApplication app, JFrame frame) {

    this.app = app;
    this.frame = frame;

    // Menu Bar --------------------

    createMenuBar();

    // Body --------------------

    JPanel contentPane = createPanel(new BorderLayout(GAP, GAP),
                                     createEmptyBorder(GAP, GAP, GAP, GAP));
    frame.setContentPane(contentPane);

    JPanel panel = null;

    // Game --------------------

    createGameInfo(contentPane);
    panel = createGameList(contentPane);
    createGameFilter(panel); // SOUTH

    // Award --------------------

    panel = createAwardList(contentPane);
    createAwardInfo(panel);   // NORTH
    createAwardFilter(panel); // SOUTH

    // Model --------------------

    createModel();

    frame.pack();

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private AwardCenterApplication app;

  private JFrame frame;

  private AwardModel model;


  // Game Components -----------------------------------------------------------

  private boolean isGameRefresh = false;

  // menu

  private JMenuItem menuItemGameInsert;

  private JMenuItem menuItemGameUpdate;

  private JMenuItem menuItemGameDelete;

  // list

  private JList<Game> gameList;

  private ListModel<Game> gameListModel;

  // filter

  private JPanel gameFilterPanel;

  private TitledBorder gameFilterTitle;

  private JTextFieldSearch<Game> gameFilterTxt;

  // detail

  private ImageViewer gameImagePanel;

  private JLabel gameName;

  private JLabel gameDeveloper;

  private JLabel gamePublisher;

  private JLabel gameScore;

  private JLabel gameScoreMax;

  private JCheckBox[] stars = new JCheckBox[10];


  // Award Components ----------------------------------------------------------

  private boolean isAwardRefresh = false;

  // menu

  private JMenuItem menuItemAwardInsert;

  private JMenuItem menuItemAwardDelete;

  private JMenuItem menuItemSeparator;

  // list

  private JList<Award> awardList;

  private ListModel<Award> awardListModel;

  // filter

  private boolean isOkFilterSelected;

  private JPanel awardFilterPanel;

  private TitledBorder awardFilterTitle;

  private JTextFieldSearch<Award> awardFilterTxt;

  private JToggleButton achievedOkFilter;

  private JToggleButton achievedKoFilter;

  private JToggleButton secretFilter;

  private JToggleButton multiFilter;

  private JToggleButton addedFilter;

  private IFilter<Award> awardFilter;

  // detail

  private ImageViewer awardImagePanel;

  private JTextField awardLabel;

  private JTextArea awardDescription;

  private JTextArea awardTipsAndTricks;

  private JLabel _awardValue;

  private JLabel awardValue;

  private JCheckBox awardIsAchieved;

  private JCheckBox awardIsSecret;

  private JCheckBox awardIsMulti;

  private JCheckBox awardIsAdded;

  private JButton plus;

  private JButton minus;

  private JButton first;

  private JButton previous;

  private JButton next;

  private JButton last;

  private JPanel awardCards;

  private Component awardGlue;


  // —————————————————————————————————————————————————————————— Abstract Methods

  // ——————————————————————————————————————————————————————————— Private Methods


  private void createMenuBar() {

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    // Menu "File"
    JMenu menuFile = addMenu(menuBar, getMsg(MENU_FILE));
    // Import
    JMenuItem menuItemImport = addMenuItem(menuFile, getMsg(MENU_IMPORT), getIcon(ICON_IMPORT));
    menuItemImport.addActionListener(app.getActionListener(IMPORT));
    // Export
    JMenuItem menuItemExport = addMenuItem(menuFile, getMsg(MENU_EXPORT), getIcon(ICON_EXPORT));
    menuItemExport.addActionListener(app.getActionListener(EXPORT));
    // Save
    JMenuItem menuItemSave = addMenuItem(menuFile, getMsg(MENU_SAVE), getIcon(ICON_SAVE));
    menuItemSave.addActionListener(app.getActionListener(SAVE));
    menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));

    // Exit
    menuFile.addSeparator();
    JMenuItem menuItemExit = addMenuItem(menuFile, getMsg(MENU_EXIT), getIcon(ICON_EXIT));
    menuItemExit.addActionListener(app.getActionListener(EXIT));

    // Menu "Game"
    JMenu menuGame = addMenu(menuBar, getMsg(MENU_GAME));
    // Insert Game
    menuItemGameInsert = addMenuItem(menuGame, getMsg(MENU_GAME_INSERT), getIcon(ICON_ADD));
    menuItemGameInsert.addActionListener(app.getActionListener(GAME_INSERT));
    // Delete Game
    menuItemGameDelete = addMenuItem(menuGame, getMsg(MENU_GAME_DELETE), getIcon(ICON_DELETE));
    menuItemGameDelete.addActionListener(app.getActionListener(GAME_DELETE));
    // Update Game
    menuGame.addSeparator();
    menuItemGameUpdate = addMenuItemCheck(menuGame, getMsg(MENU_GAME_UPDATE), getIcon(ICON_EDIT));
    menuItemGameUpdate.addActionListener(app.getActionListener(GAME_UPDATE));

    // Menu "Award"
    JMenu menuAward = addMenu(menuBar, getMsg(MENU_AWARD));
    // Insert Award
    menuItemAwardInsert = addMenuItem(menuAward, getMsg(MENU_AWARD_INSERT), getIcon(ICON_ADD));
    menuItemAwardInsert.addActionListener(app.getActionListener(AWARD_INSERT));
    // Add Separator
    menuItemSeparator = addMenuItem(menuAward, getMsg(MENU_AWARD_SEPARATOR), getIcon(ICON_SPLIT));
    menuItemSeparator.addActionListener(app.getActionListener(AWARD_SEPARATOR));
    // Delete Award
    menuItemAwardDelete = addMenuItem(menuAward, getMsg(MENU_AWARD_DELETE), getIcon(ICON_DELETE));
    menuItemAwardDelete.addActionListener(app.getActionListener(AWARD_DELETE));

    // Menu "?"
    JMenu menuHelp = addMenu(menuBar, getMsg(MENU_HELP));
    JMenuItem menuItemAbout = addMenuItem(menuHelp, getMsg(MENU_HELP_ABOUT));
    menuItemAbout.setIcon(getIcon(ICON_ABOUT));
    menuItemAbout.addActionListener(app.getActionListener(ABOUT));

  }


  private JPanel createGameInfo(JPanel contentPane) {

    JPanel gradientPanel = new GradientPanel(BLACK, DARK_GRAY, 10, 40);
    gradientPanel.setLayout(new BorderLayout());
    contentPane.add(gradientPanel, NORTH);

    JPanel panel = createPanel(new BorderLayout(GAP, GAP), createEmptyBorder(GAP, GAP, GAP, GAP));
    panel.setOpaque(false);
    gradientPanel.add(panel);

    // Image
    JPanel imagePane = createPanel();
    imagePane.setOpaque(false);
    imagePane.setLayout(new BoxLayout(imagePane, X_AXIS));
    gameImagePanel = new ImageViewer(IMG_SIZE_LARGE, IMG_SIZE_LARGE, 1, COLOR_GAME_INFO_F);
    gameImagePanel.setImageDir(app.getImageDir());
    gameImagePanel.setEditable(false);
    gameImagePanel.addPropertyChangeListener("imageFile",
      new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if (evt.getNewValue() != null) {
            File file = (File)evt.getNewValue();
            gameListModel.getSelection().setBytes(getBytes(file));
          }
        }
      }
    );
    imagePane.add(gameImagePanel);
    panel.add(imagePane, WEST);

    // Game Info
    JPanel tmpPane;
    JPanel gamePane = createPanel();
    gamePane.setOpaque(false);
    gamePane.setLayout(new BoxLayout(gamePane, Y_AXIS));
    panel.add(gamePane, CENTER);

    // Name
    tmpPane = createPanel(new FlowLayout(LEADING, 0, 0));
    tmpPane.setOpaque(false);
    gameName = createLabel(COLOR_GAME_INFO_F);
    tmpPane.add(gameName);
    gamePane.add(tmpPane);

    // Stars -

    JPanel starPane = createPanel(new FlowLayout(LEADING, 0, 2), createEmptyBorder());
    starPane.setOpaque(false);

    ActionListener actionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JCheckBox o = (JCheckBox)e.getSource();
        rating(o);
      }
    };

    for (int i=0; i < stars.length; i++) {
      JCheckBox star = new JCheckBox();
      star.addActionListener(actionListener);
      star.setName(String.valueOf(i));
      star.setOpaque(false);
      star.setBorder(createEmptyBorder());
      starPane.add(star);
      stars[i] = star;
      if (i < stars.length/2) {
        // small star
        star.setIcon(STAR_SMALL_EMPTY);
        star.setSelectedIcon(STAR_SMALL);
        star.setVerticalAlignment(BOTTOM);
      }
      else {
        star.setIcon(STAR_EMPTY);
        star.setSelectedIcon(STAR);
      }
    }

    gamePane.add(starPane);

    gamePane.add(createSeparator(HORIZONTAL, COLOR_GAME_INFO_F, GRAY));

    // Developer
    gameDeveloper = createLabel(COLOR_GAME_INFO_F);
    tmpPane = createPanel();
    tmpPane.setOpaque(false);
    tmpPane.setLayout(new FlowLayout(LEADING, 10, 0));
    JLabel tmpLabel = createLabel(COLOR_GAME_INFO_F);
    tmpLabel.setText(formatHTML(getMsg(GAME_DEVELOPER), STYLE_B));
    tmpPane.add(tmpLabel);
    tmpPane.add(gameDeveloper);
    gamePane.add(tmpPane);

    // Publisher
    gamePublisher = createLabel(COLOR_GAME_INFO_F);
    tmpPane = createPanel();
    tmpPane.setOpaque(false);
    tmpPane.setLayout(new FlowLayout(LEADING, 10, 0));
    tmpLabel = createLabel(COLOR_GAME_INFO_F);
    tmpLabel.setText(formatHTML(getMsg(GAME_PUBLISHER), STYLE_B));
    tmpPane.add(tmpLabel);
    tmpPane.add(gamePublisher);
    gamePane.add(tmpPane);

    // Score
    JPanel scorePane = createPanel();
    scorePane.setOpaque(false);
    scorePane.setLayout(new BoxLayout(scorePane, Y_AXIS));
    panel.add(scorePane, EAST);

    gameScore = createLabel(COLOR_GAME_INFO_F);
    gameScore.setHorizontalAlignment(SwingConstants.CENTER);
    scorePane.add(gameScore);
    scorePane.add(createSeparator(HORIZONTAL, COLOR_GAME_INFO_F, GRAY));
    gameScoreMax = createLabel(COLOR_GAME_INFO_F);
    gameScoreMax.setHorizontalAlignment(SwingConstants.CENTER);
    scorePane.add(gameScoreMax);

    return panel;

  }


  private JPanel createGameList(JPanel contentPane) {

    JPanel panel = createPanel(new BorderLayout(GAP, GAP));
    panel.setPreferredSize(new Dimension(300, 0));
    contentPane.add(panel, WEST);

    gameList = new JList<Game>();
    gameList.setSelectionMode(SINGLE_SELECTION);
    gameList.setCellRenderer(new GameCellRenderer());
    gameList.addListSelectionListener(
      new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          gameListModel.setSelection(gameList.getSelectedValue());
        }
      }
    );

    panel.add(new JScrollPane(gameList), CENTER);

    return panel;

  }


  private JPanel createGameFilter(JPanel contentPane) {

    gameFilterTitle = createTitledBorder(" ");
    gameFilterPanel = createPanel(new BorderLayout(), gameFilterTitle);
    gameFilterTxt = new JTextFieldSearch<Game>();
    final IFilter<Game> filter = gameFilterTxt.getFilter(); // default filter on name
    gameFilterTxt.setFilter(new IFilter<Game>() { // customized filter on rating
      @Override
      public boolean matches(Game obj) {
        if (!filter.matches(obj)) {
          int count = count(gameFilterTxt.getText(), '*');
          return (count > 0) && (obj.getRating() == count);
        }
        else {
          return true;
        }
      }
    });
    gameFilterTxt.setPostFilteringActionListener(app.getActionListener(GAME_FILTER));
    JPanel panel = createPanel(new BorderLayout(), createEmptyBorder(0, 5, 5, 5));
    panel.add(gameFilterTxt);
    gameFilterPanel.add(panel);
    contentPane.add(gameFilterPanel, SOUTH);

    return contentPane;

  }


  private JPanel createAwardInfo(JPanel contentPane) {

    DocumentListener ds = new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        edited(e);
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
        edited(e);
      }
      @Override
      public void changedUpdate(DocumentEvent e) {
      }
    };

    JPanel panel = createPanel(new BorderLayout(GAP, GAP), createEmptyBorder());
    contentPane.add(panel, NORTH);

    // Image
    JPanel imagePane = createPanel();
    imagePane.setLayout(new BoxLayout(imagePane, Y_AXIS));
    awardImagePanel = new ImageViewer(IMG_SIZE_LARGE, IMG_SIZE_LARGE, 1, BLACK);
    awardImagePanel.setImageDir(app.getImageDir());
    awardImagePanel.addPropertyChangeListener("imageFile",
      new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if (evt.getNewValue() != null) {
            File file = (File)evt.getNewValue();
            awardListModel.getSelection().setBytes(getBytes(file));
            awardListModel.fireSelectedChange();
          }
        }
      }
    );
    imagePane.add(awardImagePanel);
    panel.add(imagePane, WEST);

    awardIsAchieved = new JCheckBox(getIcon(ICON_LOCK));
    awardIsAchieved.setSelectedIcon(getIcon(ICON_TICK));
    awardIsAchieved.setCursor(getPredefinedCursor(HAND_CURSOR));
    awardIsAchieved.setAlignmentX(CENTER_ALIGNMENT);
    awardIsAchieved.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        switchCheckBox(awardIsAchieved);
      }
    });
    awardGlue = Box.createVerticalGlue();
    imagePane.add(awardGlue);
    imagePane.add(awardIsAchieved);
    imagePane.add(Box.createVerticalGlue());

    JToolBar moveTB = new JToolBar();
    moveTB.setFloatable(false);
    moveTB.setRollover(true);
    imagePane.add(moveTB);

    first = new JButton(getIcon(ICON_FIRST));
    first.setToolTipText(getMsg(AWARD_FIRST));
    first.setPreferredSize(new Dimension(16, 16));
    first.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifyIndex(Move.FIRST);
      }
    });
    moveTB.add(first);

    previous = new JButton(getIcon(ICON_PREVIOUS));
    previous.setToolTipText(getMsg(AWARD_PREVIOUS));
    previous.setPreferredSize(new Dimension(16, 16));
    previous.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifyIndex(Move.PREVIOUS);
      }
    });
    moveTB.add(previous);

    next = new JButton(getIcon(ICON_NEXT));
    next.setToolTipText(getMsg(AWARD_NEXT));
    next.setPreferredSize(new Dimension(16, 16));
    next.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifyIndex(Move.NEXT);
      }
    });
    moveTB.add(next);

    last = new JButton(getIcon(ICON_LAST));
    last.setToolTipText(getMsg(AWARD_LAST));
    last.setPreferredSize(new Dimension(16, 16));
    last.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifyIndex(Move.LAST);
      }
    });
    moveTB.add(last);

    moveTB.setMaximumSize(moveTB.getPreferredSize());
    moveTB.setMinimumSize(moveTB.getPreferredSize());

    JPanel awardPanel = createPanel();
    JLabel label1 = new JLabel(getMsg(AWARD_LABEL));
    JLabel label2 = new JLabel(getMsg(AWARD_DESCRIPTION));
    JLabel label3 = new JLabel(getMsg(AWARD_TIPSNTRICKS));

    awardLabel = new JTextField();
    awardLabel.getDocument().addDocumentListener(ds);

    awardDescription = new JTextArea(2, 0);
    awardDescription.setLineWrap(true);
    awardDescription.setFont(getFont(DIALOG));
    awardDescription.getDocument().addDocumentListener(ds);
    JScrollPane scroll1 = new JScrollPane(awardDescription);

    awardTipsAndTricks = new JTextArea(2, 0);
    awardTipsAndTricks.setLineWrap(true);
    awardTipsAndTricks.setFont(getFont(DIALOG));
    awardTipsAndTricks.getDocument().addDocumentListener(ds);
    JScrollPane scroll2 = new JScrollPane(awardTipsAndTricks);

    GroupLayout layout = new GroupLayout(awardPanel);
    awardPanel.setLayout(layout);
//    layout.setAutoCreateContainerGaps(true);
    layout.setAutoCreateGaps(true);

    layout.setHorizontalGroup(
      layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                            .addComponent(label1)
                            .addComponent(label2)
                            .addComponent(label3))
            .addGroup(layout.createParallelGroup()
                            .addComponent(awardLabel)
                            .addComponent(scroll1)
                            .addComponent(scroll2)));

    layout.setVerticalGroup(
      layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(label1)
                            .addComponent(awardLabel))
            .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(label2)
                            .addComponent(scroll1))
            .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(label3)
                            .addComponent(scroll2)));

    panel.add(awardPanel, CENTER);

    JPanel eastPanel = createPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, Y_AXIS));

    JPanel scorePanel = createPanel(new FlowLayout(RIGHT, 0, 0));
    scorePanel.setAlignmentX(LEFT_ALIGNMENT);
    eastPanel.add(scorePanel);
    JToolBar scoreTB = new JToolBar(VERTICAL);
    scoreTB.setFloatable(false);
    scoreTB.setRollover(true);

    plus = new JButton(getIcon(ICON_PLUS));
    plus.setToolTipText(getMsg(AWARD_PLUS));
    plus.setPreferredSize(new Dimension(16, 16));
    plus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifyValue(1);
      }
    });
    scoreTB.add(plus);

    minus = new JButton(getIcon(ICON_MINUS));
    minus.setToolTipText(getMsg(AWARD_MINUS));
    minus.setPreferredSize(new Dimension(16, 16));
    minus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifyValue(-1);
      }
    });
    scoreTB.add(minus);

    scoreTB.setMaximumSize(scoreTB.getPreferredSize());
    scoreTB.setMinimumSize(scoreTB.getPreferredSize());

    _awardValue = new JLabel();
    _awardValue.setForeground(_awardValue.getBackground());
    scorePanel.add(_awardValue);

    awardValue = new JLabel();
    scorePanel.add(awardValue);
    scorePanel.add(scoreTB);

    awardIsSecret = new JCheckBox(getMsg(AWARD_SECRET), getIcon(ICON_UNCHECK));
    awardIsSecret.setSelectedIcon(getIcon(ICON_SECRET));
    awardIsSecret.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        switchCheckBox(awardIsSecret);
      }
    });
    eastPanel.add(awardIsSecret);

    awardIsMulti = new JCheckBox(getMsg(AWARD_MULTI), getIcon(ICON_UNCHECK));
    awardIsMulti.setSelectedIcon(getIcon(ICON_MULTI));
    awardIsMulti.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        switchCheckBox(awardIsMulti);
      }
    });
    eastPanel.add(awardIsMulti);

    awardIsAdded = new JCheckBox(getMsg(AWARD_ADDED), getIcon(ICON_UNCHECK));
    awardIsAdded.setSelectedIcon(getIcon(ICON_ADDED));
    awardIsAdded.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        switchCheckBox(awardIsAdded);
      }
    });

    eastPanel.add(awardIsAdded);

    JPanel voidPanel = createPanel();
    Dimension dim = voidPanel.getPreferredSize();
    dim.setSize(0, eastPanel.getPreferredSize().getHeight());
    voidPanel.setPreferredSize(dim);

    awardCards = createPanel(new CardLayout());
    awardCards.add(eastPanel, CARD_AWARD);
    awardCards.add(voidPanel, CARD_SEPARATOR);
    panel.add(awardCards, EAST);

    return panel;

  }


  private JPanel createAwardList(JPanel contentPane) {

    JPanel panel = createPanel(new BorderLayout(GAP, GAP));
    contentPane.add(panel, CENTER);

    awardList = new JList<Award>();
    awardList.setSelectionMode(SINGLE_SELECTION);
    awardList.setCellRenderer(new AwardCellRenderer());
    awardList.addListSelectionListener(
      new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          awardListModel.setSelection(awardList.getSelectedValue());
        }
      }
    );

    panel.add(new JScrollPane(awardList), CENTER);

    return panel;

  }


  private JPanel createAwardFilter(JPanel contentPane) {

    ActionListener actionFilter = app.getActionListener(AWARD_FILTER);

    awardFilterTitle = createTitledBorder(" ");
    awardFilterPanel = createPanel(new BorderLayout(), awardFilterTitle);
    contentPane.add(awardFilterPanel, SOUTH);
    JPanel panel = createPanel(new BorderLayout(GAP, GAP), createEmptyBorder(0, 5, 5, 5));
    awardFilterPanel.add(panel);

    awardFilterTxt = new JTextFieldSearch<Award>();
    awardFilterTxt.setFilteringActionListener(actionFilter);
    panel.add(awardFilterTxt, CENTER);

    // boutons de filtre

    JToolBar filterTB = new JToolBar(HORIZONTAL);
    filterTB.setBorder(createEmptyBorder());
    filterTB.setFloatable(false);
    filterTB.setRollover(true);
    panel.add(filterTB, EAST);

    achievedOkFilter = new JToggleButton(getIcon(ICON_TICK));
    achievedOkFilter.setToolTipText(getMsg(AWARD_ACHIEVED_OK));
    achievedOkFilter.setPreferredSize(new Dimension(20, 20));
    achievedOkFilter.addActionListener(actionFilter);
    filterTB.add(achievedOkFilter);

    achievedKoFilter = new JToggleButton(getIcon(ICON_LOCK));
    achievedKoFilter.setToolTipText(getMsg(AWARD_ACHIEVED_KO));
    achievedKoFilter.setPreferredSize(new Dimension(20, 20));
    achievedKoFilter.addActionListener(actionFilter);
    filterTB.add(achievedKoFilter);

    filterTB.addSeparator();

    secretFilter = new JToggleButton(getIcon(ICON_SECRET));
    secretFilter.setToolTipText(getMsg(AWARD_SECRET));
    secretFilter.setPreferredSize(new Dimension(20, 20));
    secretFilter.addActionListener(actionFilter);
    filterTB.add(secretFilter);

    multiFilter = new JToggleButton(getIcon(ICON_MULTI));
    multiFilter.setToolTipText(getMsg(AWARD_MULTI));
    multiFilter.setPreferredSize(new Dimension(20, 20));
    multiFilter.addActionListener(actionFilter);
    filterTB.add(multiFilter);

    addedFilter = new JToggleButton(getIcon(ICON_ADDED));
    addedFilter.setToolTipText(getMsg(AWARD_ADDED));
    addedFilter.setPreferredSize(new Dimension(20, 20));
    addedFilter.addActionListener(actionFilter);
    filterTB.add(addedFilter);

    return contentPane;

  }


  private void addLink(JLabel label, final Key key) {

    label.setCursor(getPredefinedCursor(HAND_CURSOR));

    label.addMouseListener(
      new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          Game game = gameListModel.getSelection();
          String oldValue = null;
          switch (key) {
            case GAME_NAME :
              oldValue = game.getName();
              break;
            case GAME_DEVELOPER :
              oldValue = game.getDeveloper();
              break;
            case GAME_PUBLISHER :
              oldValue = game.getPublisher();
              break;
            default :
              break;
          }
          String newValue = inputValue(key, oldValue);
          if (newValue != null) {
            newValue = newValue.trim();
            if (!isEmpty(newValue) && !newValue.equals(oldValue)) {
              switch (key) {
                case GAME_NAME :
                  game.setName(newValue);
                  gameListModel.sort();
                  gameList.setSelectedValue(game, true);
                  break;
                case GAME_DEVELOPER :
                  game.setDeveloper(newValue);
                  break;
                case GAME_PUBLISHER :
                  game.setPublisher(newValue);
                  break;
                default :
                  break;
              }
              refreshGameInfo(game);
              gameListModel.fireSelectedChange();
            }
          }
        }
      }
    );

  }


  private void removeLink(JLabel label) {
    label.setCursor(getDefaultCursor());;
    for (MouseListener listener : label.getMouseListeners()) {
      label.removeMouseListener(listener);
    }
  }


  private String inputValue(Key key, String initValue) {

    return (String)showInputDialog(frame, formatHTML(getMsg(key), STYLE_B), getMsg(DIALOG_INPUT_TITLE),
                                   QUESTION_MESSAGE, null, null, initValue);

  }


  private void refreshGameInfo(Game game) {

    if (!isGameRefresh) {
      isGameRefresh = true;

      if (gameImagePanel.isEditable() &&
         (game == null || !menuItemGameUpdate.isSelected()))
      {
        removeLink(gameName);
        removeLink(gameDeveloper);
        removeLink(gamePublisher);
        gameImagePanel.setEditable(false);
      }

      if (game != null) {
        int style = STYLE_BI;
        if (menuItemGameUpdate.isSelected()) {
          style = STYLE_BIU;
          if (!gameImagePanel.isEditable()) {
            addLink(gameName,      GAME_NAME);
            addLink(gameDeveloper, GAME_DEVELOPER);
            addLink(gamePublisher, GAME_PUBLISHER);
            gameImagePanel.setEditable(true);
          }
        }
        menuItemGameDelete.setEnabled(true);
        menuItemAwardInsert.setEnabled(true);
        menuItemSeparator.setEnabled(true);
        gameName.setText(formatHTML(game.getName() + NBSP, style, 2));
        gameDeveloper.setText(formatHTML(game.getDeveloper() + NBSP, style));
        gamePublisher.setText(formatHTML(game.getPublisher() + NBSP, style));
        gameScore.setText(formatHTML(game.getScore(), STYLE_B, 3));
        gameScoreMax.setText(formatHTML(game.getScoreMax(), STYLE_B, 2));
        gameImagePanel.setImage(getImage(gameImagePanel, game.getBytes(), IMG_SIZE_LARGE, IMG_SIZE_LARGE));
        awardListModel.setList(game.getAwards());
        for (JCheckBox star : stars) {
          int noStar = Integer.parseInt(star.getName());
          star.setEnabled(true);
          star.setSelected(noStar < game.getRating());
          star.setVisible(noStar == 0 || star.isSelected() || menuItemGameUpdate.isSelected());
          if (menuItemGameUpdate.isSelected()) {
            star.setCursor(getPredefinedCursor(HAND_CURSOR));
          }
          else {
            star.setCursor(getDefaultCursor());
            if (noStar == 0 && game.getRating() == 0) {
              star.setEnabled(false);
            }
          }
        }
      }
      else {
        menuItemGameDelete.setEnabled(false);
        menuItemAwardInsert.setEnabled(false);
        menuItemSeparator.setEnabled(false);
        gameName.setText(formatHTML("...", 0, 2));
        gameScore.setText(formatHTML("---", 0, 3));
        gameScoreMax.setText(formatHTML("----", 0, 2));
        gameDeveloper.setText(formatHTML("...", 0));
        gamePublisher.setText(formatHTML("...", 0));
        gameImagePanel.setImage(getImage(gameImagePanel, (byte[])null, IMG_SIZE_LARGE, IMG_SIZE_LARGE));
        awardListModel.setList(null);
        for (JCheckBox star : stars) {
          star.setEnabled(false);
          star.setSelected(false);
          String name = star.getName();
          if (!name.equals("0")) {
            star.setVisible(false);
          }
        }
      }
      refreshAwardFilterTitle();
      isGameRefresh = false;
    }

  }


  private void refreshAwardInfo(Award award) {

    if (!isAwardRefresh) {
      isAwardRefresh = true;
      if (award != null) {
        awardImagePanel.setEditable(true);
        awardLabel.setText(award.getLabel());
        awardLabel.setEditable(true);
        awardDescription.setText(award.getDescription());
        awardDescription.setEditable(true);
        awardDescription.setOpaque(true);
        awardTipsAndTricks.setText(award.getTipsAndTricks());
        awardTipsAndTricks.setEditable(true);
        awardTipsAndTricks.setOpaque(true);
        awardImagePanel.setImage(getImage(awardImagePanel, award.getBytes(), IMG_SIZE_LARGE, IMG_SIZE_LARGE));
        awardIsSecret.setSelected(award.isSecret());
        awardIsSecret.setEnabled(true);
        awardIsMulti.setSelected(award.isMulti());
        awardIsMulti.setEnabled(true);
        awardIsAdded.setSelected(award.isAdded());
        awardIsAdded.setEnabled(true);
        plus.setEnabled(true);
        minus.setEnabled(true);
        first.setEnabled(true);
        previous.setEnabled(true);
        next.setEnabled(true);
        last.setEnabled(true);
        CardLayout cl = (CardLayout)awardCards.getLayout();
        if (award.isSeparator()) {
          cl.show(awardCards, CARD_SEPARATOR);
          awardGlue.setVisible(false);
          awardIsAchieved.setVisible(false);
        }
        else {
          cl.show(awardCards, CARD_AWARD);
          awardGlue.setVisible(true);
          awardIsAchieved.setVisible(true);
          awardIsAchieved.setSelected(award.isAchieved());
          awardIsAchieved.setEnabled(true);
          String msg = getMsg(awardIsAchieved.isSelected() ? AWARD_ACHIEVED_OK : AWARD_ACHIEVED_KO);
          awardIsAchieved.setToolTipText(formatHTML(msg, STYLE_B));
        }
      }
      else {
        awardImagePanel.setEditable(false);
        awardLabel.setText(null);
        awardLabel.setEditable(false);
        awardDescription.setText(null);
        awardDescription.setEditable(false);
        awardDescription.setOpaque(false);
        awardTipsAndTricks.setText(null);
        awardTipsAndTricks.setEditable(false);
        awardTipsAndTricks.setOpaque(false);
        awardImagePanel.setImage(getImage(awardImagePanel, (byte[])null, IMG_SIZE_LARGE, IMG_SIZE_LARGE));
        awardIsAchieved.setSelected(false);
        awardIsAchieved.setEnabled(false);
        awardIsAchieved.setToolTipText(null);
        awardIsSecret.setSelected(false);
        awardIsSecret.setEnabled(false);
        awardIsMulti.setSelected(false);
        awardIsMulti.setEnabled(false);
        awardIsAdded.setSelected(false);
        awardIsAdded.setEnabled(false);
        plus.setEnabled(false);
        minus.setEnabled(false);
        first.setEnabled(false);
        previous.setEnabled(false);
        next.setEnabled(false);
        last.setEnabled(false);
      }
      refreshAwardValue(award);
      menuItemAwardDelete.setEnabled(award != null);
      isAwardRefresh = false;
    }

  }


  private void refreshAwardValue(Award award) {

    if (award != null) {
      int length = 3 - String.valueOf(award.getValue()).length();
      _awardValue.setText(formatHTML(rPad("", '_', length), STYLE_B, 3));
      awardValue.setText(formatHTML(award.getValue(), STYLE_B, 3));
    }
    else {
      _awardValue.setText(formatHTML("__", STYLE_B, 3));
      awardValue.setText(formatHTML("0", STYLE_B, 3));
    }

  }


  private void edited(DocumentEvent e) {

    if (!isAwardRefresh) {
      Award award = awardListModel.getSelection();
      if (award != null) {
        Document source = e.getDocument();
        if (awardLabel.getDocument() == source) {
          award.setLabel(awardLabel.getText());
          awardListModel.fireSelectedChange();
        }
        else if (awardDescription.getDocument() == source) {
          award.setDescription(awardDescription.getText());
        }
        else if (awardTipsAndTricks.getDocument() == source) {
          award.setTipsAndTricks(awardTipsAndTricks.getText());
        }
      }
    }

  }


  private void rating(JCheckBox star) {

    Game game = gameListModel.getSelection();

    if (game != null && menuItemGameUpdate.isSelected()) {
      // edition
      int noStar = Integer.parseInt(star.getName());
      for (int i=0; i < stars.length; i++) {
        if (i != noStar) {
          stars[i].setSelected(i < noStar);
        }
      }
      game.setRating(noStar + (star.isSelected() ? 1 : 0));
    }
    else {
      star.setSelected(!star.isSelected());
    }

  }


  private String refreshGameFilterTitle() {
    int viewSize = gameListModel.getSize();
    int dataSize = gameListModel.getList() != null ? gameListModel.getList().size() : 0;
    return refreshFilterTitle(gameFilterPanel, gameFilterTitle, viewSize, dataSize);
  }


  private String refreshAwardFilterTitle() {

    int viewSize = awardListModel.getSize();
    int dataSize = awardListModel.getList() != null ? awardListModel.getList().size() : 0;
    Game game = gameListModel.getSelection();

    if (game != null) {
      for (Award award : game.getAwards()) {
        if (award.isSeparator()) {
          viewSize--;
          dataSize--;
        }
      }

    }

    return refreshFilterTitle(awardFilterPanel, awardFilterTitle, viewSize, dataSize);

  }


  private String refreshFilterTitle(JPanel panel, TitledBorder border, int viewSize, int dataSize) {
    String title = " [ " + viewSize + " / " + dataSize + " ] ";
    border.setTitle(title);
    panel.repaint();
    return title;
  }


  private void switchCheckBox(JCheckBox cb) {

    if (!isAwardRefresh) {
      Award award = awardListModel.getSelection();
      if (award != null) {
        if (cb == awardIsAchieved) {
          award.setAchieved(cb.isSelected());
          refreshAwardInfo(award);
          updateScore();
        }
        else if (cb == awardIsSecret) {
          award.setSecret(cb.isSelected());
        }
        else if (cb == awardIsMulti) {
          award.setMulti(cb.isSelected());
        }
        else if (cb == awardIsAdded) {
          award.setAdded(cb.isSelected());
        }
        awardListModel.fireSelectedChange();
      }
    }

  }


  private void modifyValue(int i) {

    Award award = awardListModel.getSelection();

    if (award != null) {
      int value = award.getValue() + i;
      if (value < 1000 && !(value < 0)) {
        award.setValue(award.getValue() + i);
        updateScore();
        refreshAwardValue(award);
      }
    }

  }


  private void updateScore() {

    Game game = gameListModel.getSelection();

    if (game != null) {
      game.updateScore();
      refreshGameInfo(game);
    }

  }


  private void modifyIndex(Move move) {

    Game game = gameListModel.getSelection();
    Award award = awardListModel.getSelection();
    List<Award> awards = awardListModel.getList();

    if (game != null && award != null) {
      int index = award.getIndex();
      int selectedIndex = awardList.getSelectedIndex();
      switch (move) {
        case FIRST : // end = 0
          index = 0;
          break;
        case PREVIOUS :
          selectedIndex--;
          break;
        case NEXT :
          selectedIndex++;
          break;
        case LAST :
          index = awards.size() - 1;
          break;
      }
      Award a = awardListModel.getElementAt(selectedIndex);
      if (a != null && a != award) {
        index = a.getIndex();
      }
      if (game.moveAward(award, index)) {
        awardListModel.update();
        awardList.setSelectedValue(award, true);
      }
    }

  }


  private void createModel() {

    model = new AwardModel(app.getEngine());

    // Game --------------------------------------------------------------------

    gameListModel = new ListModel<Game>();
    gameList.setModel(gameListModel);
    gameListModel.addPropertyChangeListener("selection",
      new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          refreshGameInfo(gameListModel.getSelection());
          awardList.clearSelection();
          awardList.scrollRectToVisible(new Rectangle());
        }
      }
    );

    gameFilterTxt.setFilterable(gameListModel);

    // Award -------------------------------------------------------------------

    awardListModel = new ListModel<Award>();
    awardList.setModel(awardListModel);
    awardListModel.addPropertyChangeListener("selection",
      new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          refreshAwardInfo(awardListModel.getSelection());
        }
      }
    );

    awardFilter = new IFilter<Award>()
    {
      @Override
      public boolean matches(Award award) {
        if (award.isSeparator()) {
          return true;
        }
        // text filter
        if (!awardFilterTxt.getFilter().matches(award)) {
          return false;
        }
        // specific filter
        return (!achievedOkFilter.isSelected() ||  award.isAchieved()) &&
               (!achievedKoFilter.isSelected() || !award.isAchieved()) &&
               (!secretFilter.isSelected()     ||  award.isSecret())   &&
               (!multiFilter.isSelected()      ||  award.isMulti())    &&
               (!addedFilter.isSelected()      ||  award.isAdded());
      }
    };

    gameListModel.setSelection(null);
    awardListModel.setSelection(null);

    refreshAwardFilterTitle();
    refreshGameFilterTitle();

    new Timer().schedule(
      new TimerTask() {
        @Override
        public void run() {
          model.loadGames(gameListModel);
          refreshGameFilterTitle();
//          gameList.repaint(); // does not work
        }
      }, 0);

  }


  // ——————————————————————————————————————————————————————————————————— Methods


  void actionGameInsert() {

    menuItemGameUpdate.setSelected(true);

    Game game = model.createGame();
    gameListModel.update();
    gameList.clearSelection();
    gameList.setSelectedValue(game, true);
    refreshGameFilterTitle();

    logger.debug(">> Add Game");

  }


  void actionGameUpdate() {
    refreshGameInfo(gameListModel.getSelection());
    logger.debug(">> Edit Mode " + (menuItemGameUpdate.isSelected() ? "On" : "Off"));
  }


  void actionGameDelete() {

    Game game = gameListModel.getSelection();

    if (game != null) {
      int code = showConfirmDialog(frame, getMsg(GAME_DELETE_DIALOG_MSG, game.getName()),
                                          getMsg(GAME_DELETE_DIALOG_TITLE),
                                          OK_CANCEL_OPTION, WARNING_MESSAGE);
      if (code == 0) {
        model.deleteGame(game);
        gameListModel.update();
        gameList.clearSelection();
        refreshGameFilterTitle();
        logger.debug(">> Delete Game");
      }
    }

  }


  void actionGameFilter() {

    Game game = gameListModel.getSelection();
    if (game == null) {
      gameList.clearSelection();
    }
    else {
      gameList.setSelectedValue(game, true);
    }

    logger.debug("Filtering Games" + refreshGameFilterTitle());

  }


  void actionAwardInsert() {

    Award award = model.createAward(gameListModel.getSelection());
    awardListModel.update();
    awardList.setSelectedValue(award, true);
    awardLabel.selectAll();
    awardLabel.requestFocus();
    refreshAwardFilterTitle();

    logger.debug(">> Add Award");

  }


//  public void actionAwardUpdate() {
//    logger.debug(">> Update Award");
//  }


  void actionAwardDelete() {

    Game game = gameListModel.getSelection();
    Award award = awardListModel.getSelection();

    if (award != null && !award.isSeparator()) {
      int code = showConfirmDialog(frame, getMsg(AWARD_DELETE_DIALOG_MSG, award.getLabel()),
                                          getMsg(AWARD_DELETE_DIALOG_TITLE),
                                          OK_CANCEL_OPTION, WARNING_MESSAGE);
      if (code == 0) {
        model.deleteAward(game, award);
        awardListModel.update();
        awardList.clearSelection();
        updateScore();
        logger.debug(">> Delete Award");
      }
    }

  }


  void actionAwardSeparator() {

    Award award = model.createSeparator(gameListModel.getSelection());
    awardListModel.update();
    awardList.setSelectedValue(award, true);
    awardLabel.selectAll();
    awardLabel.requestFocus();

    logger.debug(">> Add Separator");

  }


  void actionAwardFilter() {

    if (isEmpty(awardFilterTxt.getText()) &&
        !achievedOkFilter.isSelected() &&
        !achievedKoFilter.isSelected() &&
        !secretFilter.isSelected() &&
        !multiFilter.isSelected() &&
        !addedFilter.isSelected())
    {
      awardListModel.filter(null);
    }
    else {
      if (achievedOkFilter.isSelected() && achievedKoFilter.isSelected()) {
        achievedOkFilter.setSelected(!isOkFilterSelected);
        achievedKoFilter.setSelected(isOkFilterSelected);
      }
      isOkFilterSelected = achievedOkFilter.isSelected();
      awardListModel.filter(awardFilter);
    }

    Award award = awardListModel.getSelection();
    if (award == null) {
      awardList.clearSelection();
    }
    else {
      awardList.setSelectedValue(award, true);
    }

    logger.debug("Filtering Awards" + refreshAwardFilterTitle());

  }


  void actionGameImport() {
    ImportExportDialog dialog = new ImportExportDialog(frame, model, true);
    dialog.setVisible(true);
    gameListModel.update();
    refreshGameFilterTitle();
  }


  void actionGameExport() {
    ImportExportDialog dialog = new ImportExportDialog(frame, model, false);
    dialog.setVisible(true);
  }


  void actionAbout() {
    String msg = "Award Center\n" +
                 "Version: 1.0";
    showMessageDialog(frame, msg, getMsg(MENU_HELP_ABOUT), INFORMATION_MESSAGE);
  }


  boolean actionSave() {
    model.saveGames();
    return true;
  }


  boolean actionClose() {
    File dir = awardImagePanel.getImageDir();
    app.setImageDir((dir != null) ? dir : gameImagePanel.getImageDir());
    actionSave();
    app.getEngine().stop();
    return true;
  }


}