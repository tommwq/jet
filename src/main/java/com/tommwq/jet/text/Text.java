package com.tommwq.jet.text;

/**
 * 字符串辅助类
 */
public class Text {

  /**
   * 判断一个字符串是否是16进制数。
   * @param s 输入字符串
   * @return 如果s表示一个16进制数，返回true；否则返回false。
   */
  public static boolean isHexString(String s) {
    Integer i = null;
    try {
      i = Integer.parseInt(s, 16);
    } catch (Exception ignore) {
      // dummy
    }

    return (i != null);
  }
}
