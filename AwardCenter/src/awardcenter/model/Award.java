package awardcenter.model;


import util.swing.app.Bean;


public class Award extends Bean implements Comparable<Award> {


  // —————————————————————————————————————————————————————————————— Constructors


  public Award() { }


  // ———————————————————————————————————————————————————————————————— Properties

  // id ------------------------------------------------------------------------

  private transient Object id;

  public Object getId() {
    return id;
  }

  public void setId(Object file) {
    this.id = file;
  }

  // value ---------------------------------------------------------------------

  private int value;

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    int old = this.value;
    this.value = value;
    firePropertyChange("value", old, value);
  }

  // index ---------------------------------------------------------------------

  private int index;

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    int old = this.index;
    this.index = index;
    firePropertyChange("index", old, index);
  }

  // secret --------------------------------------------------------------------

  private boolean isSecret;

  public boolean isSecret() {
    return isSecret;
  }

  public void setSecret(boolean isSecret) {
    boolean old = this.isSecret;
    this.isSecret = isSecret;
    firePropertyChange("isSecret", old, isSecret);
  }

  // multi ---------------------------------------------------------------------

  private boolean isMulti;

  public boolean isMulti() {
    return isMulti;
  }

  public void setMulti(boolean isMulti) {
    boolean old = this.isMulti;
    this.isMulti = isMulti;
    firePropertyChange("isMulti", old, isMulti);
  }

  // added ---------------------------------------------------------------------

  private boolean isAdded;

  public boolean isAdded() {
    return isAdded;
  }

  public void setAdded(boolean isAdded) {
    boolean old = this.isAdded;
    this.isAdded = isAdded;
    firePropertyChange("isAdded", old, isAdded);
  }

  // achieved ------------------------------------------------------------------

  private boolean isAchieved;

  public boolean isAchieved() {
    return isAchieved;
  }

  public void setAchieved(boolean isAchieved) {
    boolean old = this.isAchieved;
    this.isAchieved = isAchieved;
    firePropertyChange("isAchieved", old, isAchieved);
  }

  // separator -----------------------------------------------------------------

  private boolean isSeparator;

  public boolean isSeparator() {
    return isSeparator;
  }

  public void setSeparator(boolean isSeparator) {
    boolean old = this.isSeparator;
    this.isSeparator = isSeparator;
    firePropertyChange("isSeparator", old, isSeparator);
  }

  // label ---------------------------------------------------------------------

  private String label;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    String old = this.label;
    this.label = label;
    firePropertyChange("label", old, label);
  }

  // description ---------------------------------------------------------------

  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    String old = this.description;
    this.description = description;
    firePropertyChange("description", old, description);
  }

  // tips n tricks -------------------------------------------------------------

  private String tipsAndTricks;

  public String getTipsAndTricks() {
    return tipsAndTricks;
  }

  public void setTipsAndTricks(String tipsAndTricks) {
    String old = this.tipsAndTricks;
    this.tipsAndTricks = tipsAndTricks;
    firePropertyChange("tipsAndTricks", old, tipsAndTricks);
  }

  // image ---------------------------------------------------------------------

  private String image;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  // bytes ---------------------------------------------------------------------

  private transient byte[] bytes;

  public byte[] getBytes() {
    return bytes;
  }

  public void setBytes(byte[] bytes) {
    byte[] old = this.bytes;
    this.bytes = bytes;
    firePropertyChange("bytes", old, bytes);
  }

  // flags ---------------------------------------------------------------------

  private static final int ACHIEVED  = 0b00000001;
  private static final int ADDED     = 0b00000010;
  private static final int MULTI     = 0b00000100;
  private static final int SECRET    = 0b00001000;
  private static final int SEPARATOR = 0b00010000;

  public int getFlags() {
    int flags = 0;
    flags += isAchieved  ? ACHIEVED  : 0;
    flags += isAdded     ? ADDED     : 0;
    flags += isMulti     ? MULTI     : 0;
    flags += isSecret    ? SECRET    : 0;
    flags += isSeparator ? SEPARATOR : 0;
    return flags;
  }

  public void setFlags(int flags) {
    isAchieved  = (flags & ACHIEVED)  > 0;
    isAdded     = (flags & ADDED)     > 0;
    isMulti     = (flags & MULTI)     > 0;
    isSecret    = (flags & SECRET)    > 0;
    isSeparator = (flags & SEPARATOR) > 0;
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String toString() {
    return getLabel();
  }


  @Override
  public int compareTo(Award award) {
    return award != null ? getIndex() - award.getIndex() : 0;
  }


  @Override
  public Award clone() {

    Award clone = new Award();

    clone.setValue(value);
    clone.setIndex(index);
    clone.setSecret(isSecret);
    clone.setMulti(isMulti);
    clone.setAdded(isAdded);
    clone.setAchieved(isAchieved);
    clone.setSeparator(isSeparator);
    clone.setLabel(label);
    clone.setDescription(description);
    clone.setTipsAndTricks(tipsAndTricks);
    clone.setImage(image);
    clone.setBytes(bytes);

    return clone;

  }


}