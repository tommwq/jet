package com.tommwq.jet.runtime;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class JavaVersionTest {

    @Test
    public void testJavaVersion() {
        JavaVersion jv1 = new JavaVersion("1.8.0_192");
        assertEquals("8", jv1.getMajor());
        assertEquals("0", jv1.getMinor());
        assertEquals("", jv1.getSecurity());
        assertEquals("", jv1.getPatch());
        assertEquals("192", jv1.getUpdate());
        
        JavaVersion jv2 = new JavaVersion("17.0.2");
        assertEquals("17", jv2.getMajor());
        assertEquals("0", jv2.getMinor());
        assertEquals("2", jv2.getSecurity());
        assertEquals("", jv2.getPatch());
        assertEquals("", jv2.getUpdate());        
    }
}
