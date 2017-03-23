package com.androidessence.utility

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Tests methods in DoubleUtils.kt
 *
 * Created by adam.mcneilly on 3/22/17.
 */
class DoubleUtilsTest {
    @Test
    fun testAsCurrency() {
        val doubleValue = 100.00
        val currencyString = doubleValue.asCurrency()

        // We're expecting with dollar sign
        assertEquals("$100.00", currencyString)
    }
}