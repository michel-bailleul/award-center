package util.swing.app;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

import util.collection.CollectionUtil;
import util.collection.IFilter;
import util.collection.IFilterable;


@SuppressWarnings("serial")
public class ListModel<T extends Comparable<T>> extends AbstractListModel<T> implements IFilterable<T> {


  // —————————————————————————————————————————————————————————————— Constructors


  public ListModel() {
    this(true);
  }


  public ListModel(boolean isAutoSort) {
    this.isAutoSort = isAutoSort;
    isActive = true;
    pcs = new PropertyChangeSupport(this);
  }


  public ListModel(List<T> list) {
    this(list, true);
  }


  public ListModel(List<T> list, boolean isAutoSort) {
    this(isAutoSort);
    setList(list);
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private int oldSize;

  private boolean isActive;

  private boolean isAutoSort;

  private final PropertyChangeSupport pcs;

  private T selection;

  private List<T> data;

  private List<T> view;

  private IFilter<T> filter;


  // ——————————————————————————————————————————————————————————— Private Methods


  private boolean _filter(T element) {

    if (view != null && (filter == null || filter.matches(element))) {
      view.add(element);
      if (isAutoSort) {
        sort();
      }
      return true;
    }
    else {
      return false;
    }

  }


  // ————————————————————————————————————————————————————————— Protected Methods


  protected void firePropertyChange(String property, Object oldValue, Object newValue) {
    if (isActive) {
      pcs.firePropertyChange(property, oldValue, newValue);
    }
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }


  public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(property, listener);
  }

  public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(property, listener);
  }


  public void fireSelectedChange() {
    int index = indexOf(selection);
    super.fireContentsChanged(this, index, index);
  }


  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }


  public T getSelection() {
    return selection;
  }

  public void setSelection(T selection) {
    T old = this.selection;
    this.selection = selection;
    firePropertyChange("selection", old, selection);
  }


  public List<T> getList() {
    return data;
  }

  public void setList(List<T> list) {
    data = (list != null) ? list : new ArrayList<T>();
    filter(filter);
    fireContentsChanged(this, 0, view.size() - 1);
  }


  @Override
  public int getSize() {
    return view == null ? 0 : view.size();
  }


  @Override
  public T getElementAt(int index) {
    return (view == null || index < 0 || !(index < view.size())) ? null : view.get(index);
  }


  public int indexOf(T element) {
    return view == null ? -1 : view.indexOf(element);
  }


  public void addElement(T element) {

    if (element != null) {
      if (data == null) {
        data = new ArrayList<T>();
      }
      if (!data.contains(element)) {
        data.add(element);
        if (_filter(element)) {
          int index = indexOf(element);
          fireIntervalAdded(this, index, index);
        }
      }
    }

  }


  public void removeElement(T element) {

    if (element != null && data != null) {
      if (data.remove(element)) {
        int index = indexOf(element);
        if (!(index < 0)) {
          view.remove(element);
          fireIntervalRemoved(this, index, index);
        }
      }
    }

  }


  // sorting -------------------------------------------------------------------


  public boolean isAutoSort() {
    return isAutoSort;
  }

  public void setAutoSort(boolean isAutoSort) {
    this.isAutoSort = isAutoSort;
  }

  public void sort() {
    if (view != null) {
      Collections.sort(view);
      fireContentsChanged(this, 0, view.size() - 1);
    }
  }


  // filtering -----------------------------------------------------------------


  @Override
  public void filter(IFilter<T> filter) {

    this.filter = filter;

    if (data != null) {
      if (filter == null) {
        view = new ArrayList<T>(data);
      }
      else {
        if (view == null) {
          view = new ArrayList<T>();
        }
        CollectionUtil.filter(data, view, filter);
      }

      if (isAutoSort) {
        sort();
      }
      else if (oldSize < view.size()) {
        fireIntervalAdded(this, oldSize, view.size() - 1);
        oldSize = view.size();
      }
      else {
        fireContentsChanged(this, 0, view.size() - 1);
      }

      if (!view.contains(selection)) {
        selection = null;
      }
    }

  }


  public void update() {
    filter(filter);
  }


}