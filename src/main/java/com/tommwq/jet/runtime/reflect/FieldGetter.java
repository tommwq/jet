package com.tommwq.jet.runtime.reflect;

/**
 * 通过反射读取对象的域。
 */
public class FieldGetter {

    /**
     * 通过反射读取对象的域。
     *
     * @param <T>   对象类型
     * @param <U>   域类型
     * @param t     对象。
     * @param field 域名字。
     * @return 返回域的值。
     * @throws IllegalArgumentException 读取域失败
     * @throws IllegalAccessException   域不可访问
     * @throws NoSuchFieldException     域不存在
     * @throws SecurityException        域不可访问
     */
    @SuppressWarnings("unchecked")
    public static <T, U> U get(T t, String field)
            throws IllegalArgumentException,
            IllegalAccessException,
            NoSuchFieldException,
            SecurityException {
        //noinspection unchecked
        return (U) t.getClass().getField(field).get(t);
    }
}
