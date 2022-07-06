package com.tommwq.jet.routine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;

// TODO
public class Task {

  volatile Object result = null;
  volatile boolean done = false;
  volatile Throwable error = null;
  volatile boolean cancelled = false;
  Task next = null;
  Task previous = null;
  InterruptableSupplier<Object> supplier = null;
  Task parent = null;
  List<Task> children = new LinkedList<>();

  private Task() {
  }

  public static Task parallel(InterruptableSupplier... tasks) {
    Task[] children = new Task[tasks.length];
    for (int i = 0; i < tasks.length; i++) {
      children[i] = single(tasks[i]);
    }
    return parallel(children);
  }

  public static Task parallel(Task... tasks) {
    Task parent = new Task();
    parent.children = new LinkedList<>(Arrays.asList(tasks));
    for (Task t : tasks) {
      t.parent = parent;
    }
    return parent;
  }

  public static Task sequence(Task... tasks) {
    Task parent = new Task();
    Task prev = parent;
    for (Task t : tasks) {
      prev.next = t;
      t.previous = prev;
      prev = t;
    }
    return parent;
  }

  public static Task single(InterruptableSupplier supplier) {
    Task t = new Task();
    t.supplier = supplier;
    return t;
  }

  public static Task anyOf(Task... tasks) {
    return new Task();
  }

  public void cancel() {
    cancelled = true;
    if (supplier != null) {
      supplier.interrupt();
    }
    for (Task t : children) {
      t.cancel();
    }
    Task nextTask = next;
    while (nextTask != null) {
      nextTask.cancel();
      nextTask = nextTask.next;
    }
  }

  public TaskResult getTaskResult() {
    TaskResult taskResult = new TaskResult();
    taskResult.value = result;
    taskResult.children = new ArrayList<>(children.size());
    for (Task child : children) {
      taskResult.children.add(child.getTaskResult());
    }
    return taskResult;
  }

  public void execute(Executor executor) {

    executor.execute(() -> {
        if (cancelled) {
          return;
        }

        if (supplier != null) {
          try {
            if (previous != null) {
              previous.awaitSelfAndChildren();
            }
            result = supplier.get();
          } catch (Throwable e) {
            error = e;
          } finally {
            done = true;
          }
        }

        done = true;
        if (cancelled) {
          return;
        }
        for (Task t : children) {
          t.execute(executor);
        }

        if (next != null) {
          next.execute(executor);
        }
      });
  }

  private boolean isAllDone() {
    if (!isSelfAndChildrenDone()) {
      return false;
    }

    if (next == null) {
      return true;
    }

    return next.isAllDone();
  }

  public boolean isSelfAndChildrenDone() {
    return done && children.stream().allMatch(Task::isAllDone);
  }

  public void awaitSelfAndChildren() throws InterruptedException {
    if (cancelled) {
      throw new CancellationException();
    }

    while (!isSelfAndChildrenDone()) {
      Thread.sleep(50);
    }
  }

  public void await() throws InterruptedException {
    if (cancelled) {
      throw new CancellationException();
    }

    while (!isAllDone()) {
      Thread.sleep(50);
    }
  }

  Task getParent() {
    return parent;
  }

  void addChild(Task t) {
    children.add(t);
    t.parent = this;
  }

  List<Task> getChildren() {
    return children;
  }

  boolean haveChild() {
    return !children.isEmpty();
  }

  public int getTaskChainLength() {
    int count = 1;
    Task n = next;
    while (n != null) {
      count++;
      n = n.next;
    }
    return count;
  }

  private enum Status {
    READY, RUNNING, CANCELLED, COMPLETED
  }
}
