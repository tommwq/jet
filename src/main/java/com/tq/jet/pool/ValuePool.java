package com.tq.jet.pool;


import java.util.concurrent.ConcurrentHashMap;

public class ValuePool<T> {
    private ConcurrentHashMap<Integer, T> values = new ConcurrentHashMap<>();
    private ValueHash<T> hash;

    public ValuePool(ValueHash<T> hash) {
        this.hash = hash;
    }

    public void clear() {
        values.clear();
    }

    public void put(T object) {
        int key = hash.valueHash(object);
        if (values.containsKey(key)) {
            return;
        }

        values.put(key, object);
    }

    public T get(T object) {
        int key = hash.valueHash(object);
        if (values.containsKey(key)) {
            return values.get(key);
        }

        values.put(key, object);
        return values.get(key);
    }

    public interface ValueHash<T> {
        int valueHash(T value);
    }
}
