package util.swing.app;


public class ListController<T> extends Bean {


  // ———————————————————————————————————————————————————————— Instance Variables


  private T selection;


  // ———————————————————————————————————————————————————————————— Public Methods


  public T getSelection() {
    return selection;
  }


  public void setSelection(T selection) {
    T old = this.selection;
    this.selection = selection;
    firePropertyChange("selection", old, selection);
  }


}