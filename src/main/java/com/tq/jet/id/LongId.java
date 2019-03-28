package com.tq.jet.id;

/**
 * 使用Long记录的ID。
 *
 */
public class LongId extends Id {
        private final Long INVALID_ID = 0L;
        private Long id = INVALID_ID;

        public LongId() {
        }

        public LongId(long id) {
                this.id = id;
        }

        @Override public boolean equals(Object o) {
                if (!(o instanceof LongId)) {
                        return false;
                }

                LongId rhs = (LongId) o;
                return id == rhs.id;
        }

        @Override public void fromString(String represent) {
                id = Long.parseLong(represent);
        }

        @Override public String toString() {
                return id.toString();
        }

        @Override public int hashCode() {
                return id.hashCode();
        }

        @Override public boolean isValid() {
                return id != INVALID_ID;
        }
}

