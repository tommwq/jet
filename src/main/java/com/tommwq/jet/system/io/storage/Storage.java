package com.tommwq.jet.system.io.storage;

import java.util.Optional;

/**
 * 简单存储模型
 */
public interface Storage {
    /**
     * 读取数据
     *
     * @param path 数据路径
     * @return 返回读取结果
     * @throws IllegalArgumentException 如果路径不存在，抛出此异常
     */
    Object read(String path) throws IllegalArgumentException;

    /**
     * 写入数据
     *
     * @param path 数据路径
     * @param data 数据内容
     * @throws UnsupportedOperationException 如果不支持写入该对象，抛出此异常
     */
    void write(String path, Object data) throws UnsupportedOperationException;

    /**
     * 持久性写入数据
     *
     * @param path 数据路径
     * @param data 数据内容
     * @throws UnsupportedOperationException 如果不支持持久性写入，抛出此异常
     */
    void mustWrite(String path, Object data) throws UnsupportedOperationException;

    /**
     * 尝试读取数据
     *
     * @param path 数据路径
     * @return 返回查询结果
     */
    Optional<Object> lookup(String path);

    /**
     * 删除数据
     *
     * @param path 数据路径
     */
    void remove(String path);

    /**
     * 删除全部数据
     */
    void removeAll();

    /**
     * 将改动同步到存储器
     */
    void sync();

    /**
     * @return 是否支持持久化
     */
    boolean isSupportDurable();
}
