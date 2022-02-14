package com.tommwq.jet.routine;

import java.util.function.Function;

public class Routines {
    public static void ignoreFailure(FallibleRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception ignored) {
            // ignore
        }
    }

    // 为简化lambda，将CheckedException转换为RuntimeException。
    public static <T, R> Function<T, R> wrapException(FallibleFunction<T, R> fallible) {
        return (t) -> {
            try {
                return fallible.call(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}
