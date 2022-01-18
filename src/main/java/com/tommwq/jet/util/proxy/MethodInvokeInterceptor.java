package com.tommwq.jet.util.proxy;

import java.lang.reflect.Method;

@FunctionalInterface
public interface MethodInvokeInterceptor {
    void intercept(Object object, Method method, Object[] arguments);
}

