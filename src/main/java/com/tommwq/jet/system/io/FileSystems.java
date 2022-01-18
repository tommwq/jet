package com.tommwq.jet.system.io;

import com.tommwq.jet.routine.Call;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystems {
    public static URL fileNameToUrl(String fileName) {
        return (URL) new Call((Void) -> new File(fileName).toURI().toURL(),
                null,
                null).result();
    }

    public static List<String> recurseListProtocolFileNames(File file) {
        return recurseListFileNamesBySuffix(file, ".proto");
    }

    public static List<String> recurseListFileNamesBySuffix(File file, String suffix) {
        return recurseListFileNames(file, f -> f.getName().endsWith(suffix));
    }

    public static List<String> recurseListJavaFileNames(String path) {
        return recurseListJavaFileNames(new File(path));
    }

    public static List<String> recurseListJavaFileNames(File file) {
        return recurseListFileNamesBySuffix(file, ".java");
    }

    public static List<String> recurseListFileNames(File root, Predicate<File> predicate) {
        List<String> result = new ArrayList<>();
        if (root.isDirectory()) {
            return Stream.of(root.listFiles())
                    .map(file -> recurseListFileNames(file, predicate))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }

        if (predicate.test(root)) {
            result.add(root.getAbsolutePath());
        }

        return result;
    }

    public static void createDirectoryInNeed(String path) {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            throw new RuntimeException("fail to create directory '" + path + "' which is existed but not a directory");
        }

        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("fail to create directory '" + path + "'.");
        }
    }

    public static void walk(Path root, List<File> result) throws IOException {
        List<File> children = Files.list(root)
                .map(Path::toFile)
                .collect(Collectors.toList());

        result.addAll(children);

        for (File file : children) {
            if (file.isDirectory()) {
                walk(file.toPath(), result);
            }
        }
    }

    public static List<File> walk(Path root) throws IOException {
        List<File> result = new ArrayList<>();
        walk(root, result);
        return result;
    }

}
