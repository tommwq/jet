package com.tommwq.jet.event.id;

/**
 * Id生成器。
 */
public interface IdGenerator {
  /**
   * 生成Id
   *
   * @return 返回生成的Id
   */
  Id nextId();
}

