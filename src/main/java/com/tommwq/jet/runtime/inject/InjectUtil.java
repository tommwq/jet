package com.tommwq.jet.runtime.inject;

import com.tommwq.jet.system.thread.ThreadUtils;
import com.tommwq.jet.container.Container;
import com.tommwq.jet.system.io.FileUtils;
import com.tommwq.jet.function.Call;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class InjectUtil {

    /**
     * scan java class names under directory
     */
    public static List<String> scanDirectory(File root, String packageName) throws FileNotFoundException, IOException {
        List<String> result = new ArrayList<>();

        List<File> descendants = FileUtils.walk(root.toPath());
        for (File file : descendants) {
            if (!file.getName().endsWith(".class")) {
                continue;
            }

            String relativeName = file.getAbsolutePath().replace(root.getAbsolutePath(), "");
            if (relativeName.startsWith("\\")) {
                relativeName = relativeName.substring(1);
            }
            if (relativeName.startsWith("/")) {
                relativeName = relativeName.substring(1);
            }

            String className = getClassNameFromFileName(relativeName);
            if (!className.startsWith(packageName)) {
                continue;
            }

            result.add(className);
        }

        return result;
    }

    /**
     * scan java class names in a jar file
     */
    public static List<String> scanJar(File jarFile, String packageName) throws IOException {
        List<String> result = new ArrayList<>();

        JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile));
        for (JarEntry jarEntry = jarStream.getNextJarEntry();
             jarEntry != null;
             jarEntry = jarStream.getNextJarEntry()) {

            if (jarEntry.isDirectory()) {
                continue;
            }

            String entryName = jarEntry.getName();
            if (!entryName.endsWith(".class")) {
                continue;
            }

            String className = getClassNameFromFileName(entryName);
            if (!className.startsWith(packageName)) {
                continue;
            }

            result.add(className);
        }

        return result;
    }

    public static String getClassNameFromFileName(String classFileName) {
        return classFileName.replace("/", ".")
                .replace("\\", ".")
                .replace(".class", "");
    }

    public static List<Class> scanAndLoad(String packageName, URLClassLoader classLoader) {
        List<String> classNameList = new ArrayList<>();
        for (URL path : classLoader.getURLs()) {
            File file = new File(path.getFile());

            if (file.isFile()) {
                classNameList.addAll((List<String>) new Call((Void) -> InjectUtil.scanJar(file, packageName),
                        null,
                        Container.list()).result());
            }

            if (file.isDirectory()) {
                classNameList.addAll((List<String>) new Call((Void) -> InjectUtil.scanDirectory(file, packageName),
                        null,
                        Container.list()).result());
            }
        }

        return classNameList.stream()
                .map(name -> (Class) new Call((Void) -> classLoader.loadClass(name),
                        null,
                        null)
                        .result())
                .filter(clazz -> clazz != null)
                .collect(Collectors.toList());
    }

    public static List<Class> scanAndLoad(String packageName) {
        return scanAndLoad(packageName, ThreadUtils.classLoader());
    }
}
