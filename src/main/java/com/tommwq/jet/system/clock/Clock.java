package com.tommwq.jet.system.clock;


import java.time.LocalDateTime;

/**
 * 时钟
 */
public interface Clock {
    /**
     * 获得当前时间
     *
     * @return 当前时间
     */
    LocalDateTime getTime();
}
