package com.tommwq.jet.cache;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存组
 * <p>
 * 管理一组超时时间相同的缓存对象。
 */
public class CacheGroup {
    private Optional<Duration> defaultExpire = Optional.empty();
    private final Map<String, Cache<Object>> group = new ConcurrentHashMap<>(16);

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 缓存
     */
    public void set(String key, Object value) {
        Cache<Object> cache = group.getOrDefault(key, new Cache<>());
        cache.set(value);
        group.put(key, cache);
    }

    /**
     * 查询缓存
     *
     * @param key 键
     * @param <T> 缓存类型
     * @return 缓存值
     */
    public <T> Optional<T> get(String key) {
        Cache<Object> cache = group.getOrDefault(key, new Cache<>());
        T value = (T) cache.get().orElse(null);
        return Optional.ofNullable(value);
    }

    /**
     * 设置缓存超时时间
     *
     * @param key          键
     * @param expireSecond 超时时间
     */
    public void setExpireSecond(String key, long expireSecond) {
        group.getOrDefault(key, new Cache<>()).setExpire(Duration.ofSeconds(expireSecond));
    }

    public void setExpire(String key, Duration expire) {
        group.getOrDefault(key, new Cache<>()).setExpire(expire);
    }

    /**
     * 查询缓存超时时间
     *
     * @param key 键
     */
    public Optional<Duration> getExpire(String key) {
        return group.getOrDefault(key, new Cache<>()).getExpire();
    }

    /**
     * 刷新缓存更新时间
     *
     * @param key 键
     */
    public void touch(String key) {
        group.getOrDefault(key, new Cache<>()).touch();
    }

    /**
     * 刷新所有缓存更新时间
     */
    public void touchAll() {
        group.values().forEach(cache -> cache.touch());
    }

    /**
     * 清除缓存
     *
     * @param key 键
     */
    public void clear(String key) {
        group.getOrDefault(key, new Cache<>()).clear();
        group.remove(key);
    }

    /**
     * 清除所有缓存
     */
    public void clearAll() {
        group.clear();
    }

    /**
     * 查询默认超时时间
     *
     * @return 默认超时时间
     */
    public Optional<Duration> getDefaultExpire() {
        return defaultExpire;
    }

    /**
     * 设置默认超时时间
     *
     * @param defaultExpire 默认超时时间
     */
    public void setDefaultExpire(Duration defaultExpire) {
        this.defaultExpire = Optional.of(defaultExpire);
    }
}
