package com.tommwq.jet.routine;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Predicates {

    public static Boolean not(boolean value) {
        return !value;
    }

    public static Boolean notNull(Object object) {
        return not(object == null);
    }

    public static Boolean isNull(Object object) {
        return object == null;
    }

    public static Predicate<String> matchString(String target) {
        return new StringMatcher(target);
    }

    public static <T> Predicate<T> or(Predicate<T>[] predicates) {
        return new Or(predicates);
    }

    public static <T> Predicate<T> and(Predicate<T>[] predicates) {
        return new And(predicates);
    }

    public static <T> Predicate<T> not(Predicate<T> predictor) {
        return new Not(predictor);
    }

    public static class StringMatcher implements Predicate<String> {
        private final String target;

        public StringMatcher(String target) {
            this.target = target;
        }

        @Override
        public boolean test(String s) {
            return s.equals(target);
        }
    }

    public static class IgnoreCaseStringMatcher implements Predicate<String> {
        private final String target;

        public IgnoreCaseStringMatcher(String target) {
            this.target = target;
        }

        @Override
        public boolean test(String s) {
            return s.equalsIgnoreCase(target);
        }
    }

    public static class And<T> implements Predicate<T> {
        private final List<Predicate<T>> predicates;

        public And(Predicate<T>[] predicateArray) {
            this.predicates = Arrays.asList(predicateArray);
        }

        @Override
        public boolean test(T t) {
            return predicates.stream().allMatch(p -> p.test(t));
        }
    }

    public static class Not<T> implements Predicate<T> {
        Predicate<T> predicate;

        public Not(Predicate<T> aPredicate) {
            predicate = aPredicate;
        }

        @Override
        public boolean test(T t) {
            return !predicate.test(t);
        }
    }

    public static class Or<T> implements Predicate<T> {
        private final List<Predicate<T>> predicates;

        public Or(Predicate<T>[] predicateArray) {
            predicates = Arrays.asList(predicateArray);
        }

        @Override
        public boolean test(T t) {
            return predicates.stream().anyMatch(p -> p.test(t));
        }
    }

    public static class Null implements Predicate<Object> {
        @Override
        public boolean test(Object o) {
            return (o == null);
        }
    }

    public static class NotNull implements Predicate<Object> {
        @Override
        public boolean test(Object o) {
            return (o != null);
        }
    }

    public static class NullOrEmptyString implements Predicate<String> {
        @Override
        public boolean test(String o) {
            return (o == null || o.isEmpty());
        }
    }

    public static class NotEmptyString implements Predicate<String> {
        @Override
        public boolean test(String o) {
            return (o != null && !o.isEmpty());
        }
    }
}
