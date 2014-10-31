package util.swing.app;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.Transient;


/**
 * Root of beans requiring properties management features from
 * {@link java.beans.PropertyChangeSupport PropertyChangeSupport}
 */
public abstract class Bean {


  // —————————————————————————————————————————————————————————————— Constructors


  protected Bean() {
    isActive = true;
    isDirty = false;
    pcs = new PropertyChangeSupport(this);
    addPropertyChangeListener(
      new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
//          if (evt.getOldValue() != evt.getNewValue() &&
//              ((evt.getOldValue() != null &&
//               !evt.getOldValue().equals(evt.getNewValue())) ||
//               (evt.getNewValue() != null &&
//               !evt.getNewValue().equals(evt.getOldValue()))))
//          {
            setDirty(true);
//          }
        }
      }
    );
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private transient boolean isActive;

  private transient boolean isDirty;

  private transient final PropertyChangeSupport pcs;


  // ————————————————————————————————————————————————————————— Protected Methods


  /**
   * @see java.beans.PropertyChangeSupport#firePropertyChange(String, Object, Object)
   */
  protected void firePropertyChange(String property, Object oldValue, Object newValue) {
    if (isActive) {
      pcs.firePropertyChange(property, oldValue, newValue);
    }
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  /**
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  /**
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String, PropertyChangeListener)
   */
  public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(property, listener);
  }

  /**
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  /**
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String, PropertyChangeListener)
   */
  public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(property, listener);
  }


  /**
   * Status of the listeners
   * @return {@code true},  if the listeners are active<br/>
   *         {@code false}, if the listeners are inactive
   */
  @Transient
  public boolean isActive() {
    return isActive;
  }

  /**
   * Enable or disable the listeners
   * @param isActive - The new status of the listeners
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }


  /**
   * Status of this bean
   * @return {@code true},  if the bean is dirty<br/>
   *         {@code false}, if the bean is not dirty
   */
  @Transient
  public boolean isDirty() {
    return isDirty;
  }

  /**
   * Modify the status of this bean
   * @param isDirty - The new status of the bean
   */
  public void setDirty(boolean isDirty) {
    this.isDirty = isDirty;
  }


}