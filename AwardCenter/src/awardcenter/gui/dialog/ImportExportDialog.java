package awardcenter.gui.dialog;


import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.FlowLayout.LEFT;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.PAGE_AXIS;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;
import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

import static awardcenter.resources.Key.AWARD_TIPSNTRICKS;
import static awardcenter.resources.Key.DIALOG_IMPEXP_BUTTON_BROWSE;
import static awardcenter.resources.Key.DIALOG_IMPEXP_BUTTON_CLOSE;
import static awardcenter.resources.Key.DIALOG_IMPEXP_BUTTON_EXPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_BUTTON_IMPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_BUTTON_SELECT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_BUTTON_UNSELECT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_DIR_EXPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_DIR_IMPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_PARAM_EXPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_PARAM_IMPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_TITLE_EXPORT;
import static awardcenter.resources.Key.DIALOG_IMPEXP_TITLE_IMPORT;
import static awardcenter.resources.Key.GAME_RATING;
import static awardcenter.resources.Key.GAME_SCORE;
import static awardcenter.resources.Key.ICON_EXPORT;
import static awardcenter.resources.Key.ICON_IMPORT;

import static util.resource.ResourceUtil.getImg;
import static util.resource.ResourceUtil.getMsg;
import static util.swing.SwingUtil.createPanel;

import static awardcenter.gui.AwardCenterApplication.GAP;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import awardcenter.gui.component.ImportExportCellRenderer;
import awardcenter.model.Award;
import awardcenter.model.AwardModel;
import awardcenter.model.Game;


public class ImportExportDialog extends JDialog {


  // —————————————————————————————————————————————————————————————— Constructors


