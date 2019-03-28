package com.tq.jet.event;

import com.tq.jet.id.Id;

/**
 * 业务事件。
 */
public interface Event {
        /**
         * @return 业务类型编号。
         */
        Id getMajorId();
        /**
         * @return  业务编号。
         */
        Id getMinorId();
        /**
         * @return 附加消息。
         */
        String getMessage();
        /**
         * @return 事件创建时间。
         */
        long getCreateTime();
}
