package com.tommwq.jet.routine;

@FunctionalInterface
public interface FallibleFunction<T, R> {
    R apply(T t) throws Exception;
}
