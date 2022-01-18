package com.tommwq.jet.container;

public class HashUtil {

    public static int hash(String... array) {
        int hashCode = 0;

        for (String element : array) {
            hashCode = hash(element, hashCode);
        }

        return hashCode;
    }

    public static int hash(String string) {
        return hash(string, 0);
    }

    public static int hash(String string, int seed) {
        int hashCode = seed;

        for (int i = 0; i < string.length(); i++) {
            hashCode = 31 * hashCode + string.charAt(i);
        }

        return hashCode;
    }
}
