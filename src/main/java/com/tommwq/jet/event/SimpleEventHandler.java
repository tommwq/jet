package com.tommwq.jet.event;

import java.util.HashMap;

public class SimpleEventHandler<T> extends EventHandler {

    private static final HashMap<Class, EventHandlerFunction> handlerTable = new HashMap<>();

    protected static void registerHandler(Class clazz, EventHandlerFunction handler) {
        handlerTable.put(clazz, handler);
    }

    public final void handleEvent(Event aEvent) {
        Class eventClass = aEvent.getClass();
        EventHandlerFunction handler = handlerTable.get(eventClass);
        if (handler != null) {
            handler.apply(this, aEvent);
        }
    }
}
