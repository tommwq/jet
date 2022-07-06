package com.tommwq.jet.event.id;

public class InvalidId implements Id {
  public boolean isSame(Id aId) {
    return false;
  }

  public boolean isValid() {
    return false;
  }
}
