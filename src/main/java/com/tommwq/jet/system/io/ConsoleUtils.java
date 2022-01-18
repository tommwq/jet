package com.tommwq.jet.system.io;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ConsoleUtils {
    public static <T> void print(Stream<T> stream) {
        System.out.println(String.join(", ", stream.map(x -> x == null ? "null" : x.toString()).collect(Collectors.toList())));
    }

    public static <T> void print(T[] array) {
        print(Arrays.stream(array));
    }

    public static void print(long[] array) {
        print(Arrays.stream(array).boxed());
    }
}
