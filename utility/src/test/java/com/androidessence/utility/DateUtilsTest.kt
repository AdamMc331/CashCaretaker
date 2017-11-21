package com.androidessence.utility

import junit.framework.TestCase.assertEquals
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

    @Test
    fun testAsUIString() {
        val dateString = date.asUIString()
        assertEquals(UI_DATE_STRING, dateString)
    }

    companion object {
        private val YEAR = 2017
        private val MONTH = 1
        private val DAY = 1

        // Setup DB date string and UI date string expected here
        private val UI_DATE_STRING = "February 01, 2017"
    }
}