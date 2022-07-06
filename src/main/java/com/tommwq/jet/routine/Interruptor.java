package com.tommwq.jet.routine;

import java.lang.ref.WeakReference;

/**
 * 中断器。
 * <p>
 * 用于绑定执行线程，以便中断 Runnable 对象。
 */
public class Interruptor implements Interruptable {
  private WeakReference<Thread> thread = new WeakReference<>(null);
  private boolean interrupted = false;

  public synchronized void bindThread() {
    thread = new WeakReference<>(Thread.currentThread());
  }

  public synchronized void unbindThread() {
    thread = new WeakReference<>(null);
  }

  public synchronized void interrupt() {
    interrupted = true;
    Thread t = thread.get();
    if (t != null) {
      t.interrupt();
    }
  }

  public synchronized boolean isInterrupted() {
    return interrupted;
  }
}
