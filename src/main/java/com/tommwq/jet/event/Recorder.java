package com.tommwq.jet.event;

import com.tommwq.jet.system.io.storage.Storage;

/**
 * 业务事件记录器
 */
public class Recorder {
    private Storage storage;

    public Recorder(Storage storage) {
        this.storage = storage;
    }

    /**
     * 记录事件。
     *
     * @param event 要记录的事件。
     */
    public void record(Event event) {
        storage.write(null, event);
    }

    /**
     * 强制记录事件。
     *
     * @param event 要记录的事件。
     */
    public void mustRecord(Event event) throws UnsupportedOperationException {
        storage.mustWrite(null, event);
    }
}
