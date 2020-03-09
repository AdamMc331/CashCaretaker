package com.androidessence.cashcaretaker.database

import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ConvertersTest {
    private val converter = Converters()

    @Test
    fun fromTimestamp() {
        val timestamp = System.currentTimeMillis()
        val date = Date(timestamp)
        assertEquals(date, converter.fromTimestamp(timestamp))

        assertNull(converter.fromTimestamp(null))
    }

    @Test
    fun dateToTimestamp() {
        val date = Date()
        val timestamp = date.time
        assertEquals(timestamp, converter.dateToTimestamp(date))

        assertNull(converter.dateToTimestamp(null))
    }
}
