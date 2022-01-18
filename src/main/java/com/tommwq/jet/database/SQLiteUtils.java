package com.tommwq.jet.database;

/**
 * SQLite工具类。
 */
public class SQLiteUtils {

    /**
     * 对SQL语句进行转义
     *
     * @param sql 要进行转义的SQL
     * @return 返回转义结果
     */
    public static String escape(String sql) {
        sql = sql.replace("/", "//");
        sql = sql.replace("'", "''");
        sql = sql.replace("[", "/[");
        sql = sql.replace("]", "/]");
        sql = sql.replace("%", "/%");
        sql = sql.replace("&", "/&");
        sql = sql.replace("_", "/_");
        sql = sql.replace("(", "/(");
        sql = sql.replace(")", "/)");

        return sql;
    }
}
