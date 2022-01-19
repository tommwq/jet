package com.tommwq.jet.database;

/**
 * SQL 字符串转义接口。
 */
public interface SQLStringEscaper {
    /**
     * 转义 SQL 字符串。
     * @param text SQL 字符串。
     * @return 转义后的字符串。
     */
    String escape(String text);
}


