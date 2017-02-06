package com.androidessence.utility

import java.text.NumberFormat
import java.util.*

/**
 * Extension method for Doubles
 *
 * Created by adam.mcneilly on 2/6/17.
 */
fun Double.asCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
}