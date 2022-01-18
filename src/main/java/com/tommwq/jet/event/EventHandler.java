package com.tommwq.jet.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventHandler implements EventReceiver {
    private final HashMap<Class<?>, List<Method>> registeredHandlers = new HashMap<>();

    public void receiveEvent(Event<?> aEvent) {
        if (aEvent == null) {
            return;
        }

        Class<?> eventClass = aEvent.getClass();
        List<Method> methods = registeredHandlers.get(eventClass);
        if (methods.isEmpty()) {
            onUnregisteredEvent(aEvent);
            return;
        }

        for (Method method : methods) {
            invokeMethod(method, aEvent);
        }
    }

    private void invokeMethod(Method method, Event<?> aEvent) {
        try {
            if (method.getParameterCount() == 0) {
                method.invoke(this);
            } else {
                method.invoke(this, aEvent);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onUnregisteredEvent(Event<?> aEvent) {
    }

    /**
     * 绑定事件处理句柄。
     * <p>
     * 派生类必须在构造函数中调用这个方法。
     */
    protected void bind() {
        for (Method method : getClass().getMethods()) {
            OnEvent onEvent = method.getAnnotation(OnEvent.class);
            if (onEvent == null) {
                continue;
            }
            Class<?> eventClass = onEvent.value();
            List<Method> methods = registeredHandlers.getOrDefault(eventClass, new ArrayList<>());
            methods.add(method);
            registeredHandlers.put(eventClass, methods);
        }
    }
}
