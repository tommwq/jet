package com.tommwq.jet.util;

import javax.annotation.Nullable;
import java.time.LocalDate;

/**
 * 容器辅助函数。
 */
public class DateTimes {

    // TODO 增加 earliest

    /**
     * 返回较晚的日期。
     * TODO 改为 Temporal
     */
    @Nullable
    public static LocalDate latest(LocalDate... dates) {
        LocalDate result = null;
        for (LocalDate date : dates) {
            if (date == null) {
                continue;
            }
            if (result == null || result.isBefore(date)) {
                result = date;
            }
        }

        return result;
    }
}
