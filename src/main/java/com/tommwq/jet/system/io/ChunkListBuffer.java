package com.tommwq.jet.system.io;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 内存块链表缓冲区
 */
public class ChunkListBuffer {
    private static final int DEFAULT_CHUNK_CAPACITY = 4096;
    private final List<Chunk> chunks = new ArrayList<>(16);
    private final int chunkCapacity;
    private int dataSize;

    /**
     * 创建内存块链表缓冲区
     *
     * @param chunkCapacity 每个内存的大小
     */
    public ChunkListBuffer(int chunkCapacity) {
        this.chunkCapacity = chunkCapacity;
    }

    /**
     * 创建内存块链表缓冲区
     */
    public ChunkListBuffer() {
        this(DEFAULT_CHUNK_CAPACITY);
    }

    /**
     * 追加数据。
     *
     * @param data   数据缓冲区
     * @param offset 数据偏移量
     * @param length 数据大小
     * @return 内存块链表缓冲区自身
     */
    public ChunkListBuffer append(byte[] data, int offset, int length) {
        Chunk chunk = getAvailableChunk();
        int free = chunk.getFreeCapacity();
        if (length <= free) {
            chunk.append(data, offset, length);
            dataSize += length;
            return this;
        }

        append(data, offset, free);
        append(data, offset + free, length - free);
        return this;
    }


    /**
     * 追加写数据。
     *
     * @param buffer 数据缓冲区
     * @return 内存块链表缓冲区自身
     */
    public ChunkListBuffer append(byte[] buffer) {
        return append(buffer, 0, buffer.length);
    }

    /**
     * 追加写字符串数据
     *
     * @param text    字符串
     * @param charset 字符编码
     * @return 内存块链表缓冲区自身
     */
    public ChunkListBuffer append(String text, Charset charset) {
        return append(charset.encode(text).array());
    }

    /**
     * 追加写字符串
     *
     * @param text 字符串，采用系统默认编码
     * @return 内存块链表缓冲区自身
     */
    public ChunkListBuffer append(String text) {
        return append(text, Charset.defaultCharset());
    }

    private Chunk getLastChunk() {
        if (chunks.isEmpty()) {
            Chunk chunk = new Chunk(chunkCapacity);
            chunks.add(chunk);
            return chunk;
        }

        return chunks.get(chunks.size() - 1);
    }

    private Chunk getAvailableChunk() {
        Chunk chunk = getLastChunk();
        if (chunk.isFull()) {
            chunk = new Chunk(chunkCapacity);
            chunks.add(chunk);
        }

        return chunk;
    }

    /**
     * 获取数据大小。
     *
     * @return 数据大小
     */
    public int getDataSize() {
        return dataSize;
    }

    public byte[] getData() {
        byte[] buffer = new byte[dataSize];
        int offset = 0;
        for (Chunk chunk : chunks) {
            int chunkSize = chunk.getDataSize();
            System.arraycopy(chunk.getRawData(), 0, buffer, offset, chunkSize);
            offset += chunkSize;
        }
        return buffer;
    }
}
