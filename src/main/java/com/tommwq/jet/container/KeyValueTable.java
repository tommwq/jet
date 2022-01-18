package com.tommwq.jet.container;

import com.tommwq.jet.function.Function;
import com.tommwq.jet.runtime.reflect.FieldGetter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class KeyValueTable {

    /**
     * 根据值过滤散列表
     *
     * @param <K>       键类型
     * @param <V>       值类型
     * @param pairs     字典
     * @param predictor 过滤函数
     * @return 返回过滤结果
     */
    public static <K, V> Map<K, V> select(Map<K, V> pairs, Function<V, Boolean> predictor) {
        Map<K, V> result = new HashMap<>();
        for (K key : pairs.keySet()) {
            V value = pairs.get(key);
            if (predictor.apply(value)) {
                result.put(key, value);
            }
        }

        return result;
    }

    /**
     * 将容器(K,V1)映射为(K,transformer(V1))
     *
     * @param <K>         键类型
     * @param <V1>        源容器值类型
     * @param <V2>        结果容器值类型
     * @param items       源容器
     * @param transformer 变换函数
     * @return 返回映射结果
     */
    @SuppressWarnings("WeakerAccess")
    public static <K, V1, V2> Map<K, V2> project(Map<K, V1> items, Function<V1, V2> transformer) {
        Map<K, V2> result = new HashMap<>();

        for (K key : items.keySet()) {
            V2 value = transformer.apply(items.get(key));
            result.put(key, value);
        }

        return result;
    }

    /**
     * 将容器(K,V1)映射为(K,F)。F是V1的域
     *
     * @param <K>      键类型
     * @param <V1>     源容器的值类型
     * @param <V2>     结果容器的值类型
     * @param items    源容器
     * @param keyField 域名字
     * @return 返回映射结果
     * @throws RuntimeException 如果无法通过反射获得域，抛出异常
     */
    public static <K, V1, V2> Map<K, V2> project(Map<K, V1> items, @SuppressWarnings("SameParameterValue") String keyField) throws RuntimeException {
        return project(items, new Function<V1, V2>() {
            @Override
            public V2 apply(V1 v1) {
                try {
                    return FieldGetter.get(v1, keyField);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 从一组对象建立关联数组。将V映射为(K,V)，其中K是V的域
     *
     * @param <K>      键类型
     * @param <V>      值类型
     * @param items    数组
     * @param keyField 作为键的域的名字
     * @return 返回映射结果
     * @throws IllegalArgumentException 参数非法
     * @throws IllegalAccessException   在keyField不可访问时抛出
     * @throws NoSuchFieldException     在keyField不存在时抛出
     * @throws SecurityException        在keyField不可访问时抛出
     */
    public static <K, V> Map<K, V> makeKeyValueTable(Collection<V> items, @SuppressWarnings("SameParameterValue") String keyField) throws IllegalArgumentException,
            IllegalAccessException,
            NoSuchFieldException,
            SecurityException {

        Map<K, V> result = new HashMap<>();

        for (V v : items) {
            K k = FieldGetter.get(v, keyField);
            result.put(k, v);
        }

        return result;
    }
}
