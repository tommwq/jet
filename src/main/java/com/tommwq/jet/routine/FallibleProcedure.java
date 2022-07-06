package com.tommwq.jet.routine;

/**
 * a procedure may fail.
 */
@FunctionalInterface
public interface FallibleProcedure {
  void call() throws Exception;
}
