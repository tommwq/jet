package com.tommwq.jet.system.thread;

import java.util.Optional;

/**
 * 表示一个异步结果
 *
 * @param <T> 结果类型
 */
public interface AsyncResult<T> {
  /**
   * 获得结果
   *
   * @return
   */
  Optional<T> getResult();

  Optional<Throwable> getException();

  boolean isSuccess();

  boolean isFailure();

  boolean isFinished();
}
