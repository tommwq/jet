package com.tommwq.jet.database;

public interface DataTypeTranslater {

    Class toJavaType(String dataType);

    String toDataType(Class clazz);
}


