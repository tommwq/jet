/**
 * Function.java
 * 函数对象。
 *
 * 2017年1月24日。
 */

package com.tq.jet.util;

/**
 * Function函数对象
 * 用于替代java.jet.function.Function。在结合CompletableFuture使用时可以简化代码。
 */
@FunctionalInterface
public interface Function<T,R> {
        R apply(T arg);
}
