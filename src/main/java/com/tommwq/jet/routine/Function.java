/**
 * Function.java
 * 函数对象。
 * <p>
 * 2017年1月24日。
 */

package com.tommwq.jet.routine;

/**
 * Function函数对象
 * 用于替代java.util.function.Function。在结合CompletableFuture使用时可以简化代码。
 */
@FunctionalInterface
public interface Function<T, R> {
    R apply(T arg) throws Exception;
}
