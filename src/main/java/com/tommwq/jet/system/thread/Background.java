package com.tommwq.jet.system.thread;

import com.tommwq.jet.function.Function;

/**
 * 启动新线程执行函数。
 *
 * @param <T>
 */
public class Background<T> {

    private final Function<T, Void> function;
    private final T parameter;

    private final Thread thread = new Thread() {
        @Override
        public void run() {
            if (function == null) {
                return;
            }

            function.apply(parameter);
        }
    };

    /**
     * @param function  要执行的函数
     * @param parameter 传递给函数的参数
     */
    public Background(Function<T, Void> function, T parameter) {
        this.function = function;
        this.parameter = parameter;
    }

    /**
     * 获取底层的线程对象。
     *
     * @return 线程对象
     */
    public Thread thread() {
        return thread;
    }

    /**
     * 启动线程。
     */
    public void go() {
        thread.start();
    }
}
