package com.tommwq.jet.codec;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES + Base64 加解密类。
 * <p>
 * 依次对明文进行加密和base64编码。
 */
public class AesBase64Endec implements Endec<String> {
  private final Charset charset;
  private final AesEndec endec;

  public AesBase64Endec(Charset charset, String randomKey) {
    this.charset = charset;
    endec = new AesEndec(randomKey.getBytes(charset));
  }

  /**
   * 加密
   * <p>
   * 首先用charset对字符串解码，然后进行加密，再用Base64编码。
   *
   * @param rawString 待加密的原始字符串
   * @return 加密后经Base64编码的字符串
   */
  @Override
  public String encrypt(String rawString) throws Exception {
    byte[] encrypted = endec.encrypt(rawString.getBytes(charset));
    return Base64.getEncoder().encodeToString(encrypted);
  }

  /**
   * 解密
   * <p>
   * 首先对字符串进行Base64解码，然后进行解密，再用charset编码。
   *
   * @param base64Encrypted 使用Baes64编码的加密字符串
   * @return 解密结果
   */
  @Override
  public String decrypt(String base64Encrypted) throws Exception {
    byte[] decoded = Base64.getDecoder().decode(base64Encrypted.getBytes(StandardCharsets.UTF_8));
    byte[] decrypted = endec.decrypt(decoded);
    return new String(decrypted, charset);
  }
}
