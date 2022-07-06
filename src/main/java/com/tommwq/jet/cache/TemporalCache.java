package com.tommwq.jet.cache;

import com.tommwq.jet.annotation.NotThreadSafe;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 保存临时对象的缓存。
 *
 * @param <T> 临时对象类型。
 */
@NotThreadSafe
public class TemporalCache<T> {
  private final Supplier<T> supplier;
  private Function<Exception, T> exceptionHandler = null;
  private T cached = null;

  /**
   * 创建临时缓存。
   *
   * @param supplier 返回缓存对象的函数
   */
  public TemporalCache(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  /**
   * 创建临时缓存。
   *
   * @param supplier 返回缓存对象的函数
   * @param exceptionHandler 处理supplier异常的函数
   */
  public TemporalCache(Supplier<T> supplier, Function<Exception, T> exceptionHandler) {
    this.supplier = supplier;
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * 创建临时缓存。
   *
   * @param supplier 返回缓存对象的函数
   * @param exceptionConsumer 处理supplier异常的函数
   */
  public TemporalCache(Supplier<T> supplier, Consumer<Exception> exceptionConsumer) {
    this.supplier = supplier;
    this.exceptionHandler = (error) -> {
      exceptionConsumer.accept(error);
      return null;
    };
  }

  /**
   * 清理缓存。
   */
  public void clear() {
    cached = null;
  }

  /**
   * 更新缓存值。
   * @return 值
   */
  public T refresh() {
    clear();
    return get();
  }

  /**
   * 活动值。
   * @return 值
   */
  public T get() {
    if (cached != null) {
      return cached;
    }
    
    try {
      cached = supplier.get();
      return cached;
    } catch (Exception exception) {
      if (exceptionHandler != null) {
        cached = exceptionHandler.apply(exception);
        return cached;
      }
      throw exception;
    }
  }
}


