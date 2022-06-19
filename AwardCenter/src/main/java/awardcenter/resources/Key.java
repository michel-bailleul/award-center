package awardcenter.resources;


import javax.annotation.Resource;

import util.resource.IKey;


@Resource(name="messages", lookup="icon")
public enum Key implements IKey {


  // —————————————————————————————————————————————————————————————————————— Text


  APP_NAME ("app.name"),
  APP_ALREADY_EXISTS  ("app.already.exists"),
  APP_ALREADY_RUNNING ("app.already.running"),

  MENU_FILE   ("menu.file"),
  MENU_IMPORT ("menu.import"),
  MENU_EXPORT ("menu.export"),
  MENU_SAVE   ("menu.save"),
  MENU_EXIT   ("menu.exit"),

  MENU_GAME        ("menu.game"),
  MENU_GAME_INSERT ("menu.game.insert"),
  MENU_GAME_UPDATE ("menu.game.update"),
  MENU_GAME_DELETE ("menu.game.delete"),

  MENU_AWARD           ("menu.award"),
  MENU_AWARD_INSERT    ("menu.award.insert"),
  MENU_AWARD_DELETE    ("menu.award.delete"),
  MENU_AWARD_SEPARATOR ("menu.award.separator"),

  MENU_HELP       ("menu.help"),
  MENU_HELP_ABOUT ("menu.help.about"),

  GAME_NAME      ("game.name"),
  GAME_DEVELOPER ("game.developer"),
  GAME_PUBLISHER ("game.publisher"),
  GAME_RATING    ("game.rating"),
  GAME_SCORE     ("game.score"),
  GAME_NEW       ("game.new"),

  GAME_DELETE_DIALOG_TITLE ("game.delete.dialog.title"),
  GAME_DELETE_DIALOG_MSG   ("game.delete.dialog.msg"),

  AWARD_LABEL         ("award.label"),
  AWARD_DESCRIPTION   ("award.description"),
  AWARD_TIPSNTRICKS   ("award.tipsntricks"),
  AWARD_SECRET        ("award.secret"),
  AWARD_MULTI         ("award.multi"),
  AWARD_ADDED         ("award.added"),
  AWARD_FIRST         ("award.first"),
  AWARD_PREVIOUS      ("award.previous"),
  AWARD_NEXT          ("award.next"),
  AWARD_LAST          ("award.last"),
  AWARD_PLUS          ("award.plus"),
  AWARD_MINUS         ("award.minus"),
  AWARD_ACHIEVED_OK   ("award.achieved.ok"),
  AWARD_ACHIEVED_KO   ("award.achieved.ko"),
  AWARD_NEW           ("award.new"),
  AWARD_NEW_SEPARATOR ("award.new.separator"),

  AWARD_DELETE_DIALOG_TITLE ("award.delete.dialog.title"),
  AWARD_DELETE_DIALOG_MSG   ("award.delete.dialog.msg"),

  DIALOG_IMPEXP_TITLE_IMPORT    ("dialog.impexp.title.import"),
  DIALOG_IMPEXP_TITLE_EXPORT    ("dialog.impexp.title.export"),
  DIALOG_IMPEXP_DIR_IMPORT      ("dialog.impexp.dir.import"),
  DIALOG_IMPEXP_DIR_EXPORT      ("dialog.impexp.dir.export"),
  DIALOG_IMPEXP_PARAM_IMPORT    ("dialog.impexp.param.import"),
  DIALOG_IMPEXP_PARAM_EXPORT    ("dialog.impexp.param.export"),
  DIALOG_IMPEXP_BUTTON_BROWSE   ("dialog.impexp.button.browse"),
  DIALOG_IMPEXP_BUTTON_IMPORT   ("dialog.impexp.button.import"),
  DIALOG_IMPEXP_BUTTON_EXPORT   ("dialog.impexp.button.export"),
  DIALOG_IMPEXP_BUTTON_SELECT   ("dialog.impexp.button.select"),
  DIALOG_IMPEXP_BUTTON_UNSELECT ("dialog.impexp.button.unselect"),
  DIALOG_IMPEXP_BUTTON_CLOSE    ("dialog.impexp.button.close"),

  DIALOG_INPUT_TITLE       ("dialog.input.title"),


  // ————————————————————————————————————————————————————————————————————— Icons

  // Fugue Icons ---------------------------------------------------------------

  ICON_APP16     ("trophy.png"),
  ICON_IMPORT    ("inbox-download.png"),
  ICON_EXPORT    ("inbox-upload.png"),
  ICON_SAVE      ("disk-black.png"),
  ICON_EXIT      ("door-open.png"),
  ICON_ABOUT     ("information-frame.png"),

  ICON_ADD       ("plus-circle-frame.png"),
  ICON_EDIT      ("sticky-note-pencil.png"),
  ICON_DELETE    ("minus-circle-frame.png"),
  ICON_SPLIT     ("ui-splitter-horizontal.png"),

  ICON_FIRST     ("control-double-180.png"),
  ICON_PREVIOUS  ("control-180.png"),
  ICON_NEXT      ("control.png"),
  ICON_LAST      ("control-double.png"),

  ICON_PLUS      ("toggle-small-expand.png"),
  ICON_MINUS     ("toggle-small.png"),

  ICON_TICK      ("tick.png"),
  ICON_LOCK      ("lock.png"),

  ICON_SECRET    ("question-frame.png"),
  ICON_MULTI     ("users.png"),
  ICON_ADDED     ("puzzle.png"),
  ICON_UNCHECK   ("layer-small.png"),

  ICON_STAR             ("star.png"),
  ICON_STAR_HALF        ("star-half.png"),
  ICON_STAR_EMPTY       ("star-empty.png"),
  ICON_STAR_SMALL       ("star-small.png"),
  ICON_STAR_SMALL_HALF  ("star-small-half.png"),
  ICON_STAR_SMALL_EMPTY ("star-small-empty.png");


  // —————————————————————————————————————————————————————————————— Constructors


  private Key() {
  }


  private Key(String key) {
    this.key = key;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private String key;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public String getKey() {
    return key;
  }


}
