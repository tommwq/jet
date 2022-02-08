package com.tommwq.jet.cache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TemporalCacheTest {

    @Test
    public void test() {
        TemporalCache<Integer> cache1 = new TemporalCache<>(TemporalCacheTest::supply);
        assertEquals(1, (int) cache1.get());
    }

    public static int supply() {
        return 1;
    }
}