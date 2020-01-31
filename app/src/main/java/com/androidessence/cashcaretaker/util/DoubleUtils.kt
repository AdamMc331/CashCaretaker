package com.androidessence.cashcaretaker.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Extension method for Doubles
 */

fun Double.asCurrency(): String = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
