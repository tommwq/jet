package com.tommwq.jet.util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Packages {

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
