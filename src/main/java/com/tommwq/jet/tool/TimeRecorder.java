package com.tommwq.jet.tool;

import com.tommwq.jet.system.clock.Clock;
import com.tommwq.jet.system.clock.DefaultSystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * 计时器
 */
public class TimeRecorder {

    private long start = 0;
    private List<TimeRecord> records = new ArrayList();
    private Clock clock;
    public TimeRecorder() {
        this.clock = new DefaultSystemClock();
    }

    public TimeRecorder(Clock clock) {
        this.clock = clock;
    }

    /**
     * 启动计时器
     */
    public void start() {
        if (start > 0) {
            return;
        }
        this.start = now();
        records.clear();
    }

    /**
     * 重置计时器
     */
    public void reset() {
        this.start = now();
        records.clear();
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    public long now() {
        return System.currentTimeMillis();
    }

    /**
     * 停止计时器
     */
    public void stop() {
        start = 0;
    }

    /**
     * 计时
     *
     * @param tag 计时标签
     */
    public void record(String tag) {
        if (start == 0) {
            return;
        }
        TimeRecord tr = new TimeRecord();
        tr.time = now() - start;
        tr.tag = tag;
        records.add(tr);
    }

    /**
     * 显示计时结果
     * <p>
     * TODO 以Printer为参数输出
     */
    public void show() {
        if (start == 0) {
            return;
        }

        System.out.println("--- TIME RECORD ----");
        for (TimeRecord tr : records) {
            System.out.printf("- %d ms %s%n", (int) tr.time, tr.tag);
        }
    }

    private static class TimeRecord {
        public long time;
        public String tag;
    }
}
