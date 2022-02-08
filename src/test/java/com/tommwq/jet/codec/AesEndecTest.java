package com.tommwq.jet.codec;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AesEndecTest {

    @Test
    public void test() throws Exception {
        AesEndec endec = new AesEndec("1234567812345678".getBytes(StandardCharsets.UTF_8));
        byte[] plain = "hello".getBytes(StandardCharsets.UTF_8);
        byte[] secret = endec.encrypt(plain);
        assertArrayEquals(plain, endec.decrypt(secret));
    }
}