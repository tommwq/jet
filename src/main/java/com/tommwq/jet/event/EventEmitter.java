package com.tommwq.jet.event;

import com.tommwq.jet.util.RingBuffer;

public abstract class EventEmitter<T> implements Runnable {
    private RingBuffer<T> outputBuffer;

    public EventEmitter setOutput(RingBuffer<T> outputBuffer) {
        this.outputBuffer = outputBuffer;
        return this;
    }

    public EventEmitter emit(T event) {
        outputBuffer.update(event);
        return this;
    }
}
