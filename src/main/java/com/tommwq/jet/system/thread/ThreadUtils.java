package com.tommwq.jet.system.thread;

import com.tommwq.jet.function.Call;
import com.tommwq.jet.function.FallibleProcedure;

import java.net.URLClassLoader;

public class ThreadUtils {

    public static URLClassLoader classLoader() {
        return (URLClassLoader) Thread.currentThread().getContextClassLoader();
    }

    public static SleepResult sleep(long milliseconds) {
        SleepResult reason = SleepResult.Timeup;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            reason = SleepResult.Interrupted;
        }

        return reason;
    }

    public static Thread createThread(FallibleProcedure procedure) {
        return new Thread(() -> new Call(procedure));
    }

    public static Thread startThread(FallibleProcedure procedure) {
        Thread thread = createThread(procedure);
        thread.start();
        return thread;
    }

    public static void john(Thread thread) {
        while (true) {
            try {
                thread.join();
                return;
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    public static enum SleepResult {
        Timeup, Interrupted;
    }
}
