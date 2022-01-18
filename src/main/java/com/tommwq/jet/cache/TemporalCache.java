package com.tommwq.jet.cache;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
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
    private Supplier<T> supplier;
    private Function<Exception, T> exceptionHandler = null;
    private T cached = null;

    public TemporalCache(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public TemporalCache(Supplier<T> supplier, Function<Exception, T> exceptionHandler) {
        this.supplier = supplier;
        this.exceptionHandler = exceptionHandler;
    }

    public TemporalCache(Supplier<T> supplier, Consumer<Exception> exceptionConsumer) {
        this.supplier = supplier;
        this.exceptionHandler = (error) -> {
            exceptionConsumer.accept(error);
            return null;
        };
    }

    public void clear() {
        cached = null;
    }

    @Nullable
    public T refresh() {
        clear();
        return get();
    }

    @Nullable
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

