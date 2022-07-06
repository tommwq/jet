package com.tommwq.jet.routine;

public class Predictor {
  /**
   * 测试对象不是null
   *
   * @param <T> 对象类型
   * @param t   对象
   * @return 如果对象不是null，返回true；否则返回false
   */
  public static <T> Boolean notNull(T t) {
    return t != null;
  }

  /**
   * 测试对象是null
   *
   * @param <T> 对象类型
   * @param t   对象
   * @return 如果对象是null，返回true；否则返回false
   */
  public static <T> Boolean isNull(T t) {
    return t == null;
  }

  // TODO isInteger isLong isNumber isFloat isDouble isString hasPrefix hasSuffix
}

      
