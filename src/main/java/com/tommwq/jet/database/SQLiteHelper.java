package com.tommwq.jet.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends DatabaseHelper {

  public SQLiteHelper(Connection aConnection) {
    super(aConnection, new SQLiteDataTypeTranslator(), new SQLiteStringEscaper());
  }

  @Override
  public boolean tableExist(String tableName) throws SQLException {
    String sql = String.format("select count(*) as exist from sqlite_master where type='table' and name='%s'",
                               tableName);

    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {
      if (!resultSet.next()) {
        throw new RuntimeException("fail to query records from sqlite_master table");
      }

      boolean exist = (resultSet.getInt("exist") == 1);
      return exist;
    }
  }

  public static final String SQLITE_DATETYPE_TEXT = "TEXT";
  public static final String SQLITE_DATETYPE_INTEGER = "INTEGER";
  public static final String SQLITE_DATETYPE_REAL = "REAL";
  public static final String SQLITE_DATETYPE_BLOB = "BLOB";
  private static final String QUERY_CREATE_TABLE_SQL_FORMAT = "select sql from sqlite_master where name = '%s' and type='table';";
  private static final String QUERY_TABLE_SQL = "select table from sqlite_master where name != 'androidmetadata' and type='table';";

  /**
   * 将Java数据类型转换为SQLite数据类型
   *
   * @param javaDataType java数据类型
   * @return 对应的SQLite数据类型
   */
  public static String toSqliteDataType(String javaDataType) {
    switch (javaDataType) {
    case "byte":
    case "java.lang.Byte":
    case "short":
    case "java.lang.Short":
    case "int":
    case "java.lang.Integer":
    case "long":
    case "java.lang.Long":
      return SQLITE_DATETYPE_INTEGER;
    case "float":
    case "java.lang.Float":
    case "double":
    case "java.lang.Double":
      return SQLITE_DATETYPE_REAL;
    case "java.lang.String":
      return SQLITE_DATETYPE_TEXT;
    default:
      return SQLITE_DATETYPE_BLOB;
    }
  }

  /**
   * 解析建表语句，返回表中各列的描述符。
   *
   * @param createTableSql 建表语句
   * @return 各列描述符
   */
  public static List<ColumnDescriptor> parseCreateTableSql(String createTableSql) {
    List<ColumnDescriptor> columns = new ArrayList<>();

    // 取第一个(和最后一个)之间的部分
    int leftParenthesis = createTableSql.indexOf('(');
    int rightParenthesis = createTableSql.lastIndexOf(')');
    if (leftParenthesis == -1 || rightParenthesis == -1) {
      throw new RuntimeException("unknown sql syntax: " + createTableSql);
    }
    String columnLines = createTableSql.substring(leftParenthesis + 1, rightParenthesis - 1);

    // 用分号分行
    String[] columnDefines = columnLines.split(",");
    for (String columnDefine : columnDefines) {
      // 用空白符分块，第一块是类型，第二块是列名
      String[] block = columnDefine.trim().split("\\s");
      if (block.length < 2) {
        throw new RuntimeException("unknown sql syntax: " + createTableSql + " > " + columnDefine);
      }
      String name = block[0];
      String dataType = block[1];
      columns.add(new ColumnDescriptor(name, dataType));
    }

    return columns;
  }

  // /**
  //  * 查询建表语句
  //  *
  //  * @param sqlite SQLiteDatabase对象
  //  * @param table  表名字
  //  * @return 建表语句
  //  */
  // public static Optional<String> queryCreateTableSql(SQLiteDatabase sqlite, String table) {
  //     String result = null;

  //     String sql = String.format(QUERY_CREATE_TABLE_SQL_FORMAT, table);
  //     Cursor cursor = sqlite.rawQuery(sql, new String[]{});
  //     if (cursor.moveToFirst()) {
  //         result = cursor.getString(0);
  //     }
  //     cursor.close();

  //     return Optional.ofNullable(result);
  // }

  // /**
  //  * 查询数据库中的表
  //  *
  //  * @param sqlite 数据库对象
  //  * @return 表名列表
  //  */
  // public static List<String> listTable(SQLiteDatabase sqlite) {
  //     List<String> tables = new ArrayList<>();
  //     Cursor cursor = sqlite.rawQuery(QUERY_TABLE_SQL, new String[]{});
  //     if (cursor.moveToFirst()) {
  //         do {
  //             String table = cursor.getString(0);
  //             tables.add(table);
  //         } while (cursor.moveToNext());
  //     }
  //     cursor.close();
  //     return tables;
  // }
}
