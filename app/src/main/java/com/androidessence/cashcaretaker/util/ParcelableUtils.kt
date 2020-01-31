package com.androidessence.cashcaretaker.util

/**
 * Utility methods for parcelables.
 */

fun Int.asBoolean(): Boolean = (this != 0)

fun Boolean.asInt(): Int = if (this) 1 else 0
