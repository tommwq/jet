package com.tommwq.jet.function;

/**
 * a procedure may fail.
 */
@FunctionalInterface
public interface FallibleProcedure {
    void call() throws Exception;
}
