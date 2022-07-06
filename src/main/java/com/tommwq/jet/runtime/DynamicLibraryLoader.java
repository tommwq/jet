package com.tommwq.jet.runtime;


import java.io.File;
import java.lang.reflect.Field;
import java.util.Vector;

/**
 * 加载动态库。
 * 避免多次加载同一个动态库导致UnsatisfiedLinkError。
 */
public class DynamicLibraryLoader {
  private static Field LIBS;

  static {
    try {
      LIBS = ClassLoader.class.getDeclaredField("loadedLibraryNames");
      LIBS.setAccessible(true);
    } catch (Exception e) {
      LIBS = null;
    }
  }

  /**
   * 返回的已加载的动态库，包含路径和后缀。
   */
  public static String[] getLoadedLibraries(final Object obj) {
    String[] empty = new String[]{};
    try {
      final Vector<String> libs = (Vector<String>) LIBS.get(obj.getClass().getClassLoader());
      return libs.toArray(new String[]{});
    } catch (Exception e) {
      return empty;
    }
  }

  /**
   * 返回的已加载的动态库，包含路径和后缀。
   */
  public static String[] getLoadedLibraries(final Class clazz) {
    String[] empty = new String[]{};
    try {
      final Vector<String> libs = (Vector<String>) LIBS.get(clazz.getClassLoader());
      return libs.toArray(new String[]{});
    } catch (Exception e) {
      return empty;
    }
  }

  /**
   * 加载动态库。
   */
  public static void load(String libname, final Object obj) {
    if (isLoaded(libname, obj)) {
      return;
    }
    System.loadLibrary(libname);
  }

  /**
   * 判断动态库是否被加载。
   */
  public static boolean isLoaded(String libname, final Object obj) {
    return match(libname, getLoadedLibraries(obj));
  }

  /**
   * 判断库名是否在路径列表中。
   */
  private static boolean match(String libname, String[] fullnames) {
    boolean result = false;
    for (String fullname : fullnames) {
      File file = new File(fullname);
      String basename = file.getName();

      if (basename.equals("lib" + libname + ".so")) {
        result = true;
      }
      if (basename.toLowerCase().equals(libname.toLowerCase() + ".dll")) {
        result = true;
      }
    }
    return result;
  }
}

