package com.tommwq.jet.runtime.reflect;
//   assertThat(actual, is(expected));
//   assertThat(actual, is(not(expected)));
// }
// If you have a recent version of Junit installed with hamcrest, just add these imports:

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ReflectUtilTest {

        public static class B {}
        public static class D1 extends B {}
        public static class D2 extends D1 {}

        @Test
        public void getAllSuperclass() {
                List<Class> result = ReflectUtils.getAllSuperclass(D2.class);
                List<Class> expect = Arrays.asList(D1.class, B.class, Object.class);
                assertThat(result, is(expect));
        }
}
