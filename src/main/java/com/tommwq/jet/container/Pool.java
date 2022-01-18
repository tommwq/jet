package com.tommwq.jet.container;

import java.util.HashMap;
import java.util.Map;

public class Pool<T> {

    private Map<Integer, T> container;
    private HashFunction<T> hash;
    public Pool() {
        hash = Pool::defaultHash;
        container = new HashMap<>();
    }

    public Pool(HashFunction<T> aHash) {
        hash = aHash;
        container = new HashMap<>();
    }

    public Pool(HashFunction<T> aHash, Map<Integer, T> aContainer) {
        hash = aHash;
        container = aContainer;
    }

    private static int defaultHash(Object t) {
        return t.hashCode();
    }

    public void clear() {
        container.clear();
    }

    public void put(T object) {
        int key = hash.hash(object);
        if (container.containsKey(key)) {
            return;
        }

        container.put(key, object);
    }

    public T get(T object) {
        int key = hash.hash(object);
        if (container.containsKey(key)) {
            return container.get(key);
        }

        container.put(key, object);
        return container.get(key);
    }

    public interface HashFunction<T> {
        int hash(T value);
    }
}
