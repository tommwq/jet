package com.tommwq.jet.routine;

/**
 * 打印执行状态。
 */
public class ExecutionStatusPrinter implements ExecutionStatusReporter {
    public void reportExecutionStatus(ExecutionStatus status) {
        long duration = status.stop - status.start;
        System.out.printf("invoke %s::%s start %d stop %d duration %d success %s error %s\n", status.className, status.methodName, status.start,
                status.stop, duration, status.success, status.error);
    }
}
