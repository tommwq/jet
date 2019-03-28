/**
 * 类型相关的效用函数。
 * modify: 2016-10-11
 */

package com.tq.jet.type;

/**
 * 类型转换常用函数类。
 */
public class TypeUtils {
        private TypeUtils() {}
        
        /**
         * 将Object转换为boolan型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static boolean convert(Object value, boolean defaultValue) {
                try {
                        return (Boolean) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }
        /**
         * 将Object转换为byte型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static byte convert(Object value, byte defaultValue) {
                try {
                        return (Byte) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }

        /**
         * 将Object转换为short型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static short convert(Object value, short defaultValue) {
                try {
                        return (Short) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }

        /**
         * 将Object转换为int型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static int convert(Object value, int defaultValue) {
                try {
                        return (Integer) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }

        /**
         * 将Object转换为long型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static long convert(Object value, long defaultValue) {
                try {
                        return (Long) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }

        /**
         * 将Object转换为String型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static String convert(Object value, String defaultValue) {
                try {
                        return (String) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }

        /**
         * 将Object转换为float型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static float convert(Object value, float defaultValue) {
                try {
                        return (Float) value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }

        /**
         * 将Object转换为double型。
         * @param value 值
         * @param defaultValue 默认值
         * @return 转换后的值
         */
        public static double convert(Object value, double defaultValue) {
                try {
                        return (Double)value;
                } catch (ClassCastException e) {
                        return defaultValue;
                }
        }
}
        
