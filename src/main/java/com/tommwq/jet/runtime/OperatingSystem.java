package com.tommwq.jet.runtime;

/**
 * 返回运行平台操作系统相关信息
 */
public class OperatingSystem {
    public static String getOperatingSystemName() {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        return getOperatingSystemName().toLowerCase().contains("windows");
    }

    public static boolean isLinux() {
        return getOperatingSystemName().toLowerCase().contains("linux");
    }

    public static boolean isMac() {
        return getOperatingSystemName().toLowerCase().contains("mac");
    }

    public static String getArchitecture() {
        return System.getProperty("os.arch");
    }

    public static boolean is32bit() {
        return getArchitecture().contains("86");
    }

    public static boolean is64bit() {
        return getArchitecture().contains("64");
    }

    public static String getOperatingSystemShortName() {
        if (isWindows()) {
            return "windows";
        } else if (isLinux()) {
            return "linux";
        } else if (isMac()) {
            return "mac";
        }
        return "";
    }

    public static String getDynamicLibrarySuffix() {
        switch (getOperatingSystemShortName()) {
            case "windows":
                return ".dll";
            case "linux":
                return ".so";
            case "mac":
                return ".dylib";
            default:
                return "";
        }
    }

    public static String getDynamicLibraryPrefix() {
        switch (getOperatingSystemShortName()) {
            case "linux":
                return "lib";
            default:
                return "";
        }
    }
}
