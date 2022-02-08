package com.tommwq.jet.container;

import java.util.ArrayList;

public class SimpleStack<T> implements Stack<T> {

    private ArrayList<T> underlying = new ArrayList();

    public void push(T element) {
        underlying.add(0, element);
    }

    public T top() {
        if (underlying.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return underlying.get(0);
    }

    public void pop() {
        if (underlying.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        underlying.remove(0);
    }

    public boolean isEmpty() {
        return underlying.isEmpty();
    }

    public void bringToTop(T element) {
        if (!underlying.remove((Object) element)) {
            throw new UnsupportedOperationException();
        }

        underlying.add(0, element);
    }

    public int size() {
        return underlying.size();
    }
}
