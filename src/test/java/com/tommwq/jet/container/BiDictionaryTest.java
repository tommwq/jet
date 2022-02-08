package com.tommwq.jet.container;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BiDictionaryTest {

    @Test
    public void test() {
        BiDictionary<Integer, Integer> dict1 = new BiDictionary<>();

        dict1.put(1, 2);
        dict1.put(2, 3);

        assertEquals(1, (int) dict1.findValue(2).get());
    }
}