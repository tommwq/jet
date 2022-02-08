package com.tommwq.jet.container;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SimpleStackTest {


    @Test
    public void test() {
        Stack<Integer> stack = new SimpleStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        assertEquals(3, (int) stack.top());
        stack.pop();
        assertEquals(2, (int) stack.top());
        stack.pop();
        assertEquals(1, (int) stack.top());
        stack.pop();

        assertTrue(stack.isEmpty());
    }
}