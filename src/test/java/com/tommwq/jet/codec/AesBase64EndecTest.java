package com.tommwq.jet.codec;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class AesBase64EndecTest {

    @Test
    public void test() throws Exception {
        AesBase64Endec endec = new AesBase64Endec(StandardCharsets.UTF_8, "1234567812345678");
        String plain = "hello";
        String secret = endec.encrypt(plain);
        assertEquals(plain, endec.decrypt(secret));
    }
}