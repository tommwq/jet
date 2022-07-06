package com.tommwq.jet.runtime.concurrent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Agency是一个服务代理。Agency负责向后端实际服务发送请求，并接收应答。
 */
public abstract class Agency {

  DispatchThread dispatchThread = new DispatchThread();
  TimeoutCheckThread timeoutCheckThread = new TimeoutCheckThread();
  HeartbeatThread heartbeatThread = new HeartbeatThread();
  private boolean running;
  private final Map<Long, Context> contextes = new ConcurrentHashMap();
  private long receiptId = 0;

  public Agency() {
  }

  private long generateReceiptId() {
    return receiptId++;
  }

  public Receipt post(Object request) {
    long rid = generateReceiptId();
    Receipt receipt = new Receipt(rid, request);
    contextes.put(receipt.id(), new Context(receipt));
    send(receipt.id(), request);
    return receipt;
  }

  public void start() {
    connect();
    login();

    running = true;

    dispatchThread.start();
    timeoutCheckThread.start();
    heartbeatThread.start();
  }

  public void stop() {
    running = false;
    logoff();
    disconnect();

    dispatchThread.interrupt();
    timeoutCheckThread.interrupt();
    Thread.interrupted();
  }

  public Optional<Context> getContext(long rid) {
    if (!contextes.containsKey(rid)) {
      return Optional.ofNullable(null);
    }
    return Optional.of(contextes.get(rid));
  }

  public Optional<Object> getContext(long rid, String key) {
    if (!contextes.containsKey(rid)) {
      return Optional.ofNullable(null);
    }
    return contextes.get(rid).find(key);
  }

  public void setContext(long rid, String key, Object value) {
    Optional<Context> ctx = getContext(rid);
    if (!ctx.isPresent()) {
      return;
    }
    ctx.get().put(key, value);
  }

  public void removeContext(long rid) {
    contextes.remove(rid);
  }

  public void receiveAndDispatchCycle() {
    while (running) {
      try {
        boolean received = receive();
        if (!received) {
          sleep();
        }
      } catch (InterruptedException e) {
        break;
      }
    }
  }

  public void heartbeatCycle() {
    while (running) {
      try {
        System.out.println("HEARTBEAT");
        heartbeat();
        sleep();
      } catch (Exception e) {
        if (!running) {
          break;
        }
      }
    }
  }

  public abstract void connect();

  public abstract void send(long receiptId, Object request);

  public abstract boolean receive() throws InterruptedException;

  public void disconnect() {
  }

  public void login() {
  }

  public void heartbeat() throws Exception {
  }

  public void logoff() {
  }

  public void sleep() throws InterruptedException {
    Thread.sleep(100);
  }

  public void timeoutCheckCycle() {
    while (running) {
      /* TODO
         lock
         checkTimeout
         unlock
      */
      try {
        sleep();
      } catch (InterruptedException e) {
        // ignore
      }
    }
  }

  private class DispatchThread extends Thread {
    public void run() {
      receiveAndDispatchCycle();
    }
  }
  //public void reconnect() {}

  private class TimeoutCheckThread extends Thread {
    public void run() {
      timeoutCheckCycle();
    }
  }

  public class HeartbeatThread extends Thread {
    public void run() {
      heartbeatCycle();
    }
  }


}
