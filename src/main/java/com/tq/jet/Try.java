package com.tq.jet;

import java.util.ArrayList;
import java.util.function.Function;

public class Try {
    public static ExceptionHandler defaultExceptionHandler = new ExceptionIgnorer();

    public static void execute(Command command) {
        execute(command, defaultExceptionHandler);
    }

    public static void execute(Command command, ExceptionHandler handler) {
        try {
            command.execute();
        } catch (Throwable e) {
            if (handler != null) {
                handler.handleException(e);
            }
        }
    }

    public static <T, R> R execute(Function<T, R> function, T inputValue, R defaultResult) {
        return execute(function, inputValue, defaultResult, defaultExceptionHandler);
    }

    public static <T, R> R execute(Function<T, R> function, T inputValue, R defaultResult, ExceptionHandler handler) {
        R result = defaultResult;
        try {
            result = function.execute(inputValue);
        } catch (Throwable e) {
            if (handler != null) {
                handler.handleException(e);
            }
        }

        return result;
    }

    public static <T> void execute(Consumer<T> consumer, T inputValue) {
        execute(consumer, inputValue, defaultExceptionHandler);
    }

    public static <T> void execute(Consumer<T> consumer, T inputValue, ExceptionHandler handler) {
        try {
            consumer.execute(inputValue);
        } catch (Throwable e) {
            if (handler != null) {
                handler.handleException(e);
            }
        }
    }

    public static <R> R execute(Producer<R> producer, R defaultResult) {
        return execute(producer, defaultResult, defaultExceptionHandler);
    }

    public static <R> R execute(Producer<R> producer, R defaultResult, ExceptionHandler handler) {
        R result = defaultResult;
        try {
            result = producer.execute();
        } catch (Throwable e) {
            if (handler != null) {
                handler.handleException(e);
            }
        }

        return result;
    }

    public static RuntimeExceptionWrapper runtimeExceptionWrapper() {
        return new RuntimeExceptionWrapper();
    }

    public interface ExceptionHandler {
        void handleException(Throwable e) throws RuntimeException;
    }

    public interface Command {
        void execute() throws Exception;
    }

    public interface Function<T, R> {
        R execute(T t) throws Exception;
    }

    public interface Consumer<T> {
        void execute(T t) throws Exception;
    }

    public interface Producer<R> {
        R execute() throws Exception;
    }

    public interface ExceptionProducer extends Producer<Exception> {
    }

    public static class StackTracePrinter implements ExceptionHandler {
        @Override
        public void handleException(Throwable e) {
            e.printStackTrace();
        }
    }

    public static class ExceptionIgnorer implements ExceptionHandler {
        @Override
        public void handleException(Throwable e) {
            // ignore
        }
    }

    public static class UserDefinedExceptionWrapper implements ExceptionHandler {
        private ExceptionProducer producer;

        public UserDefinedExceptionWrapper(ExceptionProducer producer) {
            this.producer = producer;
        }

        @Override
        public void handleException(Throwable e) {
            RuntimeException exception;
            try {
                exception = new RuntimeException(producer.execute());
            } catch (Exception error) {
                exception = new RuntimeException(error);
            }

            throw exception;
        }
    }

    public static class RuntimeExceptionWrapper implements ExceptionHandler {
        @Override
        public void handleException(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
