package com.tommwq.jet.container;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K k, V v) {
        key = k;
        value = v;
    }

    public static <K, V> Pair<K, V> make(K k, V v) {
        return new Pair(k, v);
    }

    public K getKey() {
        return key;
    }

    public void setKey(K aKey) {
        key = aKey;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V aValue) {
        value = aValue;
    }
}
