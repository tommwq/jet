package com.tommwq.jet.event.id;

/**
 * 使用Long记录的ID。
 */
public class LongId implements Id {
  private final Long INVALID_ID = 0L;
  private Long id = INVALID_ID;

  public LongId() {
  }

  public LongId(long id) {
    this.id = id;
  }

  @Override
  public boolean isSame(Id o) {
    if (!(o instanceof LongId)) {
      return false;
    }

    LongId rhs = (LongId) o;
    return id == rhs.id;
  }

  @Override
  public boolean isValid() {
    return id != INVALID_ID;
  }
}

