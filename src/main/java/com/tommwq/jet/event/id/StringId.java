package com.tommwq.jet.event.id;

public class StringId implements Id {
    private final String id;

    public StringId(String aId) {
        id = aId;
    }

    public boolean isSame(Id aId) {
        return aId != null && aId instanceof StringId && id.equals(((StringId) aId).id);
    }

    public boolean isValid() {
        return true;
    }
}
