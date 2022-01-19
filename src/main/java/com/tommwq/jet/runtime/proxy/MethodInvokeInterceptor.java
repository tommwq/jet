package com.tommwq.jet.runtime.proxy;

import java.lang.reflect.Method;

@FunctionalInterface
public interface MethodInvokeInterceptor {
    void intercept(Object object, Method method, Object[] arguments);
}

