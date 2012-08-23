package util.swing.component;


import static java.awt.Cursor.DEFAULT_CURSOR;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.Cursor.getPredefinedCursor;
import static java.awt.Font.ITALIC;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

import static javax.swing.UIManager.getColor;

import static util.misc.StringUtil.isEmpty;
import static util.resource.ResourceUtil.getIcon;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.SwingGuiKey.ICON_CANCEL_BUTTON;
import static util.resources.SwingGuiKey.ICON_MAGNIFIER;
import static util.resources.SwingGuiKey.JTEXTFIELDSEARCH_CLEAR;
import static util.resources.SwingGuiKey.JTEXTFIELDSEARCH_WATERMARK;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.collection.IFilter;
import util.collection.IFilterable;


public class JTextFieldSearch<T> extends JTextFieldCheckBox implements FocusListener {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final int DEFAULT_LATENCY = 500; // in ms

  private static final Color COLOR_BACKGROUND = getColor("TextField.background");

  private static final Color COLOR_WATERMARK  = getColor("textInactiveText");

  private static final Color COLOR_FILTER     = Color.decode("#FFFFCC");


  // —————————————————————————————————————————————————————————————— Constructors


  public JTextFieldSearch() {
    this(0, null, null, null);
  }


  public JTextFieldSearch(int col) {
    this(col, null, null, null);
  }


  public JTextFieldSearch(IFilterable<T> filterable) {
    this(0, null, filterable, null);
  }


  public JTextFieldSearch(IFilterable<T> filterable, IFilter<T> filter) {
    this(0, null, filterable, filter);
  }


  public JTextFieldSearch(ActionListener listener) {
    this(0, listener, null, null);
  }


  private JTextFieldSearch(int col, ActionListener listener, IFilterable<T> filterable, IFilter<T> filter) {

    super(col, getIcon(ICON_MAGNIFIER), null, Side.RIGHT);

    isCaseSensitive = false;
    watermark = getMsg(JTEXTFIELDSEARCH_WATERMARK);
    colorFilter = COLOR_FILTER;
    latency = DEFAULT_LATENCY;
    timer = new Timer(latency, null);
    timer.setRepeats(false);

    if (listener != null) {
      listenerFiltering = listener;
    }
    else {
      this.filterable = filterable;
      if (filter != null) {
        this.filter = filter;
      }
      else {
        this.filter = new IFilter<T>() {
          @Override
          public boolean matches(T o) {
            String   text  =  isCaseSensitive ? o.toString() : o.toString().toUpperCase();
            String[] words = (isCaseSensitive ? getText()    : getText().toUpperCase()).split(" ");
            for (String word : words) {
              if (!text.contains(word)) {
                return false;
              }
            }
            return true;
          }
        };
      }
      listenerFiltering = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (JTextFieldSearch.this.filterable != null) {
            if (isEmpty(getText())) {
              JTextFieldSearch.this.filterable.filter(null);
            }
            else {
              JTextFieldSearch.this.filterable.filter(JTextFieldSearch.this.filter);
            }
          }
        }
      };
    }

    timer.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (listenerBeforeFiltering != null) {
            listenerBeforeFiltering.actionPerformed(e);
          }
          if (listenerFiltering != null) {
            _updateCheckBox();
            listenerFiltering.actionPerformed(e);
          }
          if (listenerAfterFiltering != null) {
            listenerAfterFiltering.actionPerformed(e);
          }
        }
      }
    );

    getDocument().addDocumentListener(
      new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
          timer.restart();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
          timer.restart();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
        }
      }
    );

    addCheckBoxActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (!isEmpty(getText())) {
            setText(null);
            timer.setInitialDelay(0);
            timer.restart();
            timer.setInitialDelay(latency);
            _setBackgroundColor(false);
          }
        }
      }
    );

    addFocusListener(this);

  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private int latency;

  private boolean isCaseSensitive;

  private String watermark;

  private Color colorFilter;

  private Timer timer;

  private ActionListener listenerBeforeFiltering;

  private ActionListener listenerFiltering;

  private ActionListener listenerAfterFiltering;

  private IFilterable<T> filterable;

  private IFilter<T> filter;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _updateCheckBox() {

    if (isEmpty(getText())) {
      setSelectedIcon(null);
      setCheckBoxToolTip(null);
      setCheckBoxCursor(getPredefinedCursor(DEFAULT_CURSOR));
      setCheckBoxSelected(false);
    }
    else {
      setSelectedIcon(getIcon(ICON_CANCEL_BUTTON));
      setCheckBoxToolTip(getMsg(JTEXTFIELDSEARCH_CLEAR));
      setCheckBoxCursor(getPredefinedCursor(HAND_CURSOR));
      setCheckBoxSelected(true);
    }

  }


  private void _setBackgroundColor(boolean isfocusGained) {

    if (colorFilter != null) {
      if (isfocusGained) {
        setBackground(colorFilter);
      }
      else if (isEmpty(getText())) {
        setBackground(COLOR_BACKGROUND);
      }
    }
    else {
      repaint();
    }

  }


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    if (!hasFocus() && isEmpty(getText()) && !isEmpty(watermark)) {
      g.setFont(getFont().deriveFont(ITALIC));
      g.setColor(COLOR_WATERMARK);
      int h = g.getFontMetrics().getHeight();
      int y = (h + getHeight())/2 - 2;
      int x = getInsets().left;
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
      g2d.drawString(watermark, x, y);
    }

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public void setLatency(int latency) {
    this.latency = latency;
    timer.setInitialDelay(latency);
  }


  public void setCaseSensitive(boolean isCaseSensitive) {
    this.isCaseSensitive = isCaseSensitive;
  }


  public void setWatermark(String watermark) {
    this.watermark = watermark;
  }


  public void setColorFilter(Color colorFilter) {
    this.colorFilter = colorFilter;
  }


  public void setActionListenerBeforeFiltering(ActionListener listener) {
    listenerBeforeFiltering = listener;
  }


  public void setActionListenerFiltering(ActionListener listener) {
    listenerFiltering = listener;
  }


  public void setActionListenerAfterFiltering(ActionListener listener) {
    listenerAfterFiltering = listener;
  }


  public void setFilterable(IFilterable<T> filterable) {
    this.filterable = filterable;
  }


  public void setFilter(IFilter<T> filter) {
    this.filter = filter;
  }


  @Override
  public void focusGained(FocusEvent e) {
    _setBackgroundColor(true);
  }


  @Override
  public void focusLost(FocusEvent e) {
    _setBackgroundColor(false);
  }


}