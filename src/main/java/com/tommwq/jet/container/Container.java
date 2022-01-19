package com.tommwq.jet.container;

import com.tommwq.jet.function.Call;
import com.tommwq.jet.runtime.reflect.ReflectUtils;

import java.util.*;

public class Container {

    /**
     * concat a list of list
     */
    public static <T> List<T> concat(List<T>... lists) {
        ArrayList<T> result = new ArrayList<>();
        for (List<T> l : lists) {
            result.addAll(l);
        }
        return result;
    }

    /**
     * concat a list of map
     */
    public static <T, R> Map<T, R> concat(Map<T, R>... maps) {
        HashMap<T, R> result = new HashMap<>();
        for (Map<T, R> m : maps) {
            result.putAll(m);
        }
        return result;
    }

    /**
     * create object from properties
     */
    public static <T> T propertiesToObject(Properties properties, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        T result = clazz.newInstance();
        for (String name : properties.stringPropertyNames()) {
            fillObject(result, name, properties.getProperty(name));
        }

        return result;
    }

    private static void fillObject(Object object, String fullFieldName, final Object value) {
        final String dot = "\\.";

        if (fullFieldName.isEmpty()) {
            return;
        }

        String[] piece = fullFieldName.split(dot, 2);
        if (piece.length == 1) {
            new Call(() -> ReflectUtils.setDeclaredFieldString(object,
                    fullFieldName,
                    (String) value));
            return;
        }

        final String fieldName = piece[0];
        if (!ReflectUtils.containDeclaredField(object, fieldName)) {
            return;
        }

        final String memberFieldName = piece[1];
        new Call(() -> fillObject(ReflectUtils.initializeDeclaredFieldInNeed(object, fieldName),
                memberFieldName,
                value)).abortWhenError();
    }

    public static <T> List<T> list() {
        return new ArrayList<>();
    }
}
