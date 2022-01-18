package com.tommwq.jet.database;

import java.util.HashMap;
import java.util.Map;

public class SQLiteDataTypeConverter implements DataTypeConverter {

    private static Map<String, Class> dataTypeTable = new HashMap<>();
    private static Map<Class, String> javaTypeTable = new HashMap<>();

    static {
        dataTypeTable.put("BLOB", Object.class);
        dataTypeTable.put("TEXT", String.class);
        dataTypeTable.put("REAL", Double.class);
        dataTypeTable.put("INTEGER", Long.class);
    }

    static {
        javaTypeTable.put(Boolean.class, "INTEGER");
        javaTypeTable.put(Byte.class, "INTEGER");
        javaTypeTable.put(Character.class, "INTEGER");
        javaTypeTable.put(Double.class, "REAL");
        javaTypeTable.put(Float.class, "REAL");
        javaTypeTable.put(Integer.class, "INTEGER");
        javaTypeTable.put(Long.class, "INTEGER");
        javaTypeTable.put(Short.class, "INTEGER");
        javaTypeTable.put(String.class, "TEXT");
        javaTypeTable.put(boolean.class, "INTEGER");
        javaTypeTable.put(byte.class, "INTEGER");
        javaTypeTable.put(char.class, "INTEGER");
        javaTypeTable.put(double.class, "REAL");
        javaTypeTable.put(float.class, "REAL");
        javaTypeTable.put(int.class, "INTEGER");
        javaTypeTable.put(long.class, "INTEGER");
        javaTypeTable.put(short.class, "INTEGER");
    }

    public Class toJavaClass(String dataType) {
        if (dataTypeTable.containsKey(dataType)) {
            return dataTypeTable.get(dataType);
        }

        return Object.class;
    }

    public String toDataType(Class clazz) {
        Class type = clazz;
        if (clazz.isEnum()) {
            type = int.class;
        }

        if (javaTypeTable.containsKey(type)) {
            return javaTypeTable.get(type);
        }

        return "BLOB";
    }
}


