package com.tommwq.jet.datatype;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Text {

    public static String underlineToCamelCase(String underline) {
        return pascalCaseToCamelCase(underlineToPascalCase(underline));
    }

    public static String underlineToPascalCase(String underline) {
        return Stream.of(underline.split("_"))
                .map(str -> camelCaseToPascalCase(str))
                .reduce(String::concat)
                .get();
    }

    public static String camelCaseToPascalCase(String camelCase) {
        if (camelCase.isEmpty()) {
            return camelCase;
        }

        return camelCase.substring(0, 1).toUpperCase() + camelCase.substring(1);
    }

    public static String pascalCaseToCamelCase(String pascalCase) {
        if (pascalCase.isEmpty()) {
            return pascalCase;
        }

        return pascalCase.substring(0, 1).toLowerCase() + pascalCase.substring(1);
    }

    /**
     * Dump object to string, support primitive type, Object and array.
     */
    public static String stringify(Object object) {
        if (object == null) {
            return "null";
        }

        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            return "Object[]{" + String.join(",", stringify(array)) + "}";
        }

        return object.toString();
    }

    /**
     * Dump array to string.
     */
    public static String[] stringify(Object[] objects) {
        if (objects == null) {
            return new String[]{};
        }

        return Stream.of(objects)
                .map(Text::stringify)
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    public static boolean isSubstring(String str, String pattern, int fromIndex) {
        return (str.indexOf(pattern, fromIndex) != -1);
    }

    public static boolean isSubstring(String str, String pattern) {
        return isSubstring(str, pattern, 0);
    }
}
