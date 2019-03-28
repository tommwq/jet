package com.tq.jet.container;

import com.tq.jet.reflect.FieldGetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class LinearTable {
    @SuppressWarnings("unchecked")
    public static <T> List<T> select(Collection<T> items, String field, Object value) {
        List<T> selected = new ArrayList<>();

        for (T t : items) {
            try {
                if (FieldGetter.get(t, field).equals(value)) {
                    selected.add(t);
                }
            } catch (Exception e) { /* ignore */ }
        }

        return selected;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> select(T[] items, String field, Object value) {
        List<T> selected = new ArrayList<>();

        for (T t : items) {
            try {
                //noinspection unchecked
                if (FieldGetter.get(t, field).equals(value)) {
                    selected.add(t);
                }
            } catch (Exception e) { /* ignore */ }
        }

        return selected;
    }

    public static <T> List<T> select(T[] items, T target) {
        List<T> selected = new ArrayList<>();

        for (T t : items) {
            if (t.equals(target)) {
                selected.add(t);
            }
        }

        return selected;
    }

    // Collection<U> => List<T>
    public static <T, U> List<T> transform(Collection<U> items, Function<U, T> mapper) {
        List<T> result = new ArrayList<>();
        for (U u : items) {
            T t = mapper.apply(u);
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    // Collection<U> => List<T>, allow null
    public static <T, U> List<T> transformNullable(Collection<U> items, Function<U, T> mapper) {
        List<T> result = new ArrayList<>();
        for (U u : items) {
            result.add(mapper.apply(u));
        }
        return result;
    }

    public static <T> List<T> fromCollection(Collection<T> items) {
        List<T> result = new ArrayList<>();
        result.addAll(items);
        return result;
    }

    public static String join(Object[] list, String delim) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (Object element : list) {
            if (!first) {
                b.append(delim);
            }
            b.append(String.valueOf(element));
            first = false;
        }

        return b.toString();
    }

    public static String join(Collection<Object> list, String delim) {
        return join(list.toArray(), delim);
    }
}
