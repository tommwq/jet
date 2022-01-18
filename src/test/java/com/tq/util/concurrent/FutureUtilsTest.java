package com.tq.util.concurrent;

import com.tommwq.jet.routine.Function;
import com.tommwq.jet.runtime.concurrent.FutureUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

class FutureUtilsTest {
    @Test
    void sequence() {
        // calculate (1 + 2) * 3
        try {
            int result = FutureUtils.sequence(1, new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer x) {
                    return x + 2;
                }
            }, new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer x) {
                    return x * 3;
                }
            });
            assertEquals(9, result);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

