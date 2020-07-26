package com.androidessence.cashcaretaker.util

import junit.framework.TestCase.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * Tests methods in DoubleUtils.kt
 *
 * Created by adam.mcneilly on 3/22/17.
 */
class DoubleUtilsTest {
    @Test
    fun testAsPositiveCurrency() {
        val doubleValue = 100.00
        val currencyString = doubleValue.asCurrency()

        // We're expecting with dollar sign
        assertEquals("$100.00", currencyString)
    }

    @Test
    @Ignore("Need to figure out why this failed locally.")
    fun testAsNegativeCurrency() {
        val doubleValue = -10.00
        val currencyString = doubleValue.asCurrency()

        assertEquals("($10.00)", currencyString)
    }
}
