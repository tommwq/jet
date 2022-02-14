package com.tommwq.jet.system.process;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

public class ProcessUtilsTest {

    @Test
    public void getProcessId() {
        long pid = ProcessUtils.getProcessId();
        System.out.println(pid);

    }

    @Test
    public void getProcessStartTime() {
        LocalDateTime startTime = ProcessUtils.getProcessStartTime();
        System.out.println(startTime);
    }
}