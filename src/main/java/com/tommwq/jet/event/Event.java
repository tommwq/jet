package com.tommwq.jet.event;

import com.tommwq.jet.event.id.Id;
import com.tommwq.jet.event.id.InvalidId;

import java.util.Calendar;

public abstract class Event<T extends Event<?>> {

    private final long timestamp = Calendar.getInstance().getTimeInMillis();
    private Id source = new InvalidId();
    private Id target = new InvalidId();

    public Event() {
    }

    public Event(Id aSource) {
        source = aSource;
    }

    public Event(Id aSource, Id aTarget) {
        source = aSource;
        target = aTarget;
    }

    public Id getSource() {
        return source;
    }

    public Id getTarget() {
        return target;
    }

    public void setTarget(Id aTarget) {
        target = aTarget;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public abstract T copy();

    public boolean isBroadcast() {
        return !getTarget().isValid();
    }


}
