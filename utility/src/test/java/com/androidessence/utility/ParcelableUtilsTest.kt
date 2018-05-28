package com.androidessence.utility

import junit.framework.TestCase.*
import org.junit.Test

/**
 * Tests some of the utility methods in ParcelableUtils.kt
 *
 * Created by adam.mcneilly on 3/22/17.
 */
class ParcelableUtilsTest {
    @Test
    fun testBooleanAsInt() {
        // Test true becomes 1 and false becomes 0
        var testBoolean = true
        assertEquals(1, testBoolean.asInt())

        testBoolean = false
        assertEquals(0, testBoolean.asInt())
    }

    @Test
    fun testIntAsBoolean() {
        // Test 0 becomes false and 1 becomes true
        var testInt = 0
        assertFalse(testInt.asBoolean())

        testInt = 1
        assertTrue(testInt.asBoolean())
    }
}