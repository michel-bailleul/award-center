package util.swing.app;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class ActionAdapter implements ActionListener {


  // —————————————————————————————————————————————————————————————— Constructors


  ActionAdapter(Application app, IActionType type, Object... params) {
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
  public void actionPerformed(ActionEvent e) {
    app.exec(type, params);
  }


}