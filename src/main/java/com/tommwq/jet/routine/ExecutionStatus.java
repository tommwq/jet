package com.tommwq.jet.routine;

/**
 * 执行状态
 */
public class ExecutionStatus {
    public long start;
    public long stop;
    public boolean success;
    public Throwable error;
    public String className;
    public String methodName;

    public ExecutionStatus(String className, String methodName, long start, long stop, Throwable error) {
        this.start = start;
        this.stop = stop;
        this.error = error;
        this.className = className;
        this.methodName = methodName;
        success = (error == null);
    }
}
