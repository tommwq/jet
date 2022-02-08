package com.tommwq.jet.container;

import java.util.List;

public class RingBuffer<T> {

    public static final int INVALID_POSITION = -1;

    private final int capacity;
    private volatile int position = INVALID_POSITION;
    private final T[] array;

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        array = (T[]) new Object[capacity];
    }

    public int capacity() {
        return capacity;
    }

    public void update(T value) {
        int next = (position + 1) % capacity;
        array[next] = value;
        position = next;
    }

    public T last() {
        if (position == INVALID_POSITION) {
            return null;
        }

        return array[position];
    }

    public int fetch(int startPosition, List<T> output) {
        int currentPosition = position;
        if (currentPosition == INVALID_POSITION) {
            return INVALID_POSITION;
        }

        if (startPosition == INVALID_POSITION) {
            startPosition = 0;
        }

        // TODO optimize
        for (int p = startPosition; p != currentPosition; p++) {
            if (p >= capacity) {
                p %= capacity;
            }

            output.add(array[p]);
        }

        return currentPosition;
    }

    public int position() {
        return position;
    }
}
