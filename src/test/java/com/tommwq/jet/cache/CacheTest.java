package com.tommwq.jet.cache;

import com.tommwq.jet.system.clock.Clock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;

public class CacheTest {

    private static Clock clock;

    @BeforeClass
    public static void setup() {
        clock = Mockito.mock(Clock.class);
    }

    public void setTime(int minute, int second) {
        Mockito.when(clock.getTime()).thenReturn(LocalDateTime.of(2022, 1, 1, 0, minute, second));
    }

    @Test
    public void testCache() {
        setTime(0, 0);
        Cache<Integer> cache = new Cache<>(1, Duration.ofSeconds(5), clock);
        setTime(0, 1);
        Assert.assertTrue(cache.get().isPresent());
        setTime(0, 5);
        Assert.assertFalse(cache.get().isPresent());

        cache.set(2);
        setTime(0, 8);
        cache.touch();
        setTime(0, 11);
        Assert.assertTrue(cache.get().isPresent());
        setTime(0, 13);
        Assert.assertFalse(cache.get().isPresent());
    }
}