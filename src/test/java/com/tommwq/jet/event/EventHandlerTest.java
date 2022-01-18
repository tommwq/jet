package com.tommwq.jet.event;

import org.junit.*;

import java.util.BitSet;

public class EventHandlerTest {
    SimpleEventHandler handler = new SimpleEventHandler();

    @Test
    public void testEventHandler() {
        handler.receiveEvent(new TestEvent());
        Assert.assertTrue(handler.isAllHandled());
    }
}

class TestEvent extends Event<TestEvent> {
    public TestEvent copy() {
        return new TestEvent();
    }
}

class SimpleEventHandler extends EventHandler {
    private BitSet bitSet = new BitSet(2);

    public SimpleEventHandler() {
        bind();
    }

    public boolean isAllHandled() {
        return bitSet.cardinality() == bitSet.length();
    }

    @OnEvent(TestEvent.class)
    public void onTestEvent(TestEvent e) {
        bitSet.set(0);
    }

    @OnEvent(TestEvent.class)
    public void onTestEvent() {
        bitSet.set(1);
    }
}
