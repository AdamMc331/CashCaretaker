package com.androidessence.cashcaretaker.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility methods for Dates in Kotlin
 */

object DateFormats {
    private const val UI_DATE_FORMAT = "MMMM dd, yyyy"

    @SuppressLint("ConstantLocale")
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

fun Date.asUIString(): String {
    val year = this.year()

    if (year < 0) {
        throw UnsupportedOperationException("Date has negative year value.")
    } else {
        return DateFormats.UI_DATE_FORMATTER.format(this)
    }
}
