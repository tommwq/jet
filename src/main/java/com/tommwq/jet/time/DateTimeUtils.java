package com.tommwq.jet.time;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 容器辅助函数。
 */
public class DateTimeUtils {

    // TODO 增加 earliest

    /**
     * 返回较晚的日期。
     */
    @Nullable
    public static Optional<LocalDate> latest(LocalDate... dates) {
        LocalDate result = null;
        for (LocalDate date : dates) {
            if (date == null) {
                continue;
            }
            if (result == null || result.isBefore(date)) {
                result = date;
            }
        }

        return Optional.ofNullable(result);
    }

    @Nullable
    public static Optional<LocalDateTime> latest(LocalDateTime... dates) {
        LocalDateTime result = null;
        for (LocalDateTime date : dates) {
            if (date == null) {
                continue;
            }
            if (result == null || result.isBefore(date)) {
                result = date;
            }
        }

        return Optional.ofNullable(result);
    }
}
