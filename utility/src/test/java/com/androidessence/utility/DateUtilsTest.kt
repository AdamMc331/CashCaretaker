package com.androidessence.utility

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Unit tests the DateUtils.kt class.
 *
 * Created by adam.mcneilly on 3/22/17.
 */
class DateUtilsTest {
    private var calendar = Calendar.getInstance()
    private var date = Date()

    @Before
    fun setup() {
        calendar.set(YEAR, MONTH, DAY)
        date = calendar.time
    }

    @Test
    fun testAsCalendar() {
        // Assert by verifying equality
        assertEquals(calendar, date.asCalendar())
    }

    @Test
    fun testYear() {
        assertEquals(YEAR, date.year())
    }

    @Test
    fun testMonth() {
        assertEquals(MONTH, date.month())
    }

    @Test
    fun testDay() {
        assertEquals(DAY, date.day())
    }

    companion object {
        private val YEAR = 2017
        private val MONTH = 1
        private val DAY = 1
    }
}