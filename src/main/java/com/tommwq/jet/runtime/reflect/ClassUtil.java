package com.tommwq.jet.runtime.reflect;

public class ClassUtil {

    public static boolean isSameClass(Class class1, Class class2) {
        if (class1 == null || class2 == null) {
            return false;
        }

        return class1.getName().equals(class2.getName());
    }

    public static boolean isSameClass(Object object1, Object object2) {
        if (object1 == null || object2 == null) {
            return false;
        }

        return isSameClass(object1.getClass(), object2.getClass());
    }
}
