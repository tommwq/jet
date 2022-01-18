package com.tommwq.jet.runtime.reflect;

import com.tommwq.jet.container.Container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * utility functions for reflection
 */
public class ReflectUtil {

    /**
     * test if object contain a field
     */
    public static boolean containField(Object object, String fieldName) {
        return Stream.of(object.getClass().getFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    /**
     * test if object contain a (declared) field
     */
    public static boolean containDeclaredField(Object object, String fieldName) {
        return Stream.of(object.getClass().getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    /**
     * get value of a declared field, ignore it's accessibility
     */
    public static Object declaredFieldValue(Object object, String fieldName) throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }

        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }

        if (!accessible) {
            field.setAccessible(accessible);
        }

        return value;
    }

    /**
     * initialize an declared field if it's null, ignore it's accessibility
     */
    public static Object initializeDeclaredFieldInNeed(Object object, String fieldName)
            throws NoSuchFieldException, InstantiationException {
        Field field = object.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }

        Object value = null;
        try {
            value = field.get(object);
            if (value == null) {
                value = field.getType().newInstance();

                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        if (!accessible) {
            field.setAccessible(accessible);
        }

        return value;
    }

    /**
     * set value for a declared field
     */
    public static void setDeclaredField(Object object, String fieldName, Object value)
            throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        field.setAccessible(accessible);
    }

    /**
     * set value for a declared field
     */
    public static void setDeclaredField(Object object, Field field, Object value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        field.setAccessible(accessible);
    }

    /**
     * cast string to field's type and set field's value
     * <p>
     * The field must be primitive or String.
     */
    public static void castThenSetFieldString(Object object, Field field, String value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            String fieldType = field.getType().getName();
            if (fieldType.equals("int")) {
                field.setInt(object, Integer.valueOf(value));
            } else if (fieldType.equals("short")) {
                field.setShort(object, Short.valueOf(value));
            } else if (fieldType.equals("long")) {
                field.setLong(object, Long.valueOf(value));
            } else if (fieldType.equals("float")) {
                field.setFloat(object, Float.valueOf(value));
            } else if (fieldType.equals("double")) {
                field.setDouble(object, Double.valueOf(value));
            } else if (fieldType.equals("char")) {
                field.setChar(object, value.charAt(0));
            } else if (fieldType.equals("byte")) {
                field.setByte(object, Byte.valueOf(value));
            } else if (fieldType.equals("boolean")) {
                field.setBoolean(object, Boolean.valueOf(value));
            } else if (fieldType.equals("java.lang.String")) {
                field.set(object, value);
            } else {
                throw new RuntimeException("non-castable type: " + fieldType);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        field.setAccessible(accessible);
    }

    /**
     * set value for a declared field
     * <p>
     * The field must be primitive or String.
     */
    public static void setDeclaredFieldString(Object object, String fieldName, String value)
            throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        castThenSetFieldString(object, field, value);
    }

    /**
     * get all interfaces of a class, include it's parents interfaces
     */
    public static List<Class> getAllInterfaces(Class clazz) {
        if (clazz == null) {
            return new ArrayList<Class>();
        }

        return Container.concat(Arrays.asList(clazz.getInterfaces()),
                getAllInterfaces(clazz.getSuperclass()));
    }

    /**
     * get all superclass of a class
     */
    public static List<Class> getAllSuperclass(Class clazz) {
        List<Class> list = new ArrayList<Class>();
        for (Class superclass = clazz.getSuperclass();
             superclass != null;
             superclass = superclass.getSuperclass()) {
            list.add(superclass);
        }

        return list;
    }

    /**
     * get field value of an object
     */
    public static Object fieldValue(Object object, Field field) throws NoSuchFieldException {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }

        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }

        if (!accessible) {
            field.setAccessible(accessible);
        }

        return value;
    }
}
