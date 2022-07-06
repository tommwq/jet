package com.tommwq.jet.routine;

@FunctionalInterface
public interface InterruptibleFunction<T, R> {
  @SuppressWarnings("RedundantThrows")
  R apply(T t) throws InterruptedException;
}
