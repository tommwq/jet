package com.tommwq.jet.routine;

@FunctionalInterface
public interface FallibleSupplier<T> {
    T get() throws Exception;
}
