package com.tommwq.jet.routine.filter;

import com.tommwq.jet.util.Objects;

import java.util.function.Predicate;

/**
 * 域过滤器
 *
 * @param <T>
 */
public class ByFieldFilter<T> implements Predicate<T> {
    private String fieldName;
    private String targetValue;

    public ByFieldFilter(String fieldName, Object targetValue) {
        this.fieldName = fieldName;
        this.targetValue = String.valueOf(targetValue);
    }

    @Override
    public boolean test(T t) {
        try {
            return targetValue.equals(Objects.getField(t, fieldName));
        } catch (Exception e) {
            return false;
        }
    }
}