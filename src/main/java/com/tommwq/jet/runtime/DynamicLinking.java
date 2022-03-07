package com.tommwq.jet.runtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 用于从 jar 中加载 native 库。
 * <p>
 * native 库应当保存在 jar 包中的 $OS$ARCH 目录下，如 windows64/foo.dll 。
 * <p>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 这份代码目前只在 Java 8 上验证过。是否适用于其他版本的 Java ，尚不确定。
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class DynamicLinking {

    private static final Set<String> loadedLibraries = new ConcurrentSkipListSet<>();
    private File temporaryDirectory = null;
    private File runningDirectory = null;
    private final boolean cacheNativeLibraryToRunningDirectory;

    public DynamicLinking() throws IOException {
        this(true);
    }

    public DynamicLinking(boolean cacheNativeLibraryToRunningDirectory) throws IOException {
        this.cacheNativeLibraryToRunningDirectory = cacheNativeLibraryToRunningDirectory;
        if (cacheNativeLibraryToRunningDirectory) {
            getRunningDirectory();
        } else {
            createTemporaryDirectory();
        }
    }

    private static <T> T[] append(T[] array, T element) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        list.add(element);
        return list.toArray(array);
    }

    public static String getDynamicLibraryPath() {
        return String.format("%s%d",
                OperatingSystem.getOperatingSystemShortName(),
                OperatingSystem.is32bit() ? 32 : 64);
    }

    public static String getDynamicLibraryFileName(String dynamicLibraryFile) {
        return getDynamicLibraryPath() + "/" + dynamicLibraryFile;
    }

    private void createTemporaryDirectory() throws IOException {
        temporaryDirectory = Files.createTempDirectory("tmp").toFile();
        //noinspection ResultOfMethodCallIgnored
        temporaryDirectory.createNewFile();
        temporaryDirectory.deleteOnExit();
    }

    private void getRunningDirectory() {
        runningDirectory = new File(System.getProperty("user.dir"));
    }

    /**
     * 加载本地库
     *
     * @param library 库名字
     */
    public void loadLibrary(String library) throws IOException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        if (loadedLibraries.contains(library)) {
            return;
        }

        String dynamicLibraryFilename = OperatingSystem.getDynamicLibraryPrefix() + library + OperatingSystem.getDynamicLibrarySuffix();
        File targetDirectory = cacheNativeLibraryToRunningDirectory ? runningDirectory : temporaryDirectory;
        File dynamicLibraryFile = new File(targetDirectory, dynamicLibraryFilename);
        if (dynamicLibraryFile.exists()) {
            dynamicLibraryFile.delete();
        }
        dynamicLibraryFile.deleteOnExit();

        String libraryFileName = getDynamicLibraryFileName(dynamicLibraryFilename);
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(libraryFileName)) {
            if (inputStream == null) {
                throw new NoSuchFileException("cannot get library file from resource, resource path: " + libraryFileName);
            }
            Files.copy(inputStream, dynamicLibraryFile.toPath());
        }

        appendUserClasspath(targetDirectory);
        System.loadLibrary(library);

        loadedLibraries.add(library);
    }

    public static void appendUserClasspath(File directory) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        JavaVersion version = JavaVersion.getJavaVersion();
        if ("8".equals(version.getMajor())) {
            appendUserClasspath_Java8(directory);
        } else if ("17".equals(version.getMajor())) {
            appendUserClasspath_Java17(directory);
        } else {
            throw new RuntimeException("unsupported java version");
        }
    }

    private static void appendUserClasspath_Java17(File directory) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Class klass = Class.forName("jdk.internal.loader.NativeLibraries$LibraryPaths");
        Field field = klass.getDeclaredField("USER_PATHS");
        boolean originalAccessible = field.isAccessible();
        field.setAccessible(true);
        String[] USER_PATHS = (String[]) field.get(null);
        String[] newUSER_PATHS = append(USER_PATHS, directory.getAbsolutePath());
        field.set(null, newUSER_PATHS);
        field.setAccessible(originalAccessible);        
    }

    private static void appendUserClasspath_Java8(File directory) throws NoSuchFieldException, IllegalAccessException {
        Field field = ClassLoader.class.getDeclaredField("usr_paths");
        boolean originalAccessible = field.isAccessible();
        field.setAccessible(true);
        String[] usrPaths = (String[]) field.get(null);
        String[] newUsrPaths = append(usrPaths, directory.getAbsolutePath());
        field.set(null, newUsrPaths);
        field.setAccessible(originalAccessible);        
    }
}
