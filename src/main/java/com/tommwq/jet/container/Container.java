package com.tommwq.jet.container;

import com.tommwq.jet.function.Call;
import com.tommwq.jet.routine.FallibleFunction;
import com.tommwq.jet.runtime.reflect.ClassUtils;
import com.tommwq.jet.runtime.reflect.ReflectUtils;

import java.util.*;
import java.util.function.Predicate;

// TODO 把函数移动到 LinearTable 和 KeyValueTable 中。
public class Container {

    /**
     * 创建 Map 对象
     *
     * @param keyvalues 键值数组
     * @return Map 对象
     */
    public static Map<Object, Object> createMap(Object... keyvalues) {
        Map<Object, Object> result = new HashMap<>();

        for (int i = 0; i < keyvalues.length; i += 2) {
            Object key = keyvalues[i];
            Object value = null;
            if (i + 1 < keyvalues.length) {
                value = keyvalues[i + 1];
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 创建 Map 对象
     *
     * @param mapClass  Map 类型
     * @param keyvalues 键值数组
     * @return Map 对象
     */
    public static Map<Object, Object> createMap(Class<? extends Map> mapClass, Object... keyvalues) throws InstantiationException, IllegalAccessException {
        Map<Object, Object> result = ClassUtils.create(mapClass);

        for (int i = 0; i < keyvalues.length; i += 2) {
            Object key = keyvalues[i];
            Object value = null;
            if (i + 1 < keyvalues.length) {
                value = keyvalues[i + 1];
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 连接列表
     */
    public static <T> List<T> concat(List<T>... lists) {
        ArrayList<T> result = new ArrayList<>();
        for (List<T> l : lists) {
            result.addAll(l);
        }
        return result;
    }

    /**
     * 连接 Map
     */
    public static <T, R> Map<T, R> concat(Map<T, R>... maps) {
        HashMap<T, R> result = new HashMap<>();
        for (Map<T, R> m : maps) {
            result.putAll(m);
        }
        return result;
    }

    /**
     * 根据属性创建对象
     */
    public static <T> T propertiesToObject(Properties properties, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        T result = clazz.newInstance();
        for (String name : properties.stringPropertyNames()) {
            fill(result, name, properties.getProperty(name));
        }

        return result;
    }

    private static void fill(Object object, String fullFieldName, final Object value) {
        final String dot = "\\.";

        if (fullFieldName.isEmpty()) {
            return;
        }

        String[] piece = fullFieldName.split(dot, 2);
        if (piece.length == 1) {
            new com.tommwq.jet.routine.Call(() -> ReflectUtils.setDeclaredFieldString(object,
                    fullFieldName,
                    (String) value));
            return;
        }

        final String fieldName = piece[0];
        if (!ReflectUtils.containDeclaredField(object, fieldName)) {
            return;
        }

        final String memberFieldName = piece[1];
        new com.tommwq.jet.routine.Call(() -> fill(ReflectUtils.initializeDeclaredFieldInNeed(object, fieldName),
                memberFieldName,
                value)).abortWhenError();
    }

    /**
     * 根据值过滤散列表
     *
     * @param <K>       键类型
     * @param <V>       值类型
     * @param predicate 过滤函数
     * @return 返回过滤结果
     */
    public static <K, V> Map<K, V> selectByValue(Map<K, V> map, Predicate<V> predicate) {
        Map<K, V> selected = new HashMap<>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            V value = entry.getValue();

            if (predicate.test(value)) {
                selected.put(entry.getKey(), value);
            }
        }

        return selected;
    }

    /**
     * 将容器(K,V1)映射为(K,transformer(V1))
     *
     * @param <K>          键类型
     * @param <V1>         源容器值类型
     * @param <V2>         结果容器值类型
     * @param map          源容器
     * @param transformer  变换函数
     * @param defaultValue 变换函数抛出异常时，采用默认值作为转换结果。
     * @return 返回映射结果
     */
    @SuppressWarnings("WeakerAccess")
    public static <K, V1, V2> Map<K, V2> transform(Map<K, V1> map, FallibleFunction<V1, V2> transformer, V2 defaultValue) {
        Map<K, V2> transformed = new HashMap<>();
        for (Map.Entry<K, V1> entry : map.entrySet()) {
            transformed.put(entry.getKey(), (V2) new com.tommwq.jet.routine.Call((Void) -> transformer.apply(entry.getValue()), null, defaultValue).result());
        }

        return transformed;
    }

    /**
     * 将容器(K,V1)映射为(K,F)。F是V1的域
     *
     * @param <K>  键类型
     * @param <V1> 源容器的值类型
     * @param <V2> 结果容器的值类型
     * @param map  源容器
     * @return 返回映射结果
     * @throws RuntimeException 如果无法通过反射获得域，抛出异常
     */
    public static <K, V1, V2> Map<K, V2> project(Map<K, V1> map, @SuppressWarnings("SameParameterValue") String valueFieldName) throws RuntimeException {
        Map<K, V2> projected = new HashMap<>();
        for (Map.Entry<K, V1> entry : map.entrySet()) {
            projected.put(entry.getKey(), (V2) new com.tommwq.jet.routine.Call((Void) -> ReflectUtils.declaredFieldValue(entry.getValue(), valueFieldName), null, null).result());
        }

        return projected;
    }

    /**
     * 从一组对象建立关联数组。将V映射为(K,V)，其中K是V的域
     *
     * @param <K>   键类型
     * @param <V>   值类型
     * @param items 数组
     * @return 返回映射结果
     * @throws IllegalArgumentException 参数非法
     * @throws IllegalAccessException   在keyField不可访问时抛出
     * @throws NoSuchFieldException     在keyField不存在时抛出
     * @throws SecurityException        在keyField不可访问时抛出
     */
    public static <K, V> Map<K, V> makeMap(Collection<V> items, @SuppressWarnings("SameParameterValue") String fieldName) throws IllegalArgumentException,
            IllegalAccessException,
            NoSuchFieldException,
            SecurityException {

        Map<K, V> map = new HashMap<>();
        for (V item : items) {
            K key = (K) new com.tommwq.jet.routine.Call((Void) -> ReflectUtils.declaredFieldValue(item, fieldName), null, null).result();
            if (key != null) {
                map.put(key, item);
            }
        }

        return map;
    }

    public static <T> Map<T, T> asMap(T... sequence) {
        Map<T, T> map = new HashMap<>();

        T key = null;
        T value = null;
        for (int i = 0; i < sequence.length; i += 2) {
            key = sequence[i];
            if (i + 1 < sequence.length) {
                value = sequence[i + 1];
            }
            map.put(key, value);
        }

        return map;
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


    /**
     * 将容器(K,V1)映射为(K,transformer(V1))
     *
     * @param <K>          键类型
     * @param <V1>         源容器值类型
     * @param <V2>         结果容器值类型
     * @param map          源容器
     * @param transformer  变换函数
     * @param defaultValue 变换函数抛出异常时，采用默认值作为转换结果。
     * @return 返回映射结果
     */
    @SuppressWarnings("WeakerAccess")
    public static <K, V1, V2> Map<K, V2> transform(Map<K, V1> map, com.tommwq.jet.function.FallibleFunction<V1, V2> transformer, V2 defaultValue) {
        Map<K, V2> transformed = new HashMap<>();
        for (Map.Entry<K, V1> entry : map.entrySet()) {
            transformed.put(entry.getKey(), (V2) new Call((Void) -> transformer.call(entry.getValue()), null, defaultValue).result());
        }

        return transformed;
    }
}
