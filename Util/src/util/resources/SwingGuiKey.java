package util.resources;


import util.resource.FileName;
import util.resource.IKey;


@FileName("swing-gui")
public enum SwingGuiKey implements IKey {


  // ImageViewer ---------------------------------------------------------------

  IMAGEVIEWER_SELECT_IMAGE ("imageviewer.select.image"),
  IMAGEVIEWER_FILE_TYPE    ("imageviewer.file.type"),


  // GradientPanel -------------------------------------------------------------

  JTEXTFIELDSEARCH_WATERMARK ("jtextfieldsearch.watermark"),
  JTEXTFIELDSEARCH_CLEAR     ("jtextfieldsearch.clear"),

  // --------------------------------------------------------------------- Icons

  ICON_SPACER        ("icon/spacer.png"),
  ICON_MAGNIFIER     ("icon/magnifier.png"),
  ICON_CANCEL_BUTTON ("icon/cross-button.png");


  // —————————————————————————————————————————————————————————————— Constructors


  private SwingGuiKey(String key) {
    this.key = key;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final String key;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String getKey() {
    return key;
  }


}