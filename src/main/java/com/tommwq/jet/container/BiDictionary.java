package com.tommwq.jet.container;

import java.util.HashMap;
import java.util.Optional;

/**
 * 双向字典
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class BiDictionary<K, V> {

  private final HashMap<K, V> byKey = new HashMap<>();
  private final HashMap<V, K> byValue = new HashMap<>();

  public void put(K k, V v) {
    byKey.put(k, v);
    byValue.put(v, k);
  }

  public Optional<V> findKey(K k) {
    return Optional.ofNullable(byKey.get(k));
  }

  public Optional<K> findValue(V v) {
    return Optional.ofNullable(byValue.get(v));
  }

  public void removeKey(K k) {
    if (!byKey.containsKey(k)) {
      return;
    }

    V v = byKey.get(k);
    byKey.remove(k);
    byValue.remove(v);
  }

  public void removeValue(V v) {
    if (!byValue.containsKey(v)) {
      return;
    }

    K k = byValue.get(v);

    byKey.remove(k);
    byValue.remove(v);
  }

  public void clear() {
    byKey.clear();
    byValue.clear();
  }

  public boolean containsKey(K k) {
    return byKey.containsKey(k);
  }

  public boolean containsValue(V v) {
    return byValue.containsKey(v);
  }
}
