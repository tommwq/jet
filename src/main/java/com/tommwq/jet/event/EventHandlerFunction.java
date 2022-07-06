package com.tommwq.jet.event;

public interface EventHandlerFunction<T> {
  void apply(T object, Event aEvent);
}
