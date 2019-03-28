package com.tq.util.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.tq.util.function.Function;

class FutureUtilsTest {
        @Test
        void sequence() {
                // calculate (1 + 2) * 3
                try {
                        int result = FutureUtils.sequence(1, new Function<Integer,Integer>(){
                                        @Override public Integer apply(Integer x) {
                                                return x + 2;
                                        }}, new Function<Integer,Integer>() {
                                                        @Override public Integer apply(Integer x) {
                                                                return x * 3;
                                                        }});
                        assertEquals(9, result);
                } catch (Exception e) {
                        fail(e);
                }
        }
}

