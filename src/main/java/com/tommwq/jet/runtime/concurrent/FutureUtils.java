package com.tommwq.jet.runtime.concurrent;

import com.tommwq.jet.function.FallibleFunction;
import com.tommwq.jet.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO 改为使用内部匿名类。
public class FutureUtils {
    /**
     * 顺序执行多个函数对象。
     *
     * @param <T> 输入类型
     * @param init 初始状态
     * @param actions 任务
     *
     * @return 执行结果
     *
     * @throws Exception 如果执行失败，抛出异常
     */
    public static <T> T sequence(T init, Function<T, T>... actions)
            throws Exception {
        CompletableFuture<T> future = CompletableFuture.completedFuture(init);
        int count = actions.length;
        for (int i = 0; i < count; i++) {
            final Function<T, T> action = actions[i];
            future = future.thenCompose((T state) -> {
                CompletableFuture<T> cf = new CompletableFuture();
                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        future.join();
        return future.get();
    }

    /**
     * 顺序执行多个任务。
     *
     * @param <T> 输入类型
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @param init 初始状态
     * @param actions 任务
     *
     * @return 执行结果
     *
     * @throws Exception 如果执行失败，抛出此异常
     * @throws TimeoutException 如果超时，抛出此异常
     */
    public static <T> T sequence(long timeout, TimeUnit timeunit, T init,
                                 Function<T, T>... actions)
            throws Exception, TimeoutException {
        CompletableFuture<T> future = CompletableFuture.completedFuture(init);
        int count = actions.length;
        for (int i = 0; i < count; i++) {
            final Function<T, T> action = actions[i];
            future = future.thenComposeAsync((T state) -> {
                CompletableFuture<T> cf = new CompletableFuture();
                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        CompletableFuture<Exception> timeoutFuture = CompletableFuture.completedFuture(null)
                .thenComposeAsync(state -> {
                    CompletableFuture<Exception> e = new CompletableFuture();
                    try {
                        timeunit.sleep(timeout);
                        e.complete(new TimeoutException("FutureUtils.sequence() timeout."));
                    } catch (InterruptedException ie) {
                        e.complete(ie);
                    }
                    return e;
                });
        CompletableFuture.anyOf(future, timeoutFuture).join();
        if (timeoutFuture.isDone()) {
            throw timeoutFuture.get();
        }

        return future.get();
    }

    /**
     * 并行执行多个任务。
     *
     * @param <T> 输入类型
     * @param <U> 中间结果类型
     * @param <R> 结果类型
     * @param init 初始状态
     * @param merge 任务结果合并函数
     * @param actions 任务
     *
     * @return 执行结果
     *
     * @throws Exception 如果执行失败，抛出异常
     */
    public static <T, U, R> R parallel(T init, Function<List<U>, R> merge, Function<T, U>... actions)
            throws Exception {
        CompletableFuture<T> start = CompletableFuture.completedFuture(init);
        CompletableFuture<Exception> except = new CompletableFuture();

        int count = actions.length;
        CompletableFuture<U>[] futures = new CompletableFuture[count];

        for (int i = 0; i < count; i++) {
            final Function<T, U> action = actions[i];
            futures[i] = start.thenComposeAsync(state -> {
                CompletableFuture<U> cf = new CompletableFuture();
                cf.exceptionally(t -> {
                    if (t instanceof Exception) {
                        Exception e = (Exception) t;
                        except.complete(e);
                    }
                    return null;
                });

                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        CompletableFuture.anyOf(except, CompletableFuture.allOf(futures))
                .join();

        if (except.isDone()) {
            throw except.get();
        }

        List<U> list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(futures[i].get());
        }

        return merge.apply(list);
    }

    /**
     * 并行执行多个任务。
     *
     * @param <T> 输入类型
     * @param <U> 中间结果类型
     * @param <R> 结果类型
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @param init 初始状态
     * @param merge 任务结果合并函数
     * @param actions 任务
     *
     * @return 返回结果
     *
     * @throws Exception 如果执行失败，抛出异常
     */
    public static <T, U, R> R parallel(long timeout, TimeUnit timeunit, T init,
                                       Function<List<U>, R> merge, Function<T, U>... actions)
            throws Exception {
        CompletableFuture<T> start = CompletableFuture.completedFuture(init);
        CompletableFuture<Exception> except = new CompletableFuture();

        int count = actions.length;
        CompletableFuture<U>[] futures = new CompletableFuture[count];

        for (int i = 0; i < count; i++) {
            final Function<T, U> action = actions[i];
            futures[i] = start.thenComposeAsync(state -> {
                CompletableFuture<U> cf = new CompletableFuture();
                cf.exceptionally(t -> {
                    if (t instanceof Exception) {
                        Exception e = (Exception) t;
                        except.complete(e);
                    }
                    return null;
                });

                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        start.thenApplyAsync(state -> {
            try {
                timeunit.sleep(timeout);
                except.complete(new TimeoutException("FutureUtils.parallel() timeout."));
            } catch (Exception e) {
                except.complete(e);
            }
            return null;
        });

        CompletableFuture.anyOf(except, CompletableFuture.allOf(futures))
                .join();

        if (except.isDone()) {
            throw except.get();
        }

        List<U> list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(futures[i].get());
        }

        return merge.apply(list);
    }


    /**
     * 取消Future。
     *
     * @param future 要取消的future
     */
    public static void kill(Future future) {
        cancel(future, true);
    }

    /**
     * 取消Future。
     *
     * @param future 要取消的future
     */
    public static void cancel(Future future) {
        cancel(future, false);
    }

    /**
     * 顺序执行多个函数对象。
     * @param init 初始状态
     * @param actions 任务
     */
    public static <T> T sequence(T init, com.tommwq.jet.routine.Function<T, T>... actions)
            throws Exception {
        CompletableFuture<T> future = CompletableFuture.completedFuture(init);
        int count = actions.length;
        for (int i = 0; i < count; i++) {
            final com.tommwq.jet.routine.Function<T, T> action = actions[i];
            future = future.thenCompose((T state) -> {
                CompletableFuture<T> cf = new CompletableFuture();
                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        future.join();
        return future.get();
    }

    /**
     * 顺序执行多个任务。
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @param init 初始状态
     * @param actions 任务
     */
    public static <T> T sequence(long timeout, TimeUnit timeunit, T init,
                                 com.tommwq.jet.routine.Function<T, T>... actions)
            throws Exception, TimeoutException {
        CompletableFuture<T> future = CompletableFuture.completedFuture(init);
        int count = actions.length;
        for (int i = 0; i < count; i++) {
            final com.tommwq.jet.routine.Function<T, T> action = actions[i];
            future = future.thenComposeAsync((T state) -> {
                CompletableFuture<T> cf = new CompletableFuture();
                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        CompletableFuture<Exception> timeoutFuture = CompletableFuture.completedFuture(null)
                .thenComposeAsync(state -> {
                    CompletableFuture<Exception> e = new CompletableFuture();
                    try {
                        timeunit.sleep(timeout);
                        e.complete(new TimeoutException("FutureUtils.sequence() timeout."));
                    } catch (InterruptedException ie) {
                        e.complete(ie);
                    }
                    return e;
                });
        CompletableFuture.anyOf(future, timeoutFuture).join();
        if (timeoutFuture.isDone()) {
            throw timeoutFuture.get();
        }

        return future.get();
    }

    /**
     * 并行执行多个任务。
     * @param init 初始状态
     * @param merge 任务结果合并函数
     * @param actions 任务
     */
    public static <T, U, R> R parallel(T init, com.tommwq.jet.routine.Function<List<U>, R> merge, com.tommwq.jet.routine.Function<T, U>... actions)
            throws Exception {
        CompletableFuture<T> start = CompletableFuture.completedFuture(init);
        CompletableFuture<Exception> except = new CompletableFuture();

        int count = actions.length;
        CompletableFuture<U>[] futures = new CompletableFuture[count];

        for (int i = 0; i < count; i++) {
            final com.tommwq.jet.routine.Function<T, U> action = actions[i];
            futures[i] = start.thenComposeAsync(state -> {
                CompletableFuture<U> cf = new CompletableFuture();
                cf.exceptionally(t -> {
                    if (t instanceof Exception) {
                        Exception e = (Exception) t;
                        except.complete(e);
                    }
                    return null;
                });

                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        CompletableFuture.anyOf(except, CompletableFuture.allOf(futures))
                .join();

        if (except.isDone()) {
            throw except.get();
        }

        List<U> list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(futures[i].get());
        }

        return merge.apply(list);
    }

    /**
     * 并行执行多个任务。
     * @param init 初始状态
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @param merge 任务结果合并函数
     * @param actions 任务
     */
    public static <T, U, R> R parallel(long timeout, TimeUnit timeunit, T init,
                                       com.tommwq.jet.routine.Function<List<U>, R> merge, com.tommwq.jet.routine.Function<T, U>... actions)
            throws Exception {
        CompletableFuture<T> start = CompletableFuture.completedFuture(init);
        CompletableFuture<Exception> except = new CompletableFuture();

        int count = actions.length;
        CompletableFuture<U>[] futures = new CompletableFuture[count];

        for (int i = 0; i < count; i++) {
            final com.tommwq.jet.routine.Function<T, U> action = actions[i];
            futures[i] = start.thenComposeAsync(state -> {
                CompletableFuture<U> cf = new CompletableFuture();
                cf.exceptionally(t -> {
                    if (t instanceof Exception) {
                        Exception e = (Exception) t;
                        except.complete(e);
                    }
                    return null;
                });

                try {
                    cf.complete(action.apply(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        start.thenApplyAsync(state -> {
            try {
                timeunit.sleep(timeout);
                except.complete(new TimeoutException("FutureUtils.parallel() timeout."));
            } catch (Exception e) {
                except.complete(e);
            }
            return null;
        });

        CompletableFuture.anyOf(except, CompletableFuture.allOf(futures))
                .join();

        if (except.isDone()) {
            throw except.get();
        }

        List<U> list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(futures[i].get());
        }

        return merge.apply(list);
    }

    /**
     * 取消Future。
     */
    public static void cancel(Future future, boolean mayInterruptIfRunning) {
        future.cancel(mayInterruptIfRunning);
    }

    /**
     * 顺序执行多个函数对象。
     *
     * @param <T> 输入类型
     * @param init 初始状态
     * @param actions 任务
     *
     * @return 执行结果
     *
     * @throws Exception 如果执行失败，抛出异常
     */
    public static <T> T sequence(T init, FallibleFunction<T, T>... actions)
            throws Exception {
        CompletableFuture<T> future = CompletableFuture.completedFuture(init);
        int count = actions.length;
        for (int i = 0; i < count; i++) {
            final FallibleFunction<T, T> action = actions[i];
            future = future.thenCompose((T state) -> {
                CompletableFuture<T> cf = new CompletableFuture();
                try {
                    cf.complete(action.call(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        future.join();
        return future.get();
    }

    /**
     * 顺序执行多个任务。
     *
     * @param <T> 输入类型
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @param init 初始状态
     * @param actions 任务
     *
     * @return 执行结果
     *
     * @throws Exception 如果执行失败，抛出此异常
     * @throws TimeoutException 如果超时，抛出此异常
     */
    public static <T> T sequence(long timeout, TimeUnit timeunit, T init,
                                 FallibleFunction<T, T>... actions)
            throws Exception, TimeoutException {
        CompletableFuture<T> future = CompletableFuture.completedFuture(init);
        int count = actions.length;
        for (int i = 0; i < count; i++) {
            final FallibleFunction<T, T> action = actions[i];
            future = future.thenComposeAsync((T state) -> {
                CompletableFuture<T> cf = new CompletableFuture();
                try {
                    cf.complete(action.call(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        CompletableFuture<Exception> timeoutFuture = CompletableFuture.completedFuture(null)
                .thenComposeAsync(state -> {
                    CompletableFuture<Exception> e = new CompletableFuture();
                    try {
                        timeunit.sleep(timeout);
                        e.complete(new TimeoutException("FutureUtil.sequence() timeout."));
                    } catch (InterruptedException ie) {
                        e.complete(ie);
                    }
                    return e;
                });
        CompletableFuture.anyOf(future, timeoutFuture).join();
        if (timeoutFuture.isDone()) {
            throw timeoutFuture.get();
        }

        return future.get();
    }

    /**
     * 并行执行多个任务。
     *
     * @param <T> 输入类型
     * @param <U> 中间结果类型
     * @param <R> 结果类型
     * @param init 初始状态
     * @param merge 任务结果合并函数
     * @param actions 任务
     *
     * @return 执行结果
     *
     * @throws Exception 如果执行失败，抛出异常
     */
    public static <T, U, R> R parallel(T init, FallibleFunction<List<U>, R> merge, FallibleFunction<T, U>... actions)
            throws Exception {
        CompletableFuture<T> start = CompletableFuture.completedFuture(init);
        CompletableFuture<Exception> except = new CompletableFuture();

        int count = actions.length;
        CompletableFuture<U>[] futures = new CompletableFuture[count];

        for (int i = 0; i < count; i++) {
            final FallibleFunction<T, U> action = actions[i];
            futures[i] = start.thenComposeAsync(state -> {
                CompletableFuture<U> cf = new CompletableFuture();
                cf.exceptionally(t -> {
                    if (t instanceof Exception) {
                        Exception e = (Exception) t;
                        except.complete(e);
                    }
                    return null;
                });

                try {
                    cf.complete(action.call(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        CompletableFuture.anyOf(except, CompletableFuture.allOf(futures))
                .join();

        if (except.isDone()) {
            throw except.get();
        }

        List<U> list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(futures[i].get());
        }

        return merge.call(list);
    }

    /**
     * 并行执行多个任务。
     *
     * @param <T> 输入类型
     * @param <U> 中间结果类型
     * @param <R> 结果类型
     * @param timeout 超时时间
     * @param timeunit 超时时间单位
     * @param init 初始状态
     * @param merge 任务结果合并函数
     * @param actions 任务
     *
     * @return 返回结果
     *
     * @throws Exception 如果执行失败，抛出异常
     */
    public static <T, U, R> R parallel(long timeout, TimeUnit timeunit, T init,
                                       FallibleFunction<List<U>, R> merge, FallibleFunction<T, U>... actions)
            throws Exception {
        CompletableFuture<T> start = CompletableFuture.completedFuture(init);
        CompletableFuture<Exception> except = new CompletableFuture();

        int count = actions.length;
        CompletableFuture<U>[] futures = new CompletableFuture[count];

        for (int i = 0; i < count; i++) {
            final FallibleFunction<T, U> action = actions[i];
            futures[i] = start.thenComposeAsync(state -> {
                CompletableFuture<U> cf = new CompletableFuture();
                cf.exceptionally(t -> {
                    if (t instanceof Exception) {
                        Exception e = (Exception) t;
                        except.complete(e);
                    }
                    return null;
                });

                try {
                    cf.complete(action.call(state));
                } catch (Exception e) {
                    cf.completeExceptionally(e);
                }
                return cf;
            });
        }

        start.thenApplyAsync(state -> {
            try {
                timeunit.sleep(timeout);
                except.complete(new TimeoutException("FutureUtil.parallel() timeout."));
            } catch (Exception e) {
                except.complete(e);
            }
            return null;
        });

        CompletableFuture.anyOf(except, CompletableFuture.allOf(futures))
                .join();

        if (except.isDone()) {
            throw except.get();
        }

        List<U> list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(futures[i].get());
        }

        return merge.call(list);
    }
}
