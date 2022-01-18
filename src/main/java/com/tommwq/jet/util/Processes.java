package com.tommwq.jet.util;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

public class Processes {

    public static Process createProcess(String... commandLine) throws IOException {
        return createProcess(Arrays.asList(commandLine));
    }

    public static Process createProcess(List<String> commandLine) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandLine);
        processBuilder.inheritIO();
        return processBuilder.start();
    }

    public static long pid() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    public static long startTime() {
        return ManagementFactory.getRuntimeMXBean().getStartTime();
    }
}
