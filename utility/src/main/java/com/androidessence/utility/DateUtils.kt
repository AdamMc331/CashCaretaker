package com.androidessence.utility

import java.util.*

/**
 * Utility methods for Dates in Kotlin
 *
 * Created by adam.mcneilly on 2/6/17.
 */
fun Date.asCalendar(): Calendar {
    val c = Calendar.getInstance()
    c.time = this
    return c
}

fun Date.get(field: Int): Int {
    return this.asCalendar().get(field)
}

fun Date.year(): Int {
    return this.get(Calendar.YEAR)
}

fun Date.month(): Int {
    return this.get(Calendar.MONTH)
}

fun Date.day(): Int {
    return this.get(Calendar.DAY_OF_MONTH)
}