package com.tommwq.jet.runtime.reflect;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * utility functions for reflection
 */
public class ReflectUtils {

    /**
     * 设置对象域。
     *
     * @param object        对象
     * @param nameAndValues 域名和值数组
     */
    public static Object setFields(Object object, Object... nameAndValues)
            throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < nameAndValues.length; i += 2) {
            String fieldName = String.valueOf(nameAndValues[i]);
            Object value = null;
            if (i + 1 < nameAndValues.length) {
                value = nameAndValues[i + 1];
            }
            setField(object, fieldName, value);
        }

        return object;
    }

    /**
     * 设置对象域。
     *
     * @param object        对象
     * @param nameAndValues 域名和值数组
     */
    public static Object attemptSetFields(Object object, Object... nameAndValues) {
        for (int i = 0; i < nameAndValues.length; i += 2) {
            String fieldName = String.valueOf(nameAndValues[i]);
            Object value = null;
            if (i + 1 < nameAndValues.length) {
                value = nameAndValues[i + 1];
            }
            attemptSetField(object, fieldName, value);
        }

        return object;
    }

    /**
     * 设置对象域。
     *
     * @param object    对象
     * @param fieldName 域
     * @param value     值
     */
    public static Object setField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
        return object;
    }

    /**
     * 设置对象域。
     *
     * @param object    对象
     * @param fieldName 域
     * @param value     值
     */
    public static Object attemptSetField(Object object, String fieldName, Object value) {
        try {
            setField(object, fieldName, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }

        return object;
    }

    public static String getStringField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException, ClassCastException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(object);
    }

    public static String attemptGetStringField(Object object, String fieldName) {
        try {
            return getStringField(object, fieldName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }

        return null;
    }

    public static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    public static Object attemptGetField(Object object, String fieldName) {
        try {
            return getField(object, fieldName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }

        return null;
    }

    public static void attemptSetPrimitiveWrapperAndStringField(Object object, String fieldName, String valueString) {
        try {
            setPrimitiveWrapperAndStringField(object, fieldName, valueString);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 设置对象域
     *
     * @param object      对象
     * @param fieldName   域名字
     * @param valueString 值字符串
     * @return 对象自身
     * @throws NoSuchFieldException   如果域不存在，抛出此异常
     * @throws IllegalAccessException 正常情况下不会抛出这个异常
     */
    public static Object setPrimitiveWrapperAndStringField(Object object, String fieldName, String valueString)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        String typeName = field.getType().getName();
        Object value = null;
        switch (typeName) {
            case "int": // fallthrough
            case "java.lang.Integer":
                value = Integer.valueOf(valueString);
                break;
            case "long": // fallthrough
            case "java.lang.Long":
                value = Long.valueOf(valueString);
                break;
            case "java.lang.String":
                value = String.valueOf(valueString);
                break;
            case "byte": // fallthrough
            case "java.lang.Byte":
                value = Byte.valueOf(valueString);
                break;
//            case "java.lang.Character":
//                value = Character.valueOf(valueString[0]);
//                break;
            case "java.lang.Boolean":
                value = Boolean.valueOf(valueString);
                break;
            case "double": // fallthrough
            case "java.lang.Double":
                value = Double.valueOf(valueString);
                break;
            case "float": // fallthrough
            case "java.lang.Float":
                value = Float.valueOf(valueString);
                break;
            case "short": // fallthrough
            case "java.lang.Short":
                value = Short.valueOf(valueString);
                break;
            default:
                break;
        }

        field.setAccessible(true);
        field.set(object, value);
        return object;
    }

    public static boolean isPrimitiveOrString(Class<?> type) {
        return isPrimitive(type) || isString(type);
    }

    public static boolean isPrimitive(Class<?> type) {
        switch (type.getName()) {
            case "int":
            case "short":
            case "long":
            case "boolean":
            case "byte":
            case "char":
            case "float":
            case "double":
                return true;
            default:
                return false;
        }
    }

    public static boolean isString(Class<?> type) {
        return "java.lang.String".equals(type.getName());
    }

    public static Object toPrimitive(Class<?> primitiveType, String valueString) {
        switch (primitiveType.getName()) {
            case "int":
                return Integer.parseInt(valueString);
            case "short":
                return Short.parseShort(valueString);
            case "long":
                return Long.parseLong(valueString);
            case "boolean":
                return Boolean.parseBoolean(valueString);
            case "byte":
                return Byte.parseByte(valueString);
            case "char":
                return valueString.charAt(0);
            case "float":
                return Float.parseFloat(valueString);
            case "double":
                return Double.parseDouble(valueString);
            default:
                return null;
        }
    }

    /**
     * 从Map建立对象。
     *
     * @param fieldAndValue 对象各field的名字和值
     * @param clazz         对象类
     * @param <T>           对象类型
     * @return 新建对象
     */
    public static <T> T createObject(Map<String, String> fieldAndValue, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        T object = clazz.newInstance();
        for (Map.Entry<String, String> entry : fieldAndValue.entrySet()) {
            attemptSetPrimitiveWrapperAndStringField(object, entry.getKey(), entry.getValue());
        }

        return object;
    }

    /**
     * 将对象转换成另一类对象。这两类对象拥有相同的域定义。
     *
     * @param source      源对象
     * @param targetClazz 目标对象类型
     * @param <T>         目标对象类型
     * @return 转换后的对象
     * @throws IllegalAccessException 源类型或目标类型的域不可访问
     */
    public static <T, R> T convertObject(R source, Class<T> targetClazz) throws IllegalAccessException, InstantiationException {
        return createObject(objectToMap(source), targetClazz);
    }

    /**
     * 从Map列表建立对象列表。
     *
     * @param fieldAndValueList 域-值映射列表
     * @param clazz             对象类
     * @param <T>               对象类型
     * @return 对象列表
     */
    public static <T> List<T> createObjectList(Collection<Map<String, String>> fieldAndValueList, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        ArrayList<T> list = new ArrayList<>(fieldAndValueList.size());

        for (Map<String, String> fieldAndValue : fieldAndValueList) {
            list.add(createObject(fieldAndValue, clazz));
        }

        return list;
    }

    /**
     * 转换对象列表
     *
     * @param <T>         目标对象类型
     * @param sourceList  源对象列表
     * @param targetClass 目标对象类型
     * @return 转换后的目标对象列表
     */
    public static <T, R> List<T> convertObjectList(List<R> sourceList, Class<T> targetClass)
            throws IllegalAccessException, InstantiationException {
        ArrayList<T> list = new ArrayList<>(sourceList.size());

        for (Object source : sourceList) {
            list.add(convertObject(source, targetClass));
        }

        return list;
    }

    /**
     * 从对象生成Map
     *
     * @param object 对象
     * @return 由field和value组成的map。
     */
    public static Map<String, String> objectToMap(Object object) throws IllegalAccessException {
        Map<String, String> m = new HashMap<>(16);
        if (object == null) {
            return m;
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String key = field.getName();
            Object value = field.get(object);
            String valueString = value == null ? "" : String.valueOf(value);
            m.put(key, valueString);
        }
        return m;
    }

    /**
     * 从对象数组生成Map
     *
     * @param values 对象数组
     * @return 由对象数组生成的map
     */
    public static Map<String, String> valuesToMap(Object... values) {
        Map<String, String> m = new HashMap<>(values.length / 2 + 1);
        for (int i = 0; i < values.length; i += 2) {
            Object key = values[i];
            if (key == null) {
                continue;
            }
            String keyString = String.valueOf(key);
            Object value = null;
            if (i + 1 < values.length) {
                value = values[i + 1];
            }
            String valueString = value == null ? "" : String.valueOf(value);
            m.put(keyString, valueString);
        }
        return m;
    }

    /**
     * 将类型A的POJO映射为类型B的POJO。
     *
     * @param from                源对象
     * @param toClass             目标类型
     * @param skipNullField       是否忽略源对象中的null域
     * @param nullReplacement     skipNullField为false时，源对象null域映射的值
     * @param fieldTranslateTable 域名字映射表
     * @param <FROM>              源对象类型
     * @param <TO>                目标对象类型
     */
    public static <FROM, TO> TO translate(FROM from, Class<TO> toClass, Map<String, String> fieldTranslateTable, boolean skipNullField, Object nullReplacement)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        TO to = toClass.newInstance();
        for (Field field : from.getClass().getDeclaredFields()) {
            String sourceField = field.getName();
            String targetField = fieldTranslateTable.getOrDefault(sourceField, sourceField);
            Object value = getField(from, sourceField);
            if (value == null) {
                value = nullReplacement;
                if (skipNullField) {
                    continue;
                }
            }
            setField(to, targetField, value);
        }
        return to;
    }

    /**
     * 将类型A的POJO映射为类型B的POJO。忽略源对象中的null域。
     *
     * @param from                源对象
     * @param toClass             目标类型
     * @param fieldTranslateTable 域名字映射表
     * @param <FROM>              源对象类型
     * @param <TO>                目标对象类型
     */
    public static <FROM, TO> TO translate(FROM from, Class<TO> toClass, Map<String, String> fieldTranslateTable)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        return translate(from, toClass, fieldTranslateTable, true, null);
    }

    /**
     * 创建普通对象。即全部域都是是String类型的对象。
     *
     * @param clazz          对象类
     * @param fieldAndValues 域名和值列表
     * @param <T>            对象类型
     */
    public static <T> T createPlainObject(Class<T> clazz, String... fieldAndValues)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        T instance = clazz.newInstance();

        for (int i = 0; i < fieldAndValues.length; i++) {
            String field = fieldAndValues[i];
            i = i + 1;
            String value = "";
            if (i < fieldAndValues.length) {
                value = fieldAndValues[i];
            }
            setField(instance, field, value);
        }
        return instance;
    }

    /**
     * 合并对象。
     * <p>
     * 目标和源的同名域必须具有相同类型。
     *
     * @param dest   合并目标对象
     * @param source 合并源对象
     * @param <T>    合并目标类型
     * @return 合并后的对象
     */
    public static <T> T merge(T dest, Object source) throws IllegalAccessException {
        if (source == null || dest == null) {
            return dest;
        }

        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            attemptSetField(dest, field.getName(), field.get(source));
        }

        return dest;
    }

    /**
     * test if object contain a field
     */
    public static boolean containField(Object object, String fieldName) {
        return Stream.of(object.getClass().getFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    /**
     * test if object contain a (declared) field
     */
    public static boolean containDeclaredField(Object object, String fieldName) {
        return Stream.of(object.getClass().getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    /**
     * get value of a declared field, ignore it's accessibility
     */
    public static Object declaredFieldValue(Object object, String fieldName) throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }

        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }

        if (!accessible) {
            field.setAccessible(accessible);
        }

        return value;
    }

    /**
     * initialize an declared field if it's null, ignore it's accessibility
     */
    public static Object initializeDeclaredFieldInNeed(Object object, String fieldName)
            throws NoSuchFieldException, InstantiationException {
        Field field = object.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }

        Object value = null;
        try {
            value = field.get(object);
            if (value == null) {
                value = field.getType().newInstance();

                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        if (!accessible) {
            field.setAccessible(accessible);
        }

        return value;
    }

    /**
     * set value for a declared field
     */
    public static void setDeclaredField(Object object, String fieldName, Object value)
            throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        field.setAccessible(accessible);
    }

    /**
     * set value for a declared field
     */
    public static void setDeclaredField(Object object, Field field, Object value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        field.setAccessible(accessible);
    }

    /**
     * cast string to field's type and set field's value
     * <p>
     * The field must be primitive or String.
     */
    public static void castThenSetFieldString(Object object, Field field, String value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            String fieldType = field.getType().getName();
            if (fieldType.equals("int")) {
                field.setInt(object, Integer.valueOf(value));
            } else if (fieldType.equals("short")) {
                field.setShort(object, Short.valueOf(value));
            } else if (fieldType.equals("long")) {
                field.setLong(object, Long.valueOf(value));
            } else if (fieldType.equals("float")) {
                field.setFloat(object, Float.valueOf(value));
            } else if (fieldType.equals("double")) {
                field.setDouble(object, Double.valueOf(value));
            } else if (fieldType.equals("char")) {
                field.setChar(object, value.charAt(0));
            } else if (fieldType.equals("byte")) {
                field.setByte(object, Byte.valueOf(value));
            } else if (fieldType.equals("boolean")) {
                field.setBoolean(object, Boolean.valueOf(value));
            } else if (fieldType.equals("java.lang.String")) {
                field.set(object, value);
            } else {
                throw new RuntimeException("non-castable type: " + fieldType);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }
        field.setAccessible(accessible);
    }

    /**
     * set value for a declared field
     * <p>
     * The field must be primitive or String.
     */
    public static void setDeclaredFieldString(Object object, String fieldName, String value)
            throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        castThenSetFieldString(object, field, value);
    }

    /**
     * get all interfaces of a class, include it's parents interfaces
     */
    public static List<Class> getAllInterfaces(Class clazz) {
        if (clazz == null) {
            return new ArrayList<Class>();
        }

        return com.tommwq.jet.container.Collections.concat(Arrays.asList(clazz.getInterfaces()),
                getAllInterfaces(clazz.getSuperclass()));
    }

    /**
     * get all superclass of a class
     */
    public static List<Class> getAllSuperclass(Class clazz) {
        List<Class> list = new ArrayList<Class>();
        for (Class superclass = clazz.getSuperclass();
             superclass != null;
             superclass = superclass.getSuperclass()) {
            list.add(superclass);
        }

        return list;
    }

    /**
     * get field value of an object
     */
    public static Object fieldValue(Object object, Field field) throws NoSuchFieldException {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }

        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("unknown error: cannot access an accessible field");
        }

        if (!accessible) {
            field.setAccessible(accessible);
        }

        return value;
    }

    /**
     * 列出声明域名字
     *
     * @param object 对象
     * @return 声明域名字列表
     */
    public static List<String> listDeclaredFieldName(Object object) {
        return listDeclaredFieldName(object.getClass());
    }

    /**
     * 列出声明域名字
     *
     * @param clazz 类
     * @return 声明域名字列表
     */
    public static List<String> listDeclaredFieldName(Class<?> clazz) {
        ArrayList<String> result = new ArrayList<>(16);

        for (Field field : clazz.getDeclaredFields()) {
            result.add(field.getName());
        }
        return result;
    }

    /**
     * 列出非transient域名字（包括继承的域）
     *
     * @param clazz 类型
     * @return 域名字列表
     */
    public static List<String> listNontransientFieldName(Class<?> clazz) {
        ArrayList<String> result = new ArrayList<>(16);
        for (Field field : clazz.getFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            result.add(field.getName());
        }
        return result;
    }

    /**
     * 列出声明域
     *
     * @param object 对象
     * @return 声明域列表
     */
    public static List<Field> listDeclaredField(Object object) {
        return listDeclaredField(object.getClass());
    }

    public static List<Field> listDeclaredField(Class<?> clazz) {
        ArrayList<Field> result = new ArrayList<>(16);
        java.util.Collections.addAll(result, clazz.getDeclaredFields());
        return result;
    }

    /**
     * 列出声明域名字
     *
     * @param clazz 类
     * @return 声明域名字数组
     */
    public static String[] getFieldNameArray(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String[] array = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            array[i] = fields[i].getName();
        }

        return array;
    }

    /**
     * 尝试设置域的值
     *
     * @param object    对象
     * @param fieldName 域名字
     * @param value     值
     */
    public static void trySetField(Object object, String fieldName, Object value) {
        try {
            setField(object, fieldName, value);
        } catch (Exception ignored) {
        }
    }

    /**
     * 将POJO转换为map
     *
     * @param pojo pojo
     * @return pojo对应的map对象
     */
    public static Map<String, String> pojoToMap(Object pojo) {
        try {
            Map<String, String> map = new HashMap<>(16);
            for (Field field : listDeclaredField(pojo.getClass())) {
                String key = field.getName();
                field.setAccessible(true);
                String value = String.valueOf(field.get(pojo));
                map.put(key, value);
            }
            return map;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将POJO转换为json字符串
     *
     * @param pojo pojo对象
     * @return pojo对应的json字符串
     */
    public static <T> String pojoToJson(T pojo) {
        StringBuilder builder = new StringBuilder(64);
        builder.append("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : pojoToMap(pojo).entrySet()) {
            if (!first) {
                builder.append(",");
            }
            first = false;
            builder.append("\"")
                    .append(entry.getKey().replace("\"", "\\\""))
                    .append("\":\"")
                    .append(entry.getValue().replace("\"", "\\\""))
                    .append("\"");
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * 将POJO列表转换为json字符串
     *
     * @param pojoList pojo列表
     * @return pojo列表对应的json字符串
     */
    public static <T> String pojoListToJson(List<T> pojoList) {
        StringBuilder builder = new StringBuilder(256);
        builder.append("[");
        boolean first = true;
        for (T pojo : pojoList) {
            if (!first) {
                builder.append(",");
            }
            first = false;
            builder.append(pojoToJson(pojo));
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * 列出已声明public域
     *
     * @param clazz 类
     * @return 类的public域
     */
    public static List<Field> listDeclaredPublicField(Class<?> clazz) {
        List<Field> result = new ArrayList<>(16);

        for (Field field : clazz.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                result.add(field);
            }
        }

        return result;
    }


    public static String getResourceNameFromPackage(Package aPackage) {
        String resourceName = aPackage.getName();
        return resourceName.replace(".", "/");
    }

    public static List<Class<?>> enumerateClasses() throws Exception {
        List<Class<?>> result = new ArrayList<>();
        for (Package aPackage : Package.getPackages()) {
            result.addAll(enumerateClasses(aPackage));
        }
        return result;
    }

    public static List<Class<?>> enumerateClasses(Package aPackage) throws Exception {
        String packageName = aPackage.getName();
        String resourceName = getResourceNameFromPackage(aPackage);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = loader.getResource(resourceName);
        if (resourceUrl == null) {
            return new ArrayList<>();
        }
        String protocol = resourceUrl.getProtocol();
        if ("jar".equals(protocol)) {
            return enumerateClasses(resourceUrl, aPackage.getName());
        } else {
            URI uri = new URI(resourceUrl.toString());
            File directory = new File(uri.getPath());
            return enumerateClasses(packageName, directory);
        }
    }

    // TODO
    public static List<Class<?>> enumerateClasses(URL resourceUrl, String packageName) throws Exception {
        String jarFileName;
        JarFile jf;
        Enumeration<JarEntry> jarEntries;
        String entryName;
        List<Class<?>> result = new ArrayList<>();

        jarFileName = URLDecoder.decode(resourceUrl.getFile(), "UTF-8");
        jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
        System.out.println(">" + jarFileName);
        jf = new JarFile(jarFileName);
        jarEntries = jf.entries();
        while (jarEntries.hasMoreElements()) {
            entryName = jarEntries.nextElement().getName();
            if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5) {
                entryName = entryName.substring(packageName.length(), entryName.lastIndexOf('.'));
                result.add(Class.forName(entryName));
            }
        }

        return result;
    }

    public static List<Class<?>> enumerateClasses(String packageName, File directory) throws Exception {
        List<Class<?>> result = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) {
            return result;
        }

        String entryName;
        for (File actual : files) {
            entryName = actual.getName();
            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
            result.add(Class.forName(packageName + "." + entryName));
        }
        return result;
    }
}
