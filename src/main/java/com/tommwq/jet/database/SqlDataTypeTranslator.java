package com.tommwq.jet.database;

/**
 * SQL 数据类型和 Java 数据类型转换器
 */
public interface SqlDataTypeTranslator {

    /**
     * 将 SQL 数据类型转换为 Java 类
     *
     * @param dataType SQL 数据类型
     * @return 对应的 Java 类
     */
    Class toJavaType(String dataType);

    /**
     * 将 Java 类转换为 SQL 数据类型
     *
     * @param clazz Java 类
     * @return 对应的 SQL 数据类型
     */
    String toSqlType(Class clazz);
}


