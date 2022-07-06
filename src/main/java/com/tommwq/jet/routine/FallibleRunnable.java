package com.tommwq.jet.routine;

@FunctionalInterface
public interface FallibleRunnable {
  void run() throws Exception;
}
