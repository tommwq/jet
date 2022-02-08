package com.tommwq.jet.function;

/**
 * Function函数对象
 * 用于替代java.jet.function.Function。在结合CompletableFuture使用时可以简化代码。
 */
@FunctionalInterface
public interface Function<T, R> {
    R apply(T arg);
}
