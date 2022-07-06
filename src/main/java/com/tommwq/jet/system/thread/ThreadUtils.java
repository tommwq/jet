package com.tommwq.jet.system.thread;

import com.tommwq.jet.routine.Call;
import com.tommwq.jet.routine.FallibleProcedure;

import java.net.URLClassLoader;

public class ThreadUtils {

  public static URLClassLoader getContextClassLoader() {
    return (URLClassLoader) Thread.currentThread().getContextClassLoader();
  }

  public static SleepResult sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
      return SleepResult.TIMEOUT;
    } catch (InterruptedException e) {
      return SleepResult.INTERRUPTED;
    }
  }

  public static Thread createThread(FallibleProcedure procedure) {
    return new Thread(() -> new Call(() -> procedure.call()));
  }

  public static Thread startThread(FallibleProcedure procedure) {
    Thread thread = createThread(procedure);
    thread.start();
    return thread;
  }

  public static void john(Thread thread) throws InterruptedException {
    try {
      thread.join();
      return;
    } catch (InterruptedException e) {
      throw e;
    }
  }

  public enum SleepResult {
    TIMEOUT, INTERRUPTED
  }
}
