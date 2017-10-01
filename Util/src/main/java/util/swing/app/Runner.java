package util.swing.app;


class Runner implements Runnable {


  // —————————————————————————————————————————————————————————————— Constructors


  Runner(Application app, IActionType type, Object... params) {
    this.app = app;
    this.type = type;
    this.params = params;
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final Application app;

  private final IActionType type;

  private final Object[] params;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public void run() {
    app.exec(type, params);
  }


}