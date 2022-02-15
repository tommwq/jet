package com.tommwq.jet.runtime.bean;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class BeanManagerTest {

    BeanManager beanManager;

    @Before
    public void setup() {
        beanManager = new BeanManager();
        beanManager.setBean(Integer.class, 1);
    }

    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        Optional<Double> b1 = beanManager.findBean(Double.class);
        assertFalse(b1.isPresent());


        int x = beanManager.getBean(Integer.class);
        assertEquals(1, x);
    }
}