/**
 * 处理byte[]的常用函数。
 * create: 2016-09-21
 * modify: 2016-10-20
 */
package com.tommwq.jet.datatype;

import java.util.ArrayList;

/**
 * 字节常用函数类。
 */
public class ByteArray {
    /**
     * 从byte数组中查找byte。
     *
     * @param array  数组
     * @param offset 起始位置
     * @param length 最大查找长度
     * @param target 要查找的对象
     * @return target从offset起首次在array中出现的位置。如果没找到，返回-1。
     */
    public static int indexOf(byte[] array, int offset, int length, byte target) {
        int pos = -1;
        for (int i = offset; i < offset + length && i < array.length; i++) {
            if (array[i] == target) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    /**
     * 从byte数组中查找byte。
     *
     * @param array  数组
     * @param target 要查找的对象
     * @return target在array中出现的位置。如果没找到，返回-1。
     */
    public static int indexOf(byte[] array, byte target) {
        return indexOf(array, 0, array.length, target);
    }

    /**
     * 从byte数组中查找byte。
     *
     * @param array  数组
     * @param offset 起始位置
     * @param length 最大查找长度
     * @param target 要查找的对象
     * @return target从offset起首次在array中出现的位置。如果没找到，返回-1。
     */
    public static int indexOf(byte[] array, int offset, int length, byte[] target) {
        // TODO use a better algorithm, Turbo-BM
        // http://igm.univ-mlv.fr/~lecroq/string/node15.html
        boolean matched;
        for (int i = 0; i < length - target.length; i++) {
            matched = true;
            for (int j = 0; j < target.length; j++) {
                if (target[j] != array[offset + i + j]) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return offset + i;
            }
        }

        return -1;
    }

    /**
     * 从byte数组中查找byte。
     *
     * @param array  数组
     * @param offset 起始位置
     * @param target 要查找的对象
     * @return target在array中出现的位置。如果没找到，返回-1。
     */
    public static int indexOf(byte[] array, int offset, byte[] target) {
        return indexOf(array, offset, array.length - offset, target);
    }

    /**
     * 从byte数组中查找byte。
     *
     * @param array  数组
     * @param target 要查找的对象
     * @return target在array中出现的位置。如果没找到，返回-1。
     */
    public static int indexOf(byte[] array, byte[] target) {
        return indexOf(array, 0, array.length, target);
    }

    /**
     * 导出字节数组为字符串
     *
     * @param data 字节数组
     * @return 返回导出的结果
     */
    public static String dump(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int width = 16;
        int half = width / 2;
        int row = 0;
        int column = 0;

        for (int i = 0; i < data.length; i++) {
            row = i / width;
            column = i % width;

            if (column == 0 && row != 0) {
                sb.append("\n");
            }

            if (column == half) {
                sb.append("  ");
            }

            sb.append(String.format("%02x", data[i]));

            if (column != width - 1) {
                sb.append(" ");
                continue;
            }

            sb.append("   ");
            for (int j = width - 1; j > 0; j--) {
                char c = (char) data[i - j];

                if (!Character.isLetterOrDigit(c) && "`~!@#$%^&*()_-=+[]{}|\\;:'\",<.>/?".indexOf(c) == -1) {
                    c = '.';
                }

                sb.append(c);
            }
        }

        if (column != width - 1) {
            for (int j = 0; j < width - 1 - column; ++j) {
                sb.append("   ");
            }
            if (column < half) {
                sb.append("  ");
            }

            sb.append("  ");
            for (int j = column; j > 0; j--) {
                char c = (char) data[data.length - j];

                if (!Character.isLetterOrDigit(c) && "`~!@#$%^&*()_-=+[]{}|\\;:'\",<.>/?".indexOf(c) == -1) {
                    c = '.';
                }

                sb.append(c);
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * 连接字节数组
     *
     * @param array1 数组1
     * @param array2 数组2
     * @return 返回拼接的数组
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        return concat(array1, array1.length, array2, array2.length);
    }

    /**
     * 连接字节数组
     *
     * @param array1 数组1
     * @param size1  长度1
     * @param array2 数组2
     * @param size2  长度2
     * @return 返回拼接的数组
     */
    public static byte[] concat(byte[] array1, int size1, byte[] array2, int size2) {
        byte[] result = new byte[size1 + size2];
        System.arraycopy(array1, 0, result, 0, size1);
        System.arraycopy(array2, 0, result, size1, size2);
        return result;
    }

    public byte[][] split(byte[] buffer, byte[] sep) {
        int pos1, pos2;
        int offset = 0;
        pos1 = 0;
        pos2 = 0;
        ArrayList<byte[]> list = new ArrayList<>();
        byte[] buf;

        while (true) {
            pos2 = indexOf(buffer, pos1, sep);
            if (pos2 == -1) {
                pos2 = buffer.length;
            }
            buf = new byte[pos2 - pos1];
            System.arraycopy(buffer, pos1, buf, 0, pos2 - pos1);
            list.add(buf);
            pos1 = pos2 + sep.length;
            if (pos1 >= buffer.length) {
                break;
            }
        }

        byte[][] result = new byte[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);

        }
        return result;

    }
}
