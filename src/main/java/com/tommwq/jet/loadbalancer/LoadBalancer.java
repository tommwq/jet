package com.tommwq.jet.loadbalancer;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 负载均衡器。
 */
public interface LoadBalancer<T> {
    @Nullable
    T peek();

    void initialize(List<T> list);

    void add(T t);

    void remove(T t);
}
