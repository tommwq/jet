package com.tommwq.jet.container;

import java.util.Map;

/**
 * 二层字典
 * <p>
 * 格式为(k1,k2,v)的字典
 */
public interface TwoLevelDictionary<T1, T2, T3> {

    void put(T1 k1, T2 k2, T3 v);

    T3 get(T1 k1, T2 k2);

    Map<T2, T3> get(T1 k1);

    boolean containsKey(T1 k1);

    boolean containsKey(T1 k1, T2 k2);

    void remove(T1 k1);

    void remove(T1 k1, T2 k2);

    void clear();

    class Entry<T1, T2, T3> {
        private final T1 k1;
        private final T2 k2;
        private final T3 v;

        public Entry(T1 k1, T2 k2, T3 v) {
            this.k1 = k1;
            this.k2 = k2;
            this.v = v;
        }

        public T1 first() {
            return k1;
        }

        public T2 second() {
            return k2;
        }

        public T3 thrid() {
            return v;
        }
    }
}
