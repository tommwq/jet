package com.tommwq.jet.codec;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * AES + urlencode 加解密类。
 * <p>
 * 依次对明文进行加密、base64编码、urlencode。
 */
public class AesBase64UrlencodeEndec implements Endec<String> {
  private final Charset charset;
  private final AesEndec endec;

  public AesBase64UrlencodeEndec(Charset charset, String randomKey) {
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
    return URLEncoder.encode(Base64.getEncoder().encodeToString(encrypted), "UTF-8");
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
    byte[] decoded = Base64.getDecoder().decode(URLDecoder.decode(base64Encrypted, "UTF-8"));
    byte[] decrypted = endec.decrypt(decoded);
    return new String(decrypted, charset);
  }
}
