package com.tq.jet.time;

/**
 * 默认时钟。封装System.currentTimeMillis()。
 */
public class DefaultSystemClock implements Clock {
        public long now() {
                return System.currentTimeMillis();
        }
}
