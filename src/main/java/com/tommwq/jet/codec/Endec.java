package com.tommwq.jet.codec;

/**
 * 加密/解密
 */
public interface Endec<T> {
  /**
   * 加密
   *
   * @param content 待价密内容
   * @return 加密后内容
   */
  T encrypt(T content) throws Exception;

  /**
   * 解密
   *
   * @param encrypted 待解密内容
   * @return 解密后内容
   */
  T decrypt(T encrypted) throws Exception;
}
