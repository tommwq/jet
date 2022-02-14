package com.tommwq.jet.datatype;

import com.tommwq.jet.routine.Call;

/**
 * 类型转换常用函数类。
 */
public class DataType {
    /**
     * 将Object转换为boolan型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static boolean cast(Object value, boolean defaultValue) {
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为byte型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static byte cast(Object value, byte defaultValue) {
        try {
            return (Byte) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为short型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static short cast(Object value, short defaultValue) {
        try {
            return (Short) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为int型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static int cast(Object value, int defaultValue) {
        try {
            return (Integer) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为long型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static long cast(Object value, long defaultValue) {
        try {
            return (Long) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为String型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static String cast(Object value, String defaultValue) {
        try {
            return (String) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为float型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static float cast(Object value, float defaultValue) {
        try {
            return (Float) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 将Object转换为double型。
     *
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static double cast(Object value, double defaultValue) {
        try {
            return (Double) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public static boolean isString(Class clazz) {
        return clazz == java.lang.String.class;
    }

    public static boolean isBoolean(Class clazz) {
        return clazz == Boolean.class || clazz == boolean.class;
    }

    public static boolean isInteger(Class clazz) {
        return clazz == Integer.class || clazz == int.class;
    }

    public static boolean isLong(Class clazz) {
        return clazz == Long.class || clazz == long.class;
    }

    public static boolean isEnum(Class clazz) {
        return clazz.isEnum();
    }

    public static int booleanToInt(boolean value) {
        return value ? 1 : 0;
    }

    public static boolean intToBoolean(int value) {
        return value != 0;
    }

    public static int enumToInt(Enum value) {
        return value.ordinal();
    }

    public static <T> T intToEnum(int value, Class<T> clazz) {
        return (T) new Call((Void) -> {
            java.lang.reflect.Method m = clazz.getDeclaredMethod("values");
            T[] valueArray = (T[]) m.invoke(null);
            int index = value;
            if (index >= valueArray.length) {
                index = 0;
            }

            return valueArray[index];
        }, null, null).rethrow().result();
    }
}
        
