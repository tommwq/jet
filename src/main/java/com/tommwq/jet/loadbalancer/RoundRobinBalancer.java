package com.tommwq.jet.loadbalancer;


import com.tommwq.jet.annotation.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;

@NotThreadSafe
public class RoundRobinBalancer<T> implements LoadBalancer<T> {
    private final List<T> list = new ArrayList<>();
    private int index = 0;

    @Override

    public T peek() {
        if (list.isEmpty()) {
            return null;
        }

        if (index >= list.size()) {
            index = 0;
        }

        return list.get(index++);
    }

    @Override
    public void initialize(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
        index = 0;
    }

    @Override
    public void add(T t) {
        list.add(t);
    }

    @Override
    public void remove(T t) {
        list.remove(t);
    }
}
