package com.tommwq.jet.function;

/**
 * a function that may fail.
 *
 * @param <T> return type
 */
@FunctionalInterface
public interface FallibleFunction<T, R> {
    R call(T input) throws Exception;
}