  public ImportExportDialog(JFrame frame, AwardModel model, boolean isImport) {

    super(frame, true);
    this.isImport = isImport;
    this.model = model;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(800, 600);

    // Body --------------------

    JPanel contentPane = createPanel(new BorderLayout(GAP, GAP), createEmptyBorder(GAP, GAP, GAP, GAP));
    setContentPane(contentPane);

    // directory chooser
    createDirectoryChooser();

    // options
    createOptions();

    // games
    createTable();

    // buttons
    createButtons();

    if (isImport) {
      // import
      setTitle(getMsg(DIALOG_IMPEXP_TITLE_IMPORT));
      setIconImage(getImg(ICON_IMPORT));
    }
    else {
      // export
      setTitle(getMsg(DIALOG_IMPEXP_TITLE_EXPORT));
      setIconImage(getImg(ICON_EXPORT));
      loadData(model.getGames());
    }

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private boolean isImport;

  private AwardModel model;

  private List<Game> games = new ArrayList<Game>();
  private BitSet selection = new BitSet();
  private JTable gameTable;

  private File dir;
  private JTextField dirTxt;

  private JCheckBox ratingCB = new JCheckBox(getMsg(GAME_RATING));
  private JCheckBox scoreCB  = new JCheckBox(getMsg(GAME_SCORE));
  private JCheckBox infoCB   = new JCheckBox(getMsg(AWARD_TIPSNTRICKS));

  private JButton impexp;

  private JPanel northPane;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void createDirectoryChooser() {

    northPane = createPanel(new GridLayout(0, 1));
    String title = getMsg(isImport ? DIALOG_IMPEXP_DIR_IMPORT : DIALOG_IMPEXP_DIR_EXPORT);
    TitledBorder border = createTitledBorder(title);
    JPanel panelTitle = createPanel(new BorderLayout(), border);
    northPane.add(panelTitle);
    getContentPane().add(northPane, NORTH);

    JPanel panel = createPanel(new BorderLayout(GAP, GAP), createEmptyBorder(0, 5, 5, 5));
    panelTitle.add(panel);

    dirTxt = new JTextField();
    dirTxt.setEditable(false);
    panel.add(dirTxt, CENTER);

    JButton browse = new JButton(getMsg(DIALOG_IMPEXP_BUTTON_BROWSE));
    panel.add(browse, EAST);
    browse.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectDirectory();
      }
    });

  }


  private void createOptions() {

    String title = getMsg(isImport ? DIALOG_IMPEXP_PARAM_IMPORT : DIALOG_IMPEXP_PARAM_EXPORT);
    TitledBorder border = createTitledBorder(title);
    JPanel panelTitle = createPanel(new BorderLayout(), border);
    northPane.add(panelTitle);

    JPanel panel = createPanel(new FlowLayout(LEFT, 5, 0));
    panelTitle.add(panel);

    // 1. rating
    ratingCB = new JCheckBox(getMsg(GAME_RATING), true);
    panel.add(ratingCB);

    // 2. score
    scoreCB = new JCheckBox(getMsg(GAME_SCORE), true);
    panel.add(scoreCB);

    // 3. info
    infoCB = new JCheckBox(getMsg(AWARD_TIPSNTRICKS), true);
    panel.add(infoCB);

  }


  private void createTable() {

    TableModel tableModel = new AbstractTableModel() {
      @Override
      public int getColumnCount() { return 2; }
      @Override
      public int getRowCount() { return games.size(); }
      @Override
      public Object getValueAt(int row, int col) {
        if (col == 0) {
          return games.get(row);
        }
        else {
          return selection.get(row);
        }
      }
      @Override
      public void setValueAt(Object value, int row, int col) {
        selection.flip(row);
        refreshButton();
      }
      @Override
      public boolean isCellEditable(int row, int column) {
        return (column != 0);
      }
      @Override
      public Class<?> getColumnClass(int column) {
        return (getValueAt(0, column).getClass());
      }
    };

    gameTable = new JTable();
    gameTable.setModel(tableModel);
//    gameTable.setShowGrid(false);
    gameTable.setTableHeader(null);
    gameTable.setShowVerticalLines(false);
    gameTable.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
    gameTable.setRowSelectionAllowed(false);
    gameTable.setColumnSelectionAllowed(false);
    gameTable.setCellSelectionEnabled(false);

    JScrollPane sp = new JScrollPane(gameTable);
    gameTable.getParent().setBackground(gameTable.getBackground());
    getContentPane().add(sp, CENTER);

  }


  private void createButtons() {

    JPanel panel = createPanel();
    panel.setLayout(new BoxLayout(panel, PAGE_AXIS));
    getContentPane().add(panel, EAST);

    JButton selectAll = new JButton(getMsg(DIALOG_IMPEXP_BUTTON_SELECT));
    panel.add(selectAll);
    selectAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selection.set(0, games.size());
        gameTable.repaint();
        refreshButton();
      }
    });

    panel.add(Box.createVerticalStrut(5));

    JButton unselectAll = new JButton(getMsg(DIALOG_IMPEXP_BUTTON_UNSELECT));
    panel.add(unselectAll);
    unselectAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selection.clear();
        gameTable.repaint();
        refreshButton();
      }
    });

    panel.add(Box.createVerticalStrut(5));

    impexp = new JButton(getMsg(isImport ? DIALOG_IMPEXP_BUTTON_IMPORT : DIALOG_IMPEXP_BUTTON_EXPORT));
    impexp.setEnabled(false);
    panel.add(impexp);
    impexp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (isImport) {
          model.importGames(filter());
        }
        else {
          model.exportGames(filter(), dir);
        }
      }
    });

    panel.add(Box.createVerticalGlue());

    JButton close = new JButton(getMsg(DIALOG_IMPEXP_BUTTON_CLOSE));
    panel.add(close);
    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    Dimension size = selectAll.getPreferredSize();
    size.setSize(panel.getPreferredSize().getWidth(), size.getHeight());
    selectAll.setMaximumSize(size);
    unselectAll.setMaximumSize(size);
    impexp.setMaximumSize(size);
    close.setMaximumSize(size);

  }


  private void loadData(List<Game> list) {

    games.clear();
    games.addAll(list);
    Collections.sort(games);
    selection.set(0, games.size());

    if (games.size() > 0) {
      ImportExportCellRenderer renderer = new ImportExportCellRenderer();
      int height = renderer.getHeight(gameTable, games.get(0));
      gameTable.setRowHeight(height);
      TableColumnModel tcm = gameTable.getColumnModel();
      tcm.getColumn(0).setCellRenderer(renderer);
      tcm.getColumn(1).setMaxWidth(height);
    }

  }


  private void selectDirectory() {

    JFileChooser chooser = new JFileChooser(dir);
    chooser.setFileSelectionMode(DIRECTORIES_ONLY);

    if (chooser.showOpenDialog(this) == APPROVE_OPTION) {
      dir = chooser.getSelectedFile();
      dirTxt.setText(dir.getAbsolutePath());
      if (isImport) {
        List<Game> games = model.loadGames(dir);
        if (games != null && games.size() > 0) {
          loadData(games);
        }
      }
    }

    refreshButton();

  }


  private void refreshButton() {
    impexp.setEnabled(dir != null && selection.nextSetBit(0) != -1);
  }


  private List<Game> filter() {

    List<Game> view = new ArrayList<Game>();

    for (int i = 0; (i = selection.nextSetBit(i)) != -1; i++) {
      Game game = games.get(i).clone();
      if (!ratingCB.isSelected()) {
        game.setRating(0);
      }
      if (!scoreCB.isSelected() || !infoCB.isSelected()) {
        if (!scoreCB.isSelected()) {
          game.setScore(0);
        }
        for (Award award : game.getAwards()) {
          if (!scoreCB.isSelected()) {
            award.setAchieved(false);
          }
          if (!infoCB.isSelected()) {
            award.setTipsAndTricks(null);
          }
        }
      }
      view.add(game);
    }

    return view;

  }


}