package com.tommwq.jet.event.id;

public interface Id {
    boolean isValid();

    boolean isSame(Id aId);
}
