package com.tommwq.jet.loadbalancer;


import java.util.List;

/**
 * 负载均衡器。
 */
public interface LoadBalancer<T> {

  T peek();

  void initialize(List<T> list);

  void add(T t);

  void remove(T t);
}
