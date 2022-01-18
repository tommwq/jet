package com.tommwq.jet.routine;

/**
 * a consumer that may fail
 *
 * @param <T> input type
 */
@FunctionalInterface
public interface FallibleConsumer<T> {
    void call(T input) throws Exception;
}
