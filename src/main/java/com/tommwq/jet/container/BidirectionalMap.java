package com.tommwq.jet.container;

import java.util.HashMap;

/**
 * 双向字典
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class BidirectionalMap<K, V> {

    private HashMap<K, V> table1 = new HashMap<>();
    private HashMap<V, K> table2 = new HashMap<>();

    public void put(K k, V v) {
        table1.put(k, v);
        table2.put(v, k);
    }

    public V getByKey(K k) {
        return table1.get(k);
    }

    public K getByValue(V v) {
        return table2.get(v);
    }

    public void removeByKey(K k) {
        if (!table1.containsKey(k)) {
            return;
        }

        V v = table1.get(k);
        table1.remove(k);
        table2.remove(v);
    }

    public void removeByValue(V v) {
        if (!table2.containsKey(v)) {
            return;
        }

        K k = table2.get(v);
        table1.remove(k);
        table2.remove(v);
    }

    public boolean containsKey(K k) {
        return table1.containsKey(k);
    }

    public boolean containsValue(V v) {
        return table2.containsKey(v);
    }
}
