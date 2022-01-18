package com.tommwq.jet.routine;


// TODO
public class ExecutionStatusReportInterceptor { // implements MethodInterceptor {
//    private ExecutionStatusReporter reporter;
//
//    public ExecutionStatusReportInterceptor(ExecutionStatusReporter reporter) {
//        this.reporter = reporter;
//    }
//
//    @Override
//    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//        long start = System.currentTimeMillis();
//        Throwable error = null;
//        try {
//            return methodProxy.invokeSuper(o, objects);
//            // infinity loop
//            // return method.invoke(o, objects);
//        } catch (Throwable exception) {
//            error = exception;
//            throw error;
//        } finally {
//            long stop = System.currentTimeMillis();
//            ExecutionStatus status = new ExecutionStatus(method.getDeclaringClass().getName(), method.getName(), start, stop, error);
//            reporter.reportExecutionStatus(status);
//        }
//    }
}
