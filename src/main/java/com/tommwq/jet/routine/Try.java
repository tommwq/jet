package com.tommwq.jet.routine;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * 为简化 try-cache 结构引入的辅助类。
 */
public class Try<SingleParameter, ReturnValue> {
    private Function<SingleParameter, ReturnValue> function = null;
    private Function<Throwable, Void> exceptionHandler = null;
    private final ArrayList<Class> ignoreList = new ArrayList<>();
    private Class rethrowException = null;

    /**
     * 建立Try对象。
     *
     * @param function 要执行的函数。
     */
    public Try(Function<SingleParameter, ReturnValue> function) {
        this.function = function;
    }

    /**
     * 执行函数，得到返回值。
     *
     * @param in            传递给函数的参数。
     * @param defaultResult 异常时的默认返回值。
     * @return 如果出现异常，返回defaultResult。否则返回函数结果。
     */
    public ReturnValue run(SingleParameter in, ReturnValue defaultResult) {
        if (function == null) {
            return defaultResult;
        }

        ReturnValue result = defaultResult;

        try {
            result = function.apply(in);
        } catch (Exception e) {
            onException(e);
        }

        return result;
    }

    /**
     * 设置异常处理句柄。
     *
     * @param exceptionHandler 异常处理句柄。
     * @return Try自身
     */
    public Try exception(Function<Throwable, Void> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * 忽略函数执行中的异常。
     *
     * @return Try自身
     */
    public Try skipError() {
        exceptionHandler = null;
        return this;
    }

    /**
     * 忽略函数执行中的特定异常。
     *
     * @param errorClass 忽略的异常。
     * @return Try自身
     */
    public Try skipError(Class errorClass) {
        ignoreList.add(errorClass);
        return this;
    }

    /**
     * 封装并重新抛出异常。
     * <p>
     * 如果函数执行中发送异常，并且该异常不再忽略清单中，而又没有设置异常处理句柄，Try将封装并重新抛出异常。
     * 如果封装异常的过程失败，go()会抛出一个RuntimeException。
     *
     * @param errorClass 当函数执行发送异常时，Try对象抛出的异常。
     *                   errorClass必须是RuntimeException或其派生类，并提供带有Throwable参数的构造函数。
     * @return Try自身
     */
    public Try rethrow(Class errorClass) {
        rethrowException = errorClass;
        return this;
    }

    private void onException(Throwable error) {
        if (exceptionHandler == null && rethrowException == null) {
            return;
        }

        for (Class clazz : ignoreList) {
            if (clazz.getName().equals(error.getClass().getName())) {
                return;
            }
        }

        if (exceptionHandler != null) {
            exceptionHandler.apply(error);
        }

        RuntimeException exception = null;
        try {
            exception = (RuntimeException) rethrowException.getConstructor(Throwable.class).newInstance(error);
        } catch (Exception e) {
            exception = new RuntimeException(e);
        }

        throw exception;
    }
}
