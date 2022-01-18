package com.tommwq.jet.system.io.storage;

import java.util.HashMap;
import java.util.Optional;

/**
 * 简单存储模型。
 */
public class VolatileStorage implements Storage {
    private HashMap<String, Object> container;

    public Object read(String path) throws IllegalArgumentException {
        if (!container.containsKey(path)) {
            throw new IllegalArgumentException();
        }

        return container.get(path);
    }

    public void write(String path, Object data) throws UnsupportedOperationException {
        container.put(path, data);
    }

    public void mustWrite(String path, Object data) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Optional<Object> lookup(String path) {
        if (!container.containsKey(path)) {
            return Optional.empty();
        }

        return Optional.ofNullable(container.get(path));
    }

    public void remove(String path) {
        container.remove(path);
    }

    public void removeAll() {
        container.clear();
    }

    public void sync() {
    }

    public boolean isSupportDurable() {
        return false;
    }
}
