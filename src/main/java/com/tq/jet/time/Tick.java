/**
 * File: Tick.java
 * Description:
 * @author Wang Qian
 * Create date 2016-09-15
 * Modify date 2016-10-10
 */

package com.tq.jet.time;

/**
 * 定时器。
 */
public class Tick {
        private long millisecondsPerTick = 0;
        private long lastTickTime = 0;
        private Clock clock;

        /**
         * 建立定时器。
         *
         * @param clock 时钟。
         * @param millisecondsPerTick 每次Tick时间间隔。
         *
         * @throws IllegalArgumentException 如果millisecondsPerTick小于等于0，抛出此异常。
         */
        public Tick(Clock clock, long millisecondsPerTick) {
                if (millisecondsPerTick <= 0) {
                        throw new IllegalArgumentException();
                }
                this.millisecondsPerTick = millisecondsPerTick;
                this.clock = clock;
        }

        /**
         * 建立定时器。
         *
         * @param millisecondsPerTick 每次Tick时间间隔。
         *
         * @throws IllegalArgumentException 如果millisecondsPerTick小于等于0，抛出此异常。
         */
        public Tick(long millisecondsPerTick) {
                if (millisecondsPerTick <= 0) {
                        throw new IllegalArgumentException();
                }
                this.millisecondsPerTick = millisecondsPerTick;
                clock = new DefaultSystemClock();
        }

        /**
         * 等待下次间隔。
         *
         * @throws InterruptedException 如果线程中断，抛出此异常。
         */
        public void tick() throws InterruptedException {
                long now = clock.now();
                long elapsed = now - lastTickTime;
                lastTickTime = now;
                if (elapsed >= millisecondsPerTick) {
                        return;
                }
                Thread.sleep(millisecondsPerTick - elapsed);
        }
}
