package com.tommwq.jet.util;

import java.io.Closeable;
import java.io.IOException;

public class Util {

    /**
     * get current stack frame
     */
    public static StackTraceElement currentFrame() {
        StackTraceElement[] stack = new Throwable().getStackTrace();

        final int stackDepth = 2;
        if (stack.length < stackDepth) {
            throw new RuntimeException("cannot get stack information");
        }

        return stack[stackDepth - 1];
    }

    /**
     * get current stack frame
     */
    public static StackTraceElement currentFrame(int stackDepth) {
        StackTraceElement[] stack = new Throwable().getStackTrace();

        if (stack.length < stackDepth) {
            throw new RuntimeException("cannot get stack information");
        }

        return stack[stackDepth - 1];
    }

    /**
     * get current time stamp
     */
    public static long currentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 安全关闭
     *
     * @param closeable 要关闭的对象
     */
    public static void safeClose(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            // IGNORE
        }
    }
}
