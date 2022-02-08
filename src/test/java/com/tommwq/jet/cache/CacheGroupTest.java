package com.tommwq.jet.cache;

import org.junit.Test;

import static org.junit.Assert.*;

public class CacheGroupTest {

    @Test
    public void test() {
        CacheGroup cacheGroup = new CacheGroup();
        cacheGroup.set("1","1");

        assertEquals("1", cacheGroup.get("1").get());
    }
}