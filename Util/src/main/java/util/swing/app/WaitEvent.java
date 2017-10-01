package util.swing.app;


import static java.awt.Toolkit.getDefaultToolkit;


import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.EventQueue;


@SuppressWarnings("serial")
public class WaitEvent extends AWTEvent implements ActiveEvent {


  // —————————————————————————————————————————————————————————————— Constructors


  public WaitEvent() {
    super(new Object(), 0);
    reset();
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private boolean isDispatched;
  private boolean isEmpty;


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public synchronized void dispatch() {

    EventQueue eq = getDefaultToolkit().getSystemEventQueue();
    isEmpty = (eq.peekEvent() == null);
    isDispatched = true;
    notifyAll();

  }


  public synchronized boolean isEmpty() {

    while (!isDispatched) {
      try {
        wait();
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return isEmpty;

  }


  public void reset() {

    isDispatched = false;
    isEmpty = false;

  }


}