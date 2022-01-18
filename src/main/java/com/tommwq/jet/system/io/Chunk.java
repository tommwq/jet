package com.tommwq.jet.system.io;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 内存块
 */
public class Chunk {
    private int capacity;
    private int dataSize;
    private byte[] data = null;

    /**
     * 创建内存块
     *
     * @param capacity 容量
     */
    public Chunk(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 获得内存块容量
     *
     * @return 内存块容量
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 获得内存块数据大小
     *
     * @return 数据大小
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * 获得内存块剩余容量
     *
     * @return 内存块剩余容量
     */
    public int getFreeCapacity() {
        return capacity - dataSize;
    }

    /**
     * 检查内存块是否已满
     *
     * @return 如果内存块已满，返回true。否则返回false。
     */
    public boolean isFull() {
        return dataSize == capacity;
    }

    /**
     * 检查内存块是否已满
     *
     * @return 如果内存块已满，返回true。否则返回false。
     */
    public boolean isNotFull() {
        return dataSize < capacity;
    }

    /**
     * 向内存块追加写入数据
     *
     * @param buffer 数据缓冲区
     * @param offset 数据偏移量
     * @param length 数据长度
     * @return 内存块对象自身
     * @throws IndexOutOfBoundsException 如果内存块容量不够，抛出异常。
     */
    public Chunk append(byte[] buffer, int offset, int length) throws IndexOutOfBoundsException {
        if (offset < 0 || offset + length > buffer.length) {
            throw new IndexOutOfBoundsException();
        }

        if (getFreeCapacity() < length) {
            throw new IndexOutOfBoundsException();
        }

        allocateInNeed();

        System.arraycopy(buffer, offset, data, dataSize, length);
        dataSize += length;
        return this;
    }

    /**
     * 追加写数据。
     *
     * @param buffer 数据缓冲区
     * @return 内存块对象自身
     */
    public Chunk append(byte[] buffer) {
        return append(buffer, 0, buffer.length);
    }

    /**
     * 追加写字符串数据
     *
     * @param text    字符串
     * @param charset 字符编码
     * @return 内存块对象自身
     */
    public Chunk append(String text, Charset charset) {
        return append(charset.encode(text).array());
    }

    /**
     * 追加写字符串
     *
     * @param text 字符串，采用系统默认编码
     * @return 内存块对象自身
     */
    public Chunk append(String text) {
        return append(text, Charset.defaultCharset());
    }

    /**
     * 获得数据。
     *
     * @return 数据副本。
     */
    public byte[] getData() {
        return Arrays.copyOfRange(data, 0, dataSize);
    }

    private void allocateInNeed() {
        if (data != null) {
            return;
        }
        data = new byte[capacity];
    }

    byte[] getRawData() {
        return data;
    }
}
