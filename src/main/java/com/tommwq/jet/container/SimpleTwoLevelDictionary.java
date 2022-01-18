package com.tommwq.jet.container;

import java.util.HashMap;
import java.util.Map;

/**
 * 格式为(k1,k2,v)的字典
 */
public class SimpleTwoLevelDictionary<T1, T2, T3> implements TwoLevelDictionary<T1, T2, T3> {

    private HashMap<T1, HashMap<T2, T3>> triples = new HashMap();

    public void put(T1 k1, T2 k2, T3 v) {
        if (!triples.containsKey(k1)) {
            triples.put(k1, new HashMap<T2, T3>());
        }

        HashMap<T2, T3> dict = triples.get(k1);
        dict.put(k2, v);
    }

    public T3 get(T1 k1, T2 k2) {
        if (!triples.containsKey(k1)) {
            return null;
        }

        HashMap<T2, T3> dict = triples.get(k1);
        return dict.get(k2);
    }

    public Map<T2, T3> get(T1 k1) {
        return triples.get(k1);
    }

    public boolean containsKey(T1 k1) {
        return triples.containsKey(k1);
    }

    public boolean containsKey(T1 k1, T2 k2) {
        if (!triples.containsKey(k1)) {
            return false;
        }

        HashMap<T2, T3> dict = triples.get(k1);
        return dict.containsKey(k2);
    }

    public void remove(T1 k1) {
        triples.remove(k1);
    }

    public void remove(T1 k1, T2 k2) {
        if (!triples.containsKey(k1)) {
            return;
        }
        HashMap<T2, T3> dict = triples.get(k1);
        dict.remove(k2);
    }

    public void clear() {
        triples.clear();
    }
}
