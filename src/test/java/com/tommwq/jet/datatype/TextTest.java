package com.tommwq.jet.datatype;

import org.junit.Assert;
import org.junit.Test;

public class TextTest {

    @Test
    public void containNonAscii() {
        String s1 = "abc123";
        String s2 = "你好";

        Assert.assertFalse(Text.containNonAscii(s1));
        Assert.assertTrue(Text.containNonAscii(s2));
    }
}