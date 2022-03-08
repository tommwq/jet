package com.tommwq.jet.runtime;

import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.Optional;

import static org.junit.Assert.*;

public class DynamicLinkingTest {

    @Test
    public void testDynamicLinking() throws Exception {
        File directory = new File("./src/test/resources");
        DynamicLinking.appendUserClasspath(directory);
        
        String libname = "mylib";
        System.loadLibrary(libname);
        assertTrue(true);
    }
}
