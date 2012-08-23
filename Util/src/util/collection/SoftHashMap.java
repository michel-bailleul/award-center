package util.collection;


import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SoftHashMap<K,V> extends AbstractMap<K,V> {


  // ————————————————————————————————————————————————————————————— Inner Classes


  private static final class Value<T> extends SoftReference<T> {
    private final Object key;
    private Value(T o, Object key, ReferenceQueue<T> rq) {
      super(o, rq);
      this.key = key;
    }
  }


  // —————————————————————————————————————————————————————————————— Constructors


  public SoftHashMap() {
    map = new HashMap<K,Value<V>>();
    queue = new ReferenceQueue<V>();
  }


  // ———————————————————————————————————————————————————————— Instance Variables


  private final Map<K,Value<V>> map;

  private final ReferenceQueue<V> queue;


  // ——————————————————————————————————————————————————————————— Private Methods


  @SuppressWarnings("unchecked")
  private void _refresh() {
    Value<V> o;
    while ((o = (Value<V>)queue.poll()) != null) {
      map.remove(o.key);
    }
  }


  // ———————————————————————————————————————————————————————————— Public Methods


  @Override
  public V get(Object key) {
    _refresh();
    Value<V> ref = map.get(key);
    return (ref != null) ? ref.get() : null;
  }


  @Override
  public V put(K key, V value) {
    _refresh();
    Value<V> ref = map.put(key, new Value<V>(value, key, queue));
    return (ref != null) ? ref.get() : null;
  }


  @Override
  public V remove(Object key) {
    _refresh();
    Value<V> ref = map.remove(key);
    return (ref != null) ? ref.get() : null;
  }


  @Override
  public void clear() {
    _refresh();
    map.clear();
  }


  @Override
  public int size() {
    _refresh();
    return map.size();
  }


  @Override
  public Set<Entry<K,V>> entrySet() {
    throw new UnsupportedOperationException();
  }


}