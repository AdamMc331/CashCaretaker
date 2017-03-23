package com.androidessence.utility

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Tests all of our Utility methods.
 *
 * Created by adam.mcneilly on 3/22/17.
 */
class UtilityTest {
    private var calendar = Calendar.getInstance()
    private var date = Date()

    @Before
    fun setup() {
        calendar.set(YEAR, MONTH, DAY)
        date = calendar.time
    }

    @Test
    fun testGetDBDateString() {
        val dateString = Utility.getDBDateString(date)
        assertEquals(DB_DATE_STRING, dateString)
    }

    @Test
    fun testGetUIDateString() {
        val dateString = Utility.getUIDateString(date)
        assertEquals(UI_DATE_STRING, dateString)
    }

    @Test
    fun testGetUIDateStringFromDB() {
        val dateString = Utility.getUIDateStringFromDB(DB_DATE_STRING)
        assertEquals(UI_DATE_STRING, dateString)
    }

    @Test
    fun testGetDateFromDb() {
        val dbDate = Utility.getDateFromDb(DB_DATE_STRING)
        // Compare date portion
        assertEquals(YEAR, dbDate.year())
        assertEquals(MONTH, dbDate.month())
        assertEquals(DAY, dbDate.day())
    }

    companion object {
        private val YEAR = 2017
        private val MONTH = 0 // Java calendar uses 0-index months
        private val DAY = 1

        // Setup DB date string and UI date string expected here
        private val UI_DATE_STRING = "January 01, 2017"
        private val DB_DATE_STRING = "2017-01-01"
    }
}