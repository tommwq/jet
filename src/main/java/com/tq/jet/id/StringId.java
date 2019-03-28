package com.tq.jet.id;

/**
 * 使用String记录的ID。
 *
 */
public class StringId extends Id {
        private final String INVALID_ID = "";
        private String id = INVALID_ID;

        public StringId() {
        }

        public StringId(String id) {
                this.id = id;
        }

        @Override public boolean equals(Object o) {
                if (!(o instanceof StringId)) {
                        return false;
                }

                StringId rhs = (StringId) o;
                return id == rhs.id;
        }

        @Override public void fromString(String represent) {
                id = represent;
        }

        @Override public String toString() {
                return id;
        }

        @Override public int hashCode() {
                return id.hashCode();
        }

        @Override public boolean isValid() {
                return id != INVALID_ID;
        }
}

