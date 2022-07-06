package com.tommwq.jet.event;

@FunctionalInterface
public interface EventReceiver {
  void receiveEvent(Event<?> aEvent);
}
