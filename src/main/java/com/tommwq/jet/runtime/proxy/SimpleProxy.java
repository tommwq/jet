package com.tommwq.jet.runtime.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SimpleProxy implements InvocationHandler {

  private final Object target;
  private MethodInvokeInterceptor before = null;
  private MethodInvokeInterceptor after = null;

  private SimpleProxy(Object target, MethodInvokeInterceptor before, MethodInvokeInterceptor after) {
    this.target = target;
    this.before = before;
    this.after = after;
  }

  public static <T> T create(T target, MethodInvokeInterceptor before, MethodInvokeInterceptor after) {
    SimpleProxy p = new SimpleProxy(target, before, after);
    Class clazz = target.getClass();
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), p);
  }

  public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {

    try {
      if (before != null) {
        before.intercept(target, method, arguments);
      }

      return method.invoke(target, arguments);
    } finally {
      if (after != null) {
        after.intercept(target, method, arguments);
      }
    }
  }
}

