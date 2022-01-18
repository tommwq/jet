package com.tommwq.jet.routine;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class InterruptableSupplier<T> implements Supplier<T>, Interruptable {
    private Interruptor interruptor = new Interruptor();

    public static <T> InterruptableSupplier make(FallibleSupplier<T> supplier, Function<Exception, T> exceptionHandler) {
        return new InterruptableSupplier() {
            @Override
            public T doGet() {
                try {
                    return supplier.get();
                } catch (Exception e) {
                    return exceptionHandler.apply(e);
                }
            }
        };
    }

    public static <T> InterruptableSupplier make(FallibleSupplier<T> supplier) {
        return new InterruptableSupplier() {
            @Override
            public T doGet() {
                try {
                    return supplier.get();
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    public static <T> InterruptableSupplier delay(long count, TimeUnit unit, FallibleSupplier<T> supplier) {
        return new InterruptableSupplier() {
            @Override
            public T doGet() {
                try {
                    Thread.sleep(unit.toMillis(count));
                    return supplier.get();
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    public static <T> InterruptableSupplier delayMillis(long count, FallibleSupplier<T> supplier) {
        return delay(count, TimeUnit.MILLISECONDS, supplier);
    }

    public static <T> InterruptableSupplier delay(long count, TimeUnit unit, T value) {
        return new InterruptableSupplier() {
            @Override
            public T doGet() {
                try {
                    Thread.sleep(unit.toMillis(count));
                    return value;
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    public static <T> InterruptableSupplier delayMillis(long count, T value) {
        return delay(count, TimeUnit.MILLISECONDS, value);
    }

    @Override
    public final T get() {
        if (interruptor.isInterrupted()) {
            return null;
        }

        try {
            interruptor.bindThread();
            return doGet();
        } finally {
            interruptor.unbindThread();
        }
    }

    @Override
    public void interrupt() {
        interruptor.interrupt();
    }

    public T doGet() {
        return null;
    }

}
