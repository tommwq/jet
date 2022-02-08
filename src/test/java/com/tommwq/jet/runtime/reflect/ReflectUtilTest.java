package com.tommwq.jet.runtime.reflect;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReflectUtilTest {

    public static class B {
    }

    public static class D1 extends B {
    }

    public static class D2 extends D1 {
    }

    @Test
    public void getAllSuperclass() {
        List<Class> result = ReflectUtils.getAllSuperclass(D2.class);
        List<Class> expect = Arrays.asList(D1.class, B.class, Object.class);
        assertEquals(expect, result);
    }
}
