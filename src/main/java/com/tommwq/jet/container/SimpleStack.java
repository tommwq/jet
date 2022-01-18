package com.tommwq.jet.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

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

    public T pop() {
        if (underlying.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return underlying.remove(0);
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

    public void forEach(Consumer<? super T> action) {
        underlying.forEach(action);
    }

    public Iterator<T> iterator() {
        return underlying.iterator();
    }

    public Spliterator<T> spliterator() {
        return underlying.spliterator();
    }

    public int size() {
        return underlying.size();
    }
}
