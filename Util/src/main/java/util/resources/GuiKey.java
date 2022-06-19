package util.resources;


import javax.annotation.Resource;

import util.resource.IKey;


@Resource(name="gui")
public enum GuiKey implements IKey {


  // ImageViewer ---------------------------------------------------------------

  IMAGEVIEWER_SELECT_IMAGE ("imageviewer.select.image"),
  IMAGEVIEWER_FILE_TYPE    ("imageviewer.file.type"),


  // GradientPanel -------------------------------------------------------------

  JTEXTFIELDSEARCH_WATERMARK ("jtextfieldsearch.watermark"),
  JTEXTFIELDSEARCH_CLEAR     ("jtextfieldsearch.clear"),

  // Icons ---------------------------------------------------------------------

  ICON_SPACER        ("spacer.png"),
  ICON_MAGNIFIER     ("magnifier.png"),
  ICON_CANCEL_BUTTON ("cross-button.png");


  // —————————————————————————————————————————————————————————————— Constructors


  private GuiKey(String key) {
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
