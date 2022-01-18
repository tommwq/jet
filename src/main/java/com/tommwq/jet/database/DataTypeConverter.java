package com.tommwq.jet.database;

public interface DataTypeConverter {
    Class toJavaClass(String dataType);

    String toDataType(Class clazz);
}


