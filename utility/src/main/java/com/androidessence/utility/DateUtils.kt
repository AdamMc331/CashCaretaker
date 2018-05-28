package com.androidessence.utility

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility methods for Dates in Kotlin
 */

object DateFormats {
    private val UI_DATE_FORMAT = "MMMM dd, yyyy"
    val UI_DATE_FORMATTER = SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault())
}

fun Date.asCalendar(): Calendar {
    val c = Calendar.getInstance()
    c.time = this
    return c
}

fun Date.get(field: Int): Int = this.asCalendar().get(field)

fun Date.year(): Int = this.get(Calendar.YEAR)

fun Date.month(): Int = this.get(Calendar.MONTH)

fun Date.day(): Int = this.get(Calendar.DAY_OF_MONTH)

fun Date?.asUIString(): String {
    val year = this?.year() ?: -1

    if (year < 0) {
        throw UnsupportedOperationException("Date has negative year value.")
    } else {
        return DateFormats.UI_DATE_FORMATTER.format(this)
    }
}