package com.tommwq.jet.codec;


import java.nio.charset.Charset;

/**
 * AES加解密类。输入输出为字符串
 */
public class StringAesEndec implements Endec<String> {
    private Charset charset;
    private AesEndec endec;

    public StringAesEndec(Charset charset, String randomKey) {
        this.charset = charset;
        endec = new AesEndec(randomKey.getBytes(charset));
    }

    @Override
    public String encrypt(String content) throws Exception {
        return new String(endec.encrypt(content.getBytes(charset)), charset);
    }

    @Override
    public String decrypt(String encrypted) throws Exception {
        return new String(endec.decrypt(encrypted.getBytes(charset)), charset);
    }
}
