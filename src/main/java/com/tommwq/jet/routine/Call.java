package com.tommwq.jet.routine;

public class Call<T, R> {
    private R result;
    private Throwable error = null;

    public Call(FallibleFunction<T, R> function, T t, R value) {
        result = value;
        try {
            result = function.apply(t);
        } catch (Exception e) {
            error = e;
        }
    }

    public Call(FallibleConsumer<T> consumer, T t) {
        try {
            consumer.call(t);
        } catch (Exception e) {
            error = e;
        }
    }

    public Call(FallibleRunnable procedure) {
        try {
            procedure.run();
        } catch (Exception e) {
            error = e;
        }
    }

    public R result() {
        return result;
    }

    public Call result(R value) {
        result = value;
        return this;
    }

    public Call error(FallibleConsumer<Throwable> errorHandler) {
        if (error == null || errorHandler == null) {
            return this;
        }

        new Call(() -> errorHandler.call(error))
                .abortWhenError();

        return this;
    }

    public Call throwWhenError() throws Throwable {
        if (error != null) {
            throw error;
        }
        return this;
    }

    public Call rethrow() throws RuntimeException {
        if (error != null) {
            throw new RuntimeException(error);
        }
        return this;
    }

    public void abortWhenError() {
        if (error != null) {
            error.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
