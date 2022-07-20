package com.tommwq.jet.function.filter;

import com.tommwq.jet.runtime.reflect.ReflectUtils;

import java.util.function.Predicate;

/**
 * 域过滤器
 *
 * 按域的值过滤对象。
 *
 */
public class ByFieldFilter<T> implements Predicate<T> {
  private String fieldName;
  private String targetValue;

  public ByFieldFilter(String fieldName, Object targetValue) {
    this.fieldName = fieldName;
    this.targetValue = String.valueOf(targetValue);
  }

  @Override
  public boolean test(T t) {
    try {
      return targetValue.equals(ReflectUtils.getField(t, fieldName));
    } catch (Exception e) {
      return false;
    }
  }
}
