package util.swing.app;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


class WindowAdapter implements WindowListener {


  // —————————————————————————————————————————————————————————————— Constructors


  WindowAdapter(Application app, IActionType type, Object... params) {
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
  public void windowOpened(WindowEvent e) {
  }


  @Override
  public void windowClosing(WindowEvent e) {
    app.exec(type, params);
  }


  @Override
  public void windowClosed(WindowEvent e) {
  }


  @Override
  public void windowIconified(WindowEvent e) {
  }


  @Override
  public void windowDeiconified(WindowEvent e) {
  }


  @Override
  public void windowActivated(WindowEvent e) {
  }


  @Override
  public void windowDeactivated(WindowEvent e) {
  }


}