package com.tommwq.jet.container;

import com.tommwq.jet.function.Call;
import com.tommwq.jet.function.FallibleFunction;
import com.tommwq.jet.runtime.reflect.ReflectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class LinearTable {
    @SuppressWarnings("unchecked")
    public static <T> List<T> select(Collection<T> items, String field, Object value) {
        List<T> selected = new ArrayList<>();

        for (T t : items) {
            try {
                if (ReflectUtils.getField(t, field).equals(value)) {
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
                if (ReflectUtils.getField(t, field).equals(value)) {
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

    public static <T> List<T> select(Collection<T> linearTable, T target) {
        return (List<T>) Stream.of(linearTable)
                .filter(target::equals)
                .collect(Collectors.toList());
    }


    public static <T, R> List<R> transform(Collection<T> linearTable, FallibleFunction<T, R> transformer, R defaultValue) {
        return (List<R>) linearTable.stream()
                .map(element -> new Call((Void) -> transformer.call(element), null, defaultValue).result())
                .collect(Collectors.toList());
    }

    public static <T, R> List<R> transform(T[] linearTable, FallibleFunction<T, R> transformer, R defaultValue) {
        return transform(Arrays.asList(linearTable), transformer, defaultValue);
    }

    public static <T> List<T> filter(Collection<T> linearTable, Predicate<T> predicate) {
        return linearTable.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static <T> List<T> filter(T[] linearTable, Predicate<T> predicate) {
        return filter(Arrays.asList(linearTable), predicate);
    }
}
