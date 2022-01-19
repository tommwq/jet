package com.tommwq.jet.cache;

import com.tommwq.jet.system.clock.Clock;
import com.tommwq.jet.system.clock.DefaultSystemClock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 缓存
 * <p>
 * 采用被动清除机制。当调用 get 时检查是否超时，若超时，清除缓存。
 */
public class Cache<T> {

    public static int NEVER_EXPIRE = -1;

    private T value;
    private LocalDateTime touchTime;
    private Optional<Duration> expire;
    private Clock clock;

    public Cache(T value, Duration expire, Clock clock) {
        this(value, Optional.of(expire), clock);
    }

    public Cache(T value, Optional<Duration> expire, Clock clock) {
        this.value = value;
        this.expire = expire;
        this.clock = clock;
        touchTime = clock.getTime();
    }

    public Cache() {
        this(null, Optional.empty(), new DefaultSystemClock());
    }

    public Cache(Duration expire) {
        this(null, Optional.of(expire), new DefaultSystemClock());
    }

    public Cache(T value) {
        this(value, Optional.empty(), new DefaultSystemClock());
    }

    /**
     * 获得超时时间
     *
     * @return 超时时间
     */
    public Optional<Duration> getExpire() {
        return expire;
    }

    public Duration mustGetExpire() {
        return expire.get();
    }

    /**
     * 设置超时时间
     *
     * @param expire 超时时间
     */
    public void setExpire(Duration expire) {
        this.expire = Optional.of(expire);
    }

    /**
     * 获取缓存
     */
    public Optional<T> get() {
        clearIfExpired();
        return Optional.ofNullable(value);
    }

    /**
     * 设置缓存
     * <p>
     * set(null)等效于clear()。
     */
    public void set(T object) {
        this.value = object;
        touch();
    }

    /**
     * 更新缓存时间戳。
     */
    public void touch() {
        touchTime = clock.getTime();
    }

    /**
     * 清除缓存
     */
    public void clear() {
        value = null;
    }

    /**
     * 清除超时缓存。
     */
    private void clearIfExpired() {
        if (!expire.isPresent()) {
            return;
        }

        Duration remain = expire.get().minus(Duration.between(touchTime, clock.getTime()));
        if (remain.isZero() || remain.isNegative()) {
            clear();
        }
    }
}
