package com.tommwq.jet.system.clock;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 定时器。
 */
public class Ticker {
    private final Duration tickDuration;
    private final LocalDateTime lastTickTime;
    private final Clock clock;

    /**
     * 建立定时器。
     *
     * @param clock        时钟。
     * @param tickDuration 每次Tick时间间隔。
     * @throws IllegalArgumentException 如果millisecondsPerTick小于等于0，抛出此异常。
     */
    public Ticker(Clock clock, Duration tickDuration) {
        if (tickDuration.isNegative() || tickDuration.isZero()) {
            throw new IllegalArgumentException();
        }

        this.tickDuration = tickDuration;
        this.clock = clock;
        lastTickTime = clock.getTime();
    }

    /**
     * 建立定时器，采用系统默认时钟。
     *
     * @param tickDuration 每次Tick时间间隔。
     * @throws IllegalArgumentException 如果millisecondsPerTick小于等于0，抛出此异常。
     */
    public Ticker(Duration tickDuration) {
        this(new DefaultSystemClock(), tickDuration);
    }

    /**
     * 建立定时器，采用系统默认时钟，定时间隔为 1 秒。
     */
    public Ticker() {
        this(Duration.ofSeconds(1));
    }

    /**
     * 等待下次间隔。
     *
     * @throws InterruptedException 如果线程中断，抛出此异常。
     */
    public void tick() throws InterruptedException {
        LocalDateTime now = clock.getTime();
        Duration duration = Duration.between(lastTickTime, clock.getTime());
        long millis = tickDuration.toMillis() - duration.toMillis();
        if (millis <= 0) {
            return;
        }

        Thread.sleep(millis);
    }
}
