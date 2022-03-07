package com.tommwq.jet.runtime;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Java 版本
 *
 * @see https://www.oracle.com/java/technologies/javase/jdk8-naming.html
 * @see https://docs.oracle.com/javase/9/install/version-string-format.htm#JSJIG-GUID-DCA60310-6565-4BB6-8D24-6FF07C1C4B4E
 */
public class JavaVersion {
    private static final String UPDATE_SEPARATOR = "_";
    private static final String VERSION_SEPARATOR = "\\.";
    
    private String major = "";
    private String minor = "";
    private String security = "";
    private String patch = "";
    private String update = "";
    private String origin;

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getSecurity() {
        return security;
    }

    public String getPatch() {
        return patch;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String value) {
        update = value;
    }

    private String version;
    public JavaVersion(String javaVersion) {
        origin = javaVersion;
        parse();
    }

    private void parse() {
        String[] blocks = origin.split(UPDATE_SEPARATOR);
        if (blocks.length > 1) {
            update = blocks[1];
        }

        if (blocks.length == 0) {
            return;
        }

        blocks = blocks[0].split(VERSION_SEPARATOR);
        if (blocks.length > 0) {
            major = blocks[0];
        }
        if (blocks.length > 1) {
            minor = blocks[1];
        }
        if (blocks.length > 2) {
            security = blocks[2];
        }
        if (blocks.length > 3) {
            patch = blocks[3];
        }

        if ("1".equals(major)) {
            major = minor;
            minor = security;
            security = "";
            patch = "";
        }
    }

    @Override
    public String toString() {
        return origin;
    }
    
    public static JavaVersion getJavaVersion() {
        return new JavaVersion(System.getProperty("java.version"));
    }
}
