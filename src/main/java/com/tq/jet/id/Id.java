package com.tq.jet.id;

/**
 * Id
 */
public class Id {
        @Override public int hashCode() {
                return 0;
        }

        @Override public boolean equals(Object o) {
                return false;
        }

        /**
         * 从字符串解析Id
         *
         * @param represent 表示Id的字符串
         */
        public void fromString(String represent) {
                return;
        }

        /**
         * 将Id转换为字符串。
         * 
         * @return 表示Id的字符串
         */
        @Override public String toString() {
                return "";
        }

        /**
         * 判断Id是否有效。
         *
         * @return 如果Id有效，返回true；否则返回false。
         */
        public boolean isValid() {
                return false;
        }
}

