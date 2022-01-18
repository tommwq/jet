package com.tommwq.jet.codec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES加解密类
 */
public class AesEndec implements Endec<byte[]> {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private byte[] randomKey;

    public AesEndec(byte[] randomKey) {
        this.randomKey = randomKey;
    }

    /**
     * 创建Cipher对象
     *
     * @param endecMode 加解密模式，Cipher.ENCRYPT_MODE或Cipher.DECRYPT_MODE
     * @return Cipher对象
     */
    private Cipher createCipher(int endecMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(endecMode, new SecretKeySpec(randomKey, KEY_ALGORITHM));
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] content) throws Exception {
        return createCipher(Cipher.ENCRYPT_MODE).doFinal(content);
    }

    @Override
    public byte[] decrypt(byte[] encrypted) throws Exception {
        return createCipher(Cipher.DECRYPT_MODE).doFinal(encrypted);
    }
}
