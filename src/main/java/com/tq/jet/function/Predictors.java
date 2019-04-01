package com.tq.jet.function;


import java.util.Arrays;

public class Predictors {

    public static Predictor<String> matchString(String target) {
        return new MatchString(target);
    }

    public static <T> Predictor<T> or(Predictor<T>[] predictors) {
        return new Or(predictors);
    }

    public static <T> Predictor<T> and(Predictor<T>[] predictors) {
        return new And(predictors);
    }

    public static <T> Predictor<T> not(Predictor<T> predictor) {
        return new Not(predictor);
    }

    public interface Predictor<T> {
        boolean predict(T t);
    }

    public static class MatchString implements Predictor<String> {
        private String target;

        public MatchString(String target) {
            this.target = target;
        }

        @Override
        public boolean predict(String s) {
            return s.equals(target);
        }
    }

    public static class MatchStringIgnoreCase implements Predictor<String> {
        private String target;

        public MatchStringIgnoreCase(String target) {
            this.target = target;
        }

        @Override
        public boolean predict(String s) {
            return s.equalsIgnoreCase(target);
        }
    }

    public static class And<T> implements Predictor<T> {
        private Predictor<T>[] predictors;

        public And(Predictor<T>[] predictors) {
            this.predictors = predictors;
        }

        @Override
        public boolean predict(T t) {
            return Arrays.stream(predictors).allMatch((f) -> f.predict(t));
        }
    }

    public static class Not<T> implements Predictor<T> {
        Predictor<T> predictor;

        public Not(Predictor<T> predictor) {
            this.predictor = predictor;
        }

        @Override
        public boolean predict(T t) {
            return !predictor.predict(t);
        }
    }

    public static class Or<T> implements Predictor<T> {
        Predictor<T>[] predictors;

        public Or(Predictor<T>[] predictors) {
            this.predictors = predictors;
        }

        @Override
        public boolean predict(T t) {
            return Arrays.stream(predictors).anyMatch((f) -> f.predict(t));
        }
    }

    public static class IsNull implements Predictor<Object> {
        @Override
        public boolean predict(Object o) {
            return (o == null);
        }
    }

    public static class IsNotNull implements Predictor<Object> {
        @Override
        public boolean predict(Object o) {
            return (o != null);
        }
    }

    public static class IsNullOrEmptyString implements Predictor<String> {
        @Override
        public boolean predict(String o) {
            return (o == null || o.isEmpty());
        }
    }

    public static class IsNotEmptyString implements Predictor<String> {
        @Override
        public boolean predict(String o) {
            return (o != null && !o.isEmpty());
        }
    }
}
