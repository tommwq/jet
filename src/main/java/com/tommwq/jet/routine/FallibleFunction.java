package com.tommwq.jet.routine;

@FunctionalInterface
public interface FallibleFunction<T, R> {
  R call(T t) throws Exception;
}
