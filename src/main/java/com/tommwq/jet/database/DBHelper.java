package com.tommwq.jet.database;

import com.tommwq.jet.function.Call;
import com.tommwq.jet.runtime.reflect.ReflectUtil;
import com.tommwq.jet.datatype.DataType;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class DBHelper {

    protected DataTypeTranslater dataTypeTranslater;
    protected Connection connection;
    protected StringEscaper stringEscaper;
    public DBHelper(Connection aConnection, DataTypeTranslater aDataTypeTranslater, StringEscaper aStringEscaper) {
        connection = aConnection;
        dataTypeTranslater = aDataTypeTranslater;
        stringEscaper = aStringEscaper;
    }

    public abstract boolean tableExist(String tableName) throws SQLException;

    public DBHelper createTable(String createTableSQL) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
        return this;
    }

    public DBHelper createTableInNeed(String tableName, String createTableSQL) throws SQLException {
        if (!tableExist(tableName)) {
            createTable(createTableSQL);
        }
        return this;
    }

    public DBHelper createTableInNeed(Class clazz) throws SQLException {
        createTableInNeed(clazz.getSimpleName(), createTableSQL(clazz));
        return this;
    }

    public String createTableSQL(Object object) {
        return createTableSQL(object.getClass());
    }

    public String createTableSQL(Class clazz) {
        String tableName = clazz.getSimpleName();
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ")
                .append(tableName)
                .append(" (");

        builder.append(String.join(",", Stream.of(clazz.getDeclaredFields())
                .map(field -> String.format("%s %s",
                        field.getName(),
                        dataTypeTranslater.toDataType(field.getType())))
                .collect(Collectors.toList())));

        builder.append(")");
        return builder.toString();
    }

    public String selectSQL(Class clazz) {
        return new StringBuilder()
                .append("SELECT ")
                .append(String.join(",", Stream.of(clazz.getDeclaredFields())
                        .map(field -> field.getName())
                        .collect(Collectors.toList())))
                .append(" FROM ")
                .append(clazz.getSimpleName())
                .toString();
    }

    public String selectSQL(Object object) {
        return selectSQL(object.getClass());
    }

    public String insertSQL(Object object) {
        Class clazz = object.getClass();
        String sql = new StringBuilder()
                .append("INSERT INTO ")
                .append(clazz.getSimpleName())
                .append(" (")
                .append(String.join(",", Stream.of(clazz.getDeclaredFields())
                        .map(field -> field.getName())
                        .collect(Collectors.toList())))
                .append(") VALUES (")
                .append(String.join(",", Stream.of(clazz.getDeclaredFields())
                        .map(field -> {
                            try {
                                Class type = field.getType();
                                Object value = ReflectUtil.fieldValue(object, field);
                                String valueString = value == null ? "null" : value.toString();
                                if (DataType.isString(type)) {
                                    valueString = "\"" + escape(valueString) + "\"";
                                }
                                if (DataType.isBoolean(type)) {
                                    valueString = String.valueOf(DataType.booleanToInt((boolean) value));
                                }
                                if (DataType.isEnum(type)) {
                                    valueString = String.valueOf(DataType.enumToInt((Enum) value));
                                }
                                return valueString;
                            } catch (NoSuchFieldException e) {
                                throw new RuntimeException("unknown error", e);
                            }
                        })
                        .collect(Collectors.toList())))
                .append(")")
                .toString();

        // TODO
        System.out.println(sql);

        return sql;
    }

    public String deleteSQL(Object object) {
        Class clazz = object.getClass();
        String sql = new StringBuilder()
                .append("DELETE FROM ")
                .append(clazz.getSimpleName())
                .append(" WHERE ")
                .append(String.join(" AND ", Stream.of(clazz.getDeclaredFields())
                        .map(field -> {
                            try {
                                Class type = field.getType();
                                String fieldName = field.getName();
                                Object value = ReflectUtil.fieldValue(object, field);
                                String valueString = value == null ? "null" : value.toString();
                                if (DataType.isString(type)) {
                                    valueString = "\"" + escape(valueString) + "\"";
                                }
                                if (DataType.isBoolean(type)) {
                                    valueString = String.valueOf(DataType.booleanToInt((boolean) value));
                                }
                                if (DataType.isEnum(type)) {
                                    valueString = String.valueOf(DataType.enumToInt((Enum) value));
                                }
                                return String.format("%s=%s", fieldName, valueString);
                            } catch (NoSuchFieldException e) {
                                throw new RuntimeException("unknown error", e);
                            }
                        })
                        .collect(Collectors.toList())))
                .append(")")
                .toString();

        // TODO
        System.out.println(sql);

        return sql;
    }

    public String deleteSQL(Class clazz, Map<String, String> condition) {
        String sql = new StringBuilder()
                .append("DELETE FROM ")
                .append(clazz.getSimpleName())
                .append(" WHERE ")
                .append(String.join(" AND ", condition.entrySet()
                        .stream()
                        .map(entry -> String.format("%s='%s'", entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())))
                .toString();

        // TODO
        System.out.println(sql);

        return sql;
    }

    public String prepareInsertSQL(Class clazz) {
        return new StringBuilder()
                .append("INSERT INTO ")
                .append(clazz.getSimpleName())
                .append(" (")
                .append(String.join(",", Stream.of(clazz.getDeclaredFields())
                        .map(field -> field.getName())
                        .collect(Collectors.toList())))
                .append(") VALUES (")
                .append(String.join(",", Stream.of(clazz.getDeclaredFields())
                        .map(field -> "?")
                        .collect(Collectors.toList())))
                .append(")")
                .toString();
    }

    public PreparedStatement selectStatement(Object object) throws SQLException {
        return connection.prepareStatement(selectSQL(object));
    }

    public PreparedStatement selectStatement(Class clazz) throws SQLException {
        return connection.prepareStatement(selectSQL(clazz));
    }

    public PreparedStatement insertStatement(Object object) throws SQLException {
        return connection.prepareStatement(insertSQL(object));
    }

    public PreparedStatement insertStatement(Class clazz) throws SQLException {
        return connection.prepareStatement(prepareInsertSQL(clazz));
    }

    public PreparedStatement deleteStatement(Object object) throws SQLException {
        return connection.prepareStatement(deleteSQL(object));
    }

    public PreparedStatement deleteStatement(Class clazz, Map<String, String> condition) throws SQLException {
        return connection.prepareStatement(deleteSQL(clazz, condition));
    }

    public void insert(Object object) throws SQLException {
        insertStatement(object).executeUpdate();
    }

    public void insert(Object... objectArray) throws SQLException {
        for (Object object : objectArray) {
            insertStatement(object).executeUpdate();
        }
    }

    public <T> List<T> select(Class<T> clazz) throws SQLException {
        List<T> list = new ArrayList<>();
        ResultSet resultSet = selectStatement(clazz).executeQuery();
        while (resultSet.next()) {
            T element = (T) new Call((Void) -> clazz.newInstance(), null, null).rethrow().result();

            for (Field field : clazz.getDeclaredFields()) {
                Class type = field.getType();
                String name = field.getName();
                Object value = null;

                if (DataType.isString(type)) {
                    value = resultSet.getString(name);
                }
                if (DataType.isLong(type)) {
                    value = resultSet.getLong(name);
                }
                if (DataType.isInteger(type)) {
                    value = resultSet.getInt(name);
                }
                if (DataType.isEnum(type)) {
                    value = DataType.intToEnum(resultSet.getInt(name), type);
                }
                if (value != null) {
                    ReflectUtil.setDeclaredField(element, field, value);
                }
            }

            list.add(element);
        }

        return list;
    }

    public void delete(Object object) throws SQLException {
        deleteStatement(object).executeUpdate();
    }

    public void delete(Class clazz, Map<String, String> condition) throws SQLException {
        deleteStatement(clazz, condition).executeUpdate();
    }

    public String escape(String text) {
        return stringEscaper.escape(text);
    }
}
