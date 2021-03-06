package com.tq.jet.container;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;

/**
 * 格式为(k1,k2,v)的字典
 */
public interface Triple<T1,T2,T3> {

        public static class Entry<T1,T2,T3> {
                private T1 k1;
                private T2 k2;
                private T3 v;
                
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
        
        void put(T1 k1, T2 k2, T3 v);
        T3 get(T1 k1, T2 k2);
        Map<T2,T3> get(T1 k1);
        boolean containsKey(T1 k1);
        boolean containsKey(T1 k1, T2 k2);
        void remove(T1 k1);
        void remove(T1 k1, T2 k2);
        void clear();
}
