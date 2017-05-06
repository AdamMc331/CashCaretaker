package com.androidessence.utility

/**
 * Utility methods for optional properties.
 *
 * Created by adam.mcneilly on 5/6/17.
 */
fun Boolean?.default(default: Boolean): Boolean {
    return this ?: default
}

fun Long?.default(default: Long): Long {
    return this ?: default
}