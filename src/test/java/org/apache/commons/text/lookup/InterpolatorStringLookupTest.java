/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link InterpolatorStringLookup}.
 */
class InterpolatorStringLookupTest {

    private static final String TESTKEY = "TestKey";

    private static final String TESTKEY2 = "TestKey2";

    private static final String TESTVAL = "TestValue";

    @AfterAll
    public static void afterAll() {
        System.clearProperty(TESTKEY);
        System.clearProperty(TESTKEY2);
    }

    @BeforeAll
    public static void beforeAll() {
        System.setProperty(TESTKEY, TESTVAL);
        System.setProperty(TESTKEY2, TESTVAL);
    }

    private void assertLookupNotEmpty(final StringLookup lookup, final String key) {
        final String value = lookup.apply(key);
        assertNotNull(value);
        assertFalse(value.isEmpty());
        // System.out.println(key + " = " + value);
    }

    private void check(final StringLookup lookup) {
        String value = lookup.apply("sys:" + TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.apply("env:PATH");
        assertNotNull(value);
        value = lookup.apply("date:yyyy-MM-dd");
        assertNotNull(value, "No Date");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String today = format.format(new Date());
        assertEquals(value, today);
        assertLookupNotEmpty(lookup, "java:version");
        assertLookupNotEmpty(lookup, "java:runtime");
        assertLookupNotEmpty(lookup, "java:vm");
        assertLookupNotEmpty(lookup, "java:os");
        assertLookupNotEmpty(lookup, "java:locale");
        assertLookupNotEmpty(lookup, "java:hardware");
    }

    @Test
    void testLookup() {
        final Map<String, String> map = new HashMap<>();
        map.put(TESTKEY, TESTVAL);
        final StringLookup lookup = new InterpolatorStringLookup(StringLookupFactory.INSTANCE.mapStringLookup(map));
        String value = lookup.apply(TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.apply("ctx:" + TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.apply("sys:" + TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.apply("BadKey");
        assertNull(value);
        value = lookup.apply("ctx:" + TESTKEY);
        assertEquals(TESTVAL, value);
    }

    @Test
    void testLookupKeys() {
        final InterpolatorStringLookup lookup = new InterpolatorStringLookup((Map<String, Object>) null);
        final Map<String, StringLookup> stringLookupMap = lookup.getStringLookupMap();
        StringLookupFactoryTest.assertDefaultKeys(stringLookupMap);
    }

    @Test
    void testLookupWithDefaultInterpolator() {
        check(new InterpolatorStringLookup());
    }

    @Test
    void testLookupWithNullDefaultInterpolator() {
        check(new InterpolatorStringLookup((StringLookup) null));
    }

    @Test
    void testNull() {
        Assertions.assertNull(InterpolatorStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(new InterpolatorStringLookup().toString().isEmpty());
    }
}
