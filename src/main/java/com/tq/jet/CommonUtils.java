package com.tq.jet;

import java.io.Closeable;
import java.io.IOException;

/**
 * 通用常用函数
 */
public class CommonUtils {

        /**
         * 安全关闭
         *
         * @param closeable 要关闭的对象
         */
        public static void safeClose(Closeable closeable) {
                if (closeable == null) {
                        return;
                }

                try {
                        closeable.close();
                } catch (IOException e) {
                        // IGNORE
                }
        }
}
