package com.tommwq.jet.system.clock;


import java.time.LocalDateTime;

/**
 * 默认系统时钟。
 */
public class DefaultSystemClock implements Clock {

  @Override
  public LocalDateTime getTime() {
    return LocalDateTime.now();
  }
}
