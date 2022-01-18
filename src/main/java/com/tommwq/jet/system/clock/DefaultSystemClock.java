package com.tommwq.jet.system.clock;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * 默认系统时钟。
 */
public class DefaultSystemClock implements Clock {

    @Override
    @Nonnull
    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}
