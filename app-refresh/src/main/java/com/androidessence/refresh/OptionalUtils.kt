package com.androidessence.refresh

/**
 * Extension methods for optional classes.
 *
 * Created by adam.mcneilly on 2/19/17.
 */
fun Boolean?.orFalse(): Boolean {
    return this ?: false
}