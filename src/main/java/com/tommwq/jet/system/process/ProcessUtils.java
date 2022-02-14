package com.tommwq.jet.system.process;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class ProcessUtils {

    public static Process createProcess(String... commandLine) throws IOException {
        return createProcess(Arrays.asList(commandLine));
    }

    public static Process createProcess(List<String> commandLine) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandLine);
        processBuilder.inheritIO();
        return processBuilder.start();
    }

    public static long getProcessId() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    public static LocalDateTime getProcessStartTime() {
        long milliseconds = ManagementFactory.getRuntimeMXBean().getStartTime();
        long seconds = milliseconds / 1000;
        long nanoSeconds = (milliseconds % 1000) * 1000;

        return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanoSeconds),
                TimeZone.getDefault().toZoneId());
    }
}
