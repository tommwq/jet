package com.tommwq.jet.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// TODO 从 DatabaseHelper 派生
public class SQLServerHelper {

  /*
   * 只适用于SQL Server。
   */
  public static boolean isDatabaseExist(Connection connection, String database) throws SQLException {
    final int DATABASE_NAME_INDEX = 1;
    try (ResultSet resultSet = connection.getMetaData().getCatalogs()) {
      while (resultSet.next()) {
        String databaseName = resultSet.getString(DATABASE_NAME_INDEX);
        if (databaseName.equals(database)) {
          return true;
        }
      }
    }

    return false;
  }

  /*
   * 只适用于SQL Server。
   */
  public static boolean isTableExist(Connection connection, String database, String table) throws SQLException {
    final int TABLE_NAME_INDEX = 3;
    final String SCHEMA_DBO = "dbo";

    try (ResultSet resultSet = connection.getMetaData().getTables(database, null, table, null)) {
      while (resultSet.next()) {
        String tableName = resultSet.getString(TABLE_NAME_INDEX);
        if (tableName.equals(table)) {
          return true;
        }
      }
    }

    return false;
  }

  public static void createDatabase(Connection connection, String database) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format("create database %s", database));
      connection.commit();
    }
  }
}
