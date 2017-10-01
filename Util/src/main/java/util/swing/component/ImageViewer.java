package util.swing.component;


import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.event.MouseEvent.MOUSE_PRESSED;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingConstants.CENTER;

import static util.resource.Logger.getLogger;
import static util.resource.ResourceUtil.getMsg;
import static util.resources.GuiKey.IMAGEVIEWER_FILE_TYPE;
import static util.resources.GuiKey.IMAGEVIEWER_SELECT_IMAGE;
import static util.resources.LogKey.IMAGEVIEWER_READ_FILE;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.swing.ImageUtil;
import util.resource.Logger;


@SuppressWarnings("serial")
public class ImageViewer extends JPanel {


  // —————————————————————————————————————————————————————————— Static Constants


  private static final Logger logger = getLogger(ImageViewer.class);

  private static final String[] EXTS = { "jpg", "jpeg", "gif", "png" };

  private static final String DESC = getMsg(IMAGEVIEWER_FILE_TYPE) + " " + Arrays.toString(EXTS);


  // —————————————————————————————————————————————————————————— Static Variables



  // —————————————————————————————————————————————————————————————— Constructors


  public ImageViewer() {
    setOpaque(false);
    _updateHint();
  }


  public ImageViewer(int width, int height) {
    this();
    this.height = height;
    this.width = width;
    setPreferredSize(new Dimension(width, height));
    setMaximumSize(new Dimension(width, height));
  }


  public ImageViewer(int width, int height, int thickness, Color color) {
    this(width + 2*thickness, height + 2*thickness);
    this.height = height;
    this.width = width;
    COLOR_FG = color;
    setBorder(createLineBorder(color, thickness));
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private boolean editable;

  private int height;
  private int width;

  private Image image;

  private Image cache;

  private File imageDir;

  private File imageFile;

  private Color COLOR_FG;


  // ——————————————————————————————————————————————————————————— Private Methods


  private void _setImage(Image image) {

    Image oldImage = this.image;
    this.image = image;
    _clearCache();
    firePropertyChange("image", oldImage, image);
    revalidate();
    repaint();
    _updateHint();

  }


  private Image _getCache() {

    if (cache == null) {
      if (getImage() != null &&
          getImage().getHeight(this) == height &&
          getImage().getWidth(this)  == width)
      {
        cache = getImage();
      }
      else {
        cache = ImageUtil.getImage(this, getImage(), height, width);
      }
    }

    return cache;

  }


  private void _clearCache() {
    cache = null;
    repaint();
  }


  private void _updateHint() {

    Component hint = null;

    if (getComponentCount() > 0) {
      hint = getComponent(0);
    }

    if (getImage() == null && isEditable()) {
      if (hint == null) {
        hint = _createHintComponent();
        setLayout(new BorderLayout());
      }
      add(hint, SOUTH);
      revalidate();
      repaint();
    }
    else if (hint != null) {
      remove(hint);
      revalidate();
      repaint();
    }

  }


  private Component _createHintComponent() {

    JLabel label = new JLabel("TODO");
    label.setHorizontalAlignment(CENTER);
    label.setForeground(COLOR_FG);
    label.setBorder(createEmptyBorder(4, 4, 4, 4));

    return label;

  }


  private void _selectFile() {

    JFileChooser chooser = new JFileChooser(imageDir);
    chooser.addChoosableFileFilter(new FileNameExtensionFilter(DESC, EXTS));
    chooser.setMultiSelectionEnabled(false);

    if (getImageFile() != null &&
        getImageFile().toURI() != null &&
        getImageFile().toURI().getScheme().equals("file"))
    {
      chooser.setSelectedFile(new File(getImageFile().toURI()));
    }

    if (chooser.showOpenDialog(this) == APPROVE_OPTION) {
      setImageFile(chooser.getSelectedFile());
    }

    imageDir = chooser.getCurrentDirectory();

  }


  // ————————————————————————————————————————————————————————— Protected Methods


  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    Image image = _getCache();

    if (image != null) {
      Insets insets = getInsets();
      g.drawImage(image, insets.left, insets.top, this);
    }

  }


  @Override
  protected void processMouseEvent(MouseEvent e) {

    super.processMouseEvent(e);

    if (e.getID() == MOUSE_PRESSED &&
        e.getClickCount() == 1 &&
       !e.isConsumed())
    {
      _selectFile();
    }

  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public void setBorder(Border border) {
    super.setBorder(border);
    _clearCache();
  }


  @Override
  public void setBounds(int x, int y, int w, int h) {
    super.setBounds(x, y, w, h);
    _clearCache();
  }


  @Override
  public boolean imageUpdate(Image img, int infoflags,
                             int x, int y, int w, int h)
  {
    if (img == getImage()) {
      return super.imageUpdate(img, infoflags, x, y, w, h);
    }
    return false;
  }


  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {

    if (editable != this.editable) {
      if (editable) {
        enableEvents(MOUSE_EVENT_MASK);
        setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        setToolTipText(getMsg(IMAGEVIEWER_SELECT_IMAGE));
      }
      else {
        disableEvents(MOUSE_EVENT_MASK);
        setCursor(Cursor.getDefaultCursor());
        setToolTipText(null);
      }
      _updateHint();
      this.editable = editable;
      firePropertyChange("editable", !editable, editable);
    }

  }


  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    _setImage(image);
    setImageFile(null);
  }


  public File getImageFile() {
    return imageFile;
  }

  public void setImageFile(File file) {

    File old = imageFile;
    imageFile = file;

    if (imageFile != null && imageFile.isFile()) {
      logger.debug(IMAGEVIEWER_READ_FILE, imageFile.getPath());
      _setImage(ImageUtil.getImage(this, imageFile, height, width));
    }

    firePropertyChange("imageFile", old, imageFile);

  }


  public void setImageData(byte[] data) {
    _setImage(ImageUtil.getImage(this, data, height, width));
    setImageFile(null);
  }


  public File getImageDir() {
    return imageDir;
  }


  public void setImageDir(File imageDir) {
    this.imageDir = imageDir;
  }


}