package com.androidessence.cashcaretaker.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Extension method for Doubles
 *
 * TODO: Shouldn't be US specific.
 */

fun Double.asCurrency(): String = NumberFormat.getCurrencyInstance(Locale.US).format(this)
