package com.tommwq.jet.runtime.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

  public static boolean isSameClass(Class class1, Class class2) {
    if (class1 == null || class2 == null) {
      return false;
    }

    return class1.getName().equals(class2.getName());
  }

  public static boolean isSameClass(Object object1, Object object2) {
    if (object1 == null || object2 == null) {
      return false;
    }

    return isSameClass(object1.getClass(), object2.getClass());
  }

  public static List<Method> listAnnotatedMethod(Class<?> clazz, Class<?> annotationClass) {
    List<Method> result = new ArrayList<>();
    for (Method method : clazz.getMethods()) {
      if (isAnnotatedBy(method, annotationClass)) {
        result.add(method);
      }
    }
    return result;
  }

  public static List<Method> listAnnotatedMethod(Class<?> clazz) {
    List<Method> result = new ArrayList<>();
    for (Method method : clazz.getMethods()) {
      if (isAnnotated(method)) {
        result.add(method);
      }
    }
    return result;
  }

  public static boolean isAnnotatedBy(Method method, Class<?> annotationClass) {
    for (Annotation annotation : method.getDeclaredAnnotations()) {
      if (annotation.annotationType() == annotationClass) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAnnotated(Method method) {
    return method.getDeclaredAnnotations().length > 0;
  }

  public static <T> T create(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    return create(Class.forName(className));
  }

  public static <T> T create(Class<?> clazz) throws IllegalAccessException, InstantiationException {
    return (T) clazz.newInstance();
  }

  /**
   * 判断 subClass 是否从 baseClass 派生。
   */
  public static boolean isDerivedFrom(Class<?> subClass, Class<?> baseClass) {
    if (subClass == null || baseClass == null) {
      return false;
    }

    Class<?> parent = subClass.getSuperclass();
    while (parent != null) {
      if (parent == baseClass) {
        return true;
      }
      parent = parent.getSuperclass();
    }
    return false;
  }
}
